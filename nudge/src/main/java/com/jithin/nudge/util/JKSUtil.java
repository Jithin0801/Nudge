package com.jithin.nudge.util;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import org.springframework.stereotype.Component;
import com.jithin.nudge.config.SecurityProperties;
import lombok.Data;

@Data
@Component
public class JKSUtil {

    private String keystorePath;
    private String keystorePassword;
    private String keyAlias;
    private String keyPassword;
    private KeyStore keyStore;
    private Key key;

    public JKSUtil(SecurityProperties securityProperties) throws Exception {
        this.keystorePath = securityProperties.keystorePath();
        this.keystorePassword = securityProperties.keystorePassword();
        this.keyAlias = securityProperties.keyAlias();
        this.keyPassword = securityProperties.keyPassword();

        try {
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(new FileInputStream(keystorePath), keystorePassword.toCharArray());
            this.setKeyStore(keystore);
            Key key = keystore.getKey(keyAlias, keyPassword.toCharArray());
            this.setKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public PrivateKey loadPrivateKey() throws Exception {
        try {
            Key key = this.getKeyStore().getKey(keyAlias, keyPassword.toCharArray());
            PrivateKey privateKey = (PrivateKey) key;
            return privateKey;
        } catch (Exception e) {
            throw e;
        }
    }

    public PublicKey getPublicKey() throws Exception {
        try {
            Certificate cert = this.getKeyStore().getCertificate(keyAlias);
            PublicKey publicKey = cert.getPublicKey();
            return publicKey;
        } catch (Exception e) {
            throw e;
        }
    }
}
