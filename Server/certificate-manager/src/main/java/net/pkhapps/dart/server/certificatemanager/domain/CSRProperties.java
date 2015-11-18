package net.pkhapps.dart.server.certificatemanager.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "certificate-manager.csr", ignoreUnknownFields = false)
public class CSRProperties {

    private Integer newCertificateValidityInDays = 365;

    public Integer getNewCertificateValidityInDays() {
        return newCertificateValidityInDays;
    }

    public void setNewCertificateValidityInDays(Integer newCertificateValidityInDays) {
        this.newCertificateValidityInDays = newCertificateValidityInDays;
    }
}
