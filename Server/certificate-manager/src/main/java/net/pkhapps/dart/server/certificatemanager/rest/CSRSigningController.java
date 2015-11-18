package net.pkhapps.dart.server.certificatemanager.rest;

import net.pkhapps.dart.server.certificatemanager.domain.CSRSigningService;
import net.pkhapps.dart.server.certificatemanager.domain.CSRTokenService;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

@RestController
@RequestMapping("/rest/sign/{tokenHash}")
class CSRSigningController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSRSigningController.class);

    final CSRTokenService tokenService;
    final CSRSigningService signingService;

    @Autowired
    CSRSigningController(CSRTokenService tokenService, CSRSigningService signingService) {
        this.tokenService = tokenService;
        this.signingService = signingService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> sign(@PathVariable String tokenHash, @RequestBody String csrString) throws CSRTokenService.NoSuchTokenException,
            CSRTokenService.TokenExpiredException, InvalidCSRException {
        tokenService.validateToken(tokenHash);
        try (PEMParser pemParser = new PEMParser(new StringReader(csrString))) {
            Object pem = pemParser.readObject();
            if (pem instanceof PKCS10CertificationRequest) {
                final PKCS10CertificationRequest csr = (PKCS10CertificationRequest) pem;
                //final CMSSignedData certificate = signingService.sign(csr);
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                out.write("-----BEGIN PKCS #7 SIGNED DATA-----\n".getBytes("ISO-8859-1"));
                //out.write(Base64.encode(certificate.getEncoded()));
                out.write("\n-----END PKCS #7 SIGNED DATA-----\n".getBytes("ISO-8859-1"));
                out.close();
                return ResponseEntity.ok(new String(out.toByteArray(), "ISO-8859-1"));
            } else {
                throw new InvalidCSRException();
            }
        } catch (IOException ex) {
            LOGGER.error("Error while reading CSR or writing certificate", ex);
            throw new InvalidCSRException();
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The provided CSR was invalid")
    public static class InvalidCSRException extends Exception {
    }
}
