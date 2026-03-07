package com.aptitudeapp.backend.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class PhoneUtil {

    @Value("${phone.secret}")
    private String secret;

    // Hash for searching
    public String hash(String phone) {
        return DigestUtils.sha256Hex(phone);
    }

    // Encrypt for storage
    public String encrypt(String phone) throws Exception {
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder()
                .encodeToString(cipher.doFinal(phone.getBytes()));
    }

    // Optional decrypt (rarely needed)
    public String decrypt(String encryptedPhone) throws Exception {
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(
                Base64.getDecoder().decode(encryptedPhone)
        ));
    }
    public String normalize(String phone) {

        phone = phone.replaceAll("[^0-9]", "");

        if (phone.startsWith("91") && phone.length() == 12) {
            phone = phone.substring(2);
        }

        return phone;
    }
}
