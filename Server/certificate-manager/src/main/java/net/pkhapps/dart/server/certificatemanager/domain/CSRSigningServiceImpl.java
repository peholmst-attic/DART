package net.pkhapps.dart.server.certificatemanager.domain;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
class CSRSigningServiceImpl implements CSRSigningService {

    private final CAService caService;
    private final CSRProperties properties;
    private final int validityInDays;
    private final Clock clock;

    @Autowired
    CSRSigningServiceImpl(CAService caService, CSRProperties properties, Clock clock) throws Exception {
        this.caService = caService;
        this.properties = properties;
        this.clock = clock;
        validityInDays = properties.getNewCertificateValidityInDays();
    }

    @Override
    public X509CertificateHolder sign(PKCS10CertificationRequest csr) {
        try {
            final AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256withRSA");
            final AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
            final X500Name issuer = new X500Name(caService.getCertificate().getSubjectX500Principal().getName());
            final BigInteger serial = new BigInteger(32, SecureRandom.getInstanceStrong());
            final Instant from = clock.instant();
            final Instant to = from.plus(validityInDays, ChronoUnit.DAYS);

            // Create a certificate builder based on the CSR
            final X509v3CertificateBuilder certBldr = new X509v3CertificateBuilder(issuer, serial, Date.from(from),
                    Date.from(to), csr.getSubject(), csr.getSubjectPublicKeyInfo());
            certBldr.addExtension(Extension.basicConstraints, true,
                    new BasicConstraints(false));
            certBldr.addExtension(Extension.subjectKeyIdentifier, false,
                    new SubjectKeyIdentifier(csr.getSubjectPublicKeyInfo().getEncoded()));
            certBldr.addExtension(Extension.authorityKeyIdentifier, false,
                    new AuthorityKeyIdentifier(new GeneralNames(
                            new GeneralName(new X500Name(caService.getCertificate().getSubjectX500Principal().getName()))
                    ), caService.getCertificate().getSerialNumber()));

            // Sign the CSR
            ContentSigner signer = new BcRSAContentSignerBuilder(sigAlgId, digAlgId)
                    .build(PrivateKeyFactory.createKey(caService.getPrivateKey().getEncoded()));
            final X509CertificateHolder certHolder = certBldr.build(signer);
            return certHolder;
        } catch (Exception ex) {
            throw new RuntimeException("Could not sign CSR", ex);
        }
    }
}
