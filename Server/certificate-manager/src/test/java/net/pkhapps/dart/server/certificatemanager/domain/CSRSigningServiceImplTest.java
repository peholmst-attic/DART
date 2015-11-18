package net.pkhapps.dart.server.certificatemanager.domain;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringReader;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

public class CSRSigningServiceImplTest {

    private static final String CSR = "-----BEGIN NEW CERTIFICATE REQUEST-----\n" +
            "MIIC8TCCAdkCAQAwfDELMAkGA1UEBhMCRkkxGDAWBgNVBAgTD1ZhcnNpbmFpcy1TdW9taTERMA8G\n" +
            "A1UEBxMIUGFyYWluZW4xEzARBgNVBAoTClBhcmdhcyBGQksxEDAOBgNVBAsTB1Vua25vd24xGTAX\n" +
            "BgNVBAMTEFBldHRlciBIb2xtc3Ryb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCJ\n" +
            "vgEdrGTZ0X81vnfAMeDerl/WPm46cEYsqgUj7wjoHoZe9Q85L/Uz/p+mmzc50gbiMTqmZvODLFYy\n" +
            "tv1yeQJ1IoY8GCXzdFbPENWRgC0fPBwCy1L7AYJORQW5938xmC5tIusPoXFZMRPxbE2FzVPufhiE\n" +
            "ucDZyw2qh5pBAFJD/pRva0HfMBM5HbKgp71FW6x3GczTJQZFgv2v9j3Wr7Smyv4SaotTj1TKvxRl\n" +
            "YYpvrfjI0e2A3Iy6vQq1yWKQ6en2AoGBBr5WY0upAc1LG0i2TunS6TmMbNE2dJxotFa8aZC46IAf\n" +
            "iYEb5KiU8JzC+PaO+Jym1XkZ49RWghsMXU3xAgMBAAGgMDAuBgkqhkiG9w0BCQ4xITAfMB0GA1Ud\n" +
            "DgQWBBR0ksRG5IPpQWTWZ5NT7GXXN5TWpjANBgkqhkiG9w0BAQsFAAOCAQEAKOQSwqNpWcI6jkm9\n" +
            "OEyFkS2vAnln7CODHOk9wsfxmlWtHsNUKN1B43NQlombNgJ72Rrm0YcCuqCfh6+rZ/bb0Z6TyBUB\n" +
            "zxMkkQEfamHPvYA1YiQagbVWyGW5BPCvPclrbimS8QlhIcUzNfT2cOZ3Hv8EquNlcmzip7qCRNNt\n" +
            "vph3k3wkr8r/8ycpZBf8LimrHJMH+mvGV//MjLtKe+8cWFH1TJhk2/OcyDz25fn+Murdb/PNz/2P\n" +
            "IS2xm0qA1ASXU7aAOplN0XM1Ov997nY3l0PxL9uZbYHeXgWWrphhjO8bm6eW/MgEpkCm42VFLwPJ\n" +
            "746euTb4G66WTceDDX8z1A==\n" +
            "-----END NEW CERTIFICATE REQUEST-----";

    private static final String CA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIIEowIBAAKCAQEAyYy9Bcm/24wq04dkD1NY0OG9VELg4Ta41kBb10TwQ+JH8ETh\n" +
            "NBpkJKqbs6o88Qp3RZTXhkPBbTO5vpGb9b4hLj5oMSDAogUxY0vNx6HE7yB+cHaj\n" +
            "1V0daE/tB14tkU6MAAbQsjLlWkTHU/R9W1+DOTITTtT4qh0w5hp+31Dlma8IHG3Y\n" +
            "zaft17Ge8YeEaBdyMQwASOXUflgKUZa2Va0XdGkQ/fj3EbnvU1Se7Kv9V9y1CiHe\n" +
            "U9iJjwnZ++KfiOYSgNKP8jKm1kLC0Si/vGTCjFr1RTXltv9FePbDGMcCAZBo/tvz\n" +
            "812smfkL4rMac/Jv3tNmUZDyPCxmVoIEXdK+1QIDAQABAoIBABpJxQ3QNroIGj3k\n" +
            "1uQ41sjH6M3ouzQk0w7+7fux/jScew2XrlgBaTZvGkfg2HoIB26H72SPydbYfE2i\n" +
            "6FixJ9rEQQrHXtjDHKH67SgwrWMTNeqtTskxj/hfFGd1nTPqrtACE45qnQedGPKW\n" +
            "XRNh7dez9Fi/5n8pt4sLwaiXAJt+waVNyY4D2L1CwOr3iF2Hq95nSMjc1jAdMVPI\n" +
            "/8e00AQi/S3AFKMrPJVIe7TQlZ0zsa6x49kGoyhS8SDY61RToMbVZOPrZwfIVtNK\n" +
            "Civ0Bpt6rkhVeZL9eGf/UqkGw3PQXXqluXibJ7H/dz7wUDuzTv72tRvqcODMFsSn\n" +
            "FNEY1wECgYEA6VSrwVwGiwhfWVHPm/cj0dQhirmKqZ432A243Kq9vdPLOnRPLE3E\n" +
            "kgAr8wqlVkzffwwHi6pDprCGSc78TaXsWqtAcR+9WRRf0q6xeJbh00YJLEmG/ow2\n" +
            "YkvPksSZXqUxopTnkggaNZT/Zx/ARU2gXI9kwgyk9cRUNYBhTK5ilXECgYEA3SGe\n" +
            "6ALmELe8tCRvz9oCjwzrM8njCtyrl/DmP7YO3UCw9zgqYSEpe39Uudwlueq5rjG/\n" +
            "NAcQ/MacrSv/81TuTft7Aibnf7xlE3wqcjZ015H9owm01echMZj507evPbK/SlY2\n" +
            "YrARBceLwVH+9OUL6kXMMt2NkI505PwMEaU0vaUCgYEAsxJpcs9msiNt0gYImIFW\n" +
            "uz3btDTrp3unDiiR3MEpN9uo2CbVcIIKv6ZkFHXyKEZ9LGsWC2lT3EUT+udhGVKj\n" +
            "D37oySj7z7bkR7QMvijGDbhtZ9DSSt9fCZd3KgO6VTng2w7+nkOboc3FKebjUAzg\n" +
            "askeIhNEMt5HZnwtsIURYfECgYBIflYm/5RG1MVzVSKmxHTkSrrsznm1POSwOhRx\n" +
            "vJCg7i1Vzr7t+r4rs3GmQMrJaW8X8U0alFp4U/BXQT52mlhJjnEkInbwRGXqfmE3\n" +
            "8ixZ442AHpV/LneOV/VG+Efl9SyLBTptHgVmnKVvah8oT9KZKeqonkNjqaAe1ZZr\n" +
            "Tnq81QKBgCPUQ/37x7oD4u9cEYjuuF1RcPPEehYciAW0ZkSMEY2XNx1VAuXtIBLf\n" +
            "e94LHBYFXY6uLlb4WONj6AKQlys2Wo84l50VrBGymhBwkMzBb5HuDHJg7qYZ82+U\n" +
            "EBZSm9CGtUtttlkgMkbVhf6Us2IU7Qr+r7528ANdHwvlnaWk4ZqB\n" +
            "-----END RSA PRIVATE KEY-----";

    private static final String CA_CERTIFICATE = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDZjCCAk4CCQDjSOIoif2MjTANBgkqhkiG9w0BAQUFADB1MQswCQYDVQQGEwJG\n" +
            "STEYMBYGA1UECAwPVmFyc2luYWlzLVN1b21pMQ8wDQYDVQQHDAZQYXJnYXMxGzAZ\n" +
            "BgNVBAoMEkY6bWEgUEtIIFNvbHV0aW9uczEeMBwGA1UEAwwVREFSVCBSb290IENl\n" +
            "cnRpZmljYXRlMB4XDTE1MTExNDA2MTUyN1oXDTE2MTExMzA2MTUyN1owdTELMAkG\n" +
            "A1UEBhMCRkkxGDAWBgNVBAgMD1ZhcnNpbmFpcy1TdW9taTEPMA0GA1UEBwwGUGFy\n" +
            "Z2FzMRswGQYDVQQKDBJGOm1hIFBLSCBTb2x1dGlvbnMxHjAcBgNVBAMMFURBUlQg\n" +
            "Um9vdCBDZXJ0aWZpY2F0ZTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB\n" +
            "AMmMvQXJv9uMKtOHZA9TWNDhvVRC4OE2uNZAW9dE8EPiR/BE4TQaZCSqm7OqPPEK\n" +
            "d0WU14ZDwW0zub6Rm/W+IS4+aDEgwKIFMWNLzcehxO8gfnB2o9VdHWhP7QdeLZFO\n" +
            "jAAG0LIy5VpEx1P0fVtfgzkyE07U+KodMOYaft9Q5ZmvCBxt2M2n7dexnvGHhGgX\n" +
            "cjEMAEjl1H5YClGWtlWtF3RpEP349xG571NUnuyr/VfctQoh3lPYiY8J2fvin4jm\n" +
            "EoDSj/IyptZCwtEov7xkwoxa9UU15bb/RXj2wxjHAgGQaP7b8/NdrJn5C+KzGnPy\n" +
            "b97TZlGQ8jwsZlaCBF3SvtUCAwEAATANBgkqhkiG9w0BAQUFAAOCAQEAS73yayyM\n" +
            "aQprsC2bWI1pJt/f6Kn2jsIuGeRyS2AmF90EXasaDInKKyNvGlB3ObcF35xX5G2n\n" +
            "kqRVcwJXiROO8F83PMiVf5ZBTCI175Bts7WEaXmAL2TN0OC4m2mW5vytYqpgveAx\n" +
            "M1pirKhElxCRlVOm9Ay2RhRMO5HbzwnN/NYkHSgpaDvX4IKqgtsp4JC1NMARjmXL\n" +
            "Fux0Fe+acmSWlq2IPUHWcet6Fmen0tRC4wk2oBNbD6L7tSkS2vw1xg6F61VyaTM3\n" +
            "coquxrH3XkzFnTaceMUU9jhl4egqeQLOPUdg81X12vPjnPRzbVbOm2N3oTB0RSpw\n" +
            "xKckNnCTnQ5dlg==\n" +
            "-----END CERTIFICATE-----";

    private CSRSigningServiceImpl signingService;

    private ZonedDateTime testToday = ZonedDateTime.of(2015, 11, 18, 23, 25, 00, 00, ZoneId.systemDefault());
    private PrivateKey caPrivateKey;
    private X509Certificate caCertificate;

    @Before
    public void setUp() throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        caPrivateKey = readPrivateKey(CA_PRIVATE_KEY);
        caCertificate = readCertificate(CA_CERTIFICATE);

        CAService caService = Mockito.mock(CAService.class);
        Mockito.when(caService.getCertificate()).thenReturn(caCertificate);
        Mockito.when(caService.getPrivateKey()).thenReturn(caPrivateKey);

        Clock clock = Mockito.mock(Clock.class);
        Mockito.when(clock.instant()).thenReturn(testToday.toInstant());

        CSRProperties properties = new CSRProperties();
        signingService = new CSRSigningServiceImpl(caService, properties, clock);
    }

    @Test
    public void sign_validCSR_validCertificateIsReturned() throws Exception {
        X509CertificateHolder certificate = signingService.sign(readCSR(CSR));
        assertEquals(caCertificate.getSubjectX500Principal().getName(), certificate.getIssuer().toString());
        assertEquals(testToday.toInstant(), certificate.toASN1Structure().getStartDate().getDate().toInstant());
        assertEquals(testToday.plusDays(365).toInstant(), certificate.toASN1Structure().getEndDate().getDate().toInstant());

        final X509Certificate jcaCert = new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getCertificate(certificate);
        jcaCert.verify(caCertificate.getPublicKey());
    }

    private PrivateKey readPrivateKey(String pemKey) throws Exception {
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);
        PEMKeyPair keyPair = (PEMKeyPair) readPemString(pemKey);
        return converter.getKeyPair(keyPair).getPrivate();
    }

    private X509Certificate readCertificate(String pemCertificate) throws Exception {
        JcaX509CertificateConverter converter = new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);
        X509CertificateHolder certificateHolder = (X509CertificateHolder) readPemString(pemCertificate);
        return converter.getCertificate(certificateHolder);
    }

    private PKCS10CertificationRequest readCSR(String pemCSR) throws Exception {
        return (PKCS10CertificationRequest) readPemString(pemCSR);
    }

    private Object readPemString(String s) throws IOException {
        try (PEMParser pemParser = new PEMParser(new StringReader(s))) {
            return pemParser.readObject();
        }
    }
}
