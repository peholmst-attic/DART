package net.pkhapps.dart.server.certificatemanager.domain;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

public interface CSRSigningService {

    X509CertificateHolder sign(PKCS10CertificationRequest csr);
}
