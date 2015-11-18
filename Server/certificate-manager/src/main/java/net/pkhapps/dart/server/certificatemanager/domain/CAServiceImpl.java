package net.pkhapps.dart.server.certificatemanager.domain;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

@Service
class CAServiceImpl implements CAService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CAServiceImpl.class);

    private final CAProperties properties;
    private final PrivateKey privateKey;
    private final X509Certificate certificate;

    @Autowired
    CAServiceImpl(CAProperties properties) throws Exception {
        this.properties = properties;
        Pair<PrivateKey, X509Certificate> keys = loadKeys();
        privateKey = keys.getValue0();
        certificate = keys.getValue1();
    }

    private Pair<PrivateKey, X509Certificate> loadKeys() throws Exception {
        final String path = properties.getKeystorePath();
        final String keystorePassword = properties.getKeystorePassword();
        final String alias = properties.getPrivateKeyAlias();
        final String keyPassword = properties.getPrivateKeyPassword();

        final File file = new File(path);
        LOGGER.info("Attempting to load private key and certificate from {}", file.getAbsolutePath());
        final KeyStore ks = KeyStore.getInstance("JKS", "SUN");
        try (FileInputStream inputStream = new FileInputStream(file)) {
            ks.load(inputStream, keystorePassword.toCharArray());
        }

        final Key key = ks.getKey(alias, keyPassword.toCharArray());
        if (key instanceof PrivateKey) {
            LOGGER.info("Private key successfully loaded");
        } else {
            throw new IllegalArgumentException("Private key not found in key store");
        }

        final Certificate certificate = ks.getCertificate(alias);
        if (certificate instanceof X509Certificate) {
            LOGGER.info("Certificate successfully loaded");
        } else {
            throw new IllegalArgumentException("Certificate not found in key store");
        }
        return Pair.with((PrivateKey) key, (X509Certificate) certificate);
    }

    @Override
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    @Override
    public X509Certificate getCertificate() {
        return certificate;
    }
}
