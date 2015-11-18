package net.pkhapps.dart.server.certificatemanager.domain;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface CAService {

    PrivateKey getPrivateKey();

    X509Certificate getCertificate();
}
