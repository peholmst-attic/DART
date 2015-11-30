package net.pkhapps.dart.mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.spongycastle.asn1.x500.X500Name;
import org.spongycastle.asn1.x500.X500NameBuilder;
import org.spongycastle.asn1.x500.style.BCStyle;
import org.spongycastle.asn1.x509.AlgorithmIdentifier;
import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.KeyGenerationParameters;
import org.spongycastle.crypto.generators.RSAKeyPairGenerator;
import org.spongycastle.crypto.params.RSAKeyGenerationParameters;
import org.spongycastle.operator.ContentSigner;
import org.spongycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.spongycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.spongycastle.operator.OperatorCreationException;
import org.spongycastle.operator.bc.BcRSAContentSignerBuilder;
import org.spongycastle.operator.jcajce.JcaContentSignerBuilder;
import org.spongycastle.pkcs.PKCS10CertificationRequest;
import org.spongycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.spongycastle.pkcs.bc.BcPKCS10CertificationRequestBuilder;
import org.spongycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import javax.security.auth.x500.X500Principal;

public class CertificateActivity extends AppCompatActivity {

    public static final int READ_QR_CODE_REQUEST_CODE = 100;
    private String org;
    private String orgUnit;
    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        org = sharedPreferences.getString(PreferenceKeys.KEY_PREF_IDENTITY_ORG, "");
        orgUnit = sharedPreferences.getString(PreferenceKeys.KEY_PREF_IDENTITY_ORG_UNIT, "");
        deviceId = sharedPreferences.getString(PreferenceKeys.KEY_PREF_IDENTITY_DEVICE_ID, "");

        setContentView(R.layout.activity_certificate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button readQRCodeButton = (Button) findViewById(R.id.readQRCodeButton);
        readQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readQRCode();
            }
        });

        Button signCertificateButton = (Button) findViewById(R.id.signCertificateButton);
        signCertificateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signCSR();
            }
        });
        if (org.isEmpty() || orgUnit.isEmpty() || deviceId.isEmpty()) {
            // TODO Show a message
            signCertificateButton.setEnabled(false);
        }
    }

    private void readQRCode() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, READ_QR_CODE_REQUEST_CODE);
        } catch (Exception ex) {
            // Barcode reader not installed, redirect user to market to install it
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);
        }
    }

    private void signCSR() {

    }

    private PKCS10CertificationRequest createCSR() throws IOException, OperatorCreationException {
        final AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256withRSA");
        final AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
        X500Name subject = new X500NameBuilder()
                .addRDN(BCStyle.O, org)
                .addRDN(BCStyle.OU, orgUnit)
                .addRDN(BCStyle.CN, deviceId)
                .build();

        AsymmetricCipherKeyPair pair = generateKeyPair();

        PKCS10CertificationRequestBuilder p10Builder = new BcPKCS10CertificationRequestBuilder(subject, pair.getPublic());
        BcRSAContentSignerBuilder csBuilder = new BcRSAContentSignerBuilder(sigAlgId, digAlgId);
        ContentSigner signer = csBuilder.build(pair.getPrivate());
        PKCS10CertificationRequest csr = p10Builder.build(signer);
        return csr;
    }

    private AsymmetricCipherKeyPair generateKeyPair() {
        RSAKeyPairGenerator generator = new RSAKeyPairGenerator();
        generator.init(new KeyGenerationParameters(new SecureRandom(), 2048));
        return generator.generateKeyPair();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_QR_CODE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String code = data.getStringExtra("SCAN_RESULT");
                ((EditText) findViewById(R.id.csrToken)).setText(code);
            }
        }
    }
}
