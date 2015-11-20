package net.pkhapps.dart.server.certificatemanager.rest;

import net.pkhapps.dart.server.certificatemanager.domain.CAService;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.cert.CertificateEncodingException;

@RestController
@RequestMapping("/rest/ca")
class CAController {

    final CAService caService;

    @Autowired
    CAController(CAService caService) {
        this.caService = caService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> get() throws CertificateEncodingException {
        final StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN CERTIFICATE-----\n");
        sb.append(Base64.toBase64String(caService.getCertificate().getEncoded()));
        sb.append("\n-----END CERTIFICATE-----\n");
        return ResponseEntity.ok(sb.toString());
    }
}
