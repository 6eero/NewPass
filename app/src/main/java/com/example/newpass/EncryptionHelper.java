package com.example.newpass;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionHelper {

    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = generateRandomKey(16); // Chiave segreta per AES (16, 24, or 32 bytes)

    public static String encrypt(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        return Base64.getEncoder().encodeToString(encryptedByteValue);
    }

    public static String decrypt(String value) throws Exception {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.getDecoder().decode(value);
        byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
        return new String(decryptedByteValue, "utf-8");
    }

    private static Key generateKey() throws Exception {
        return new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
    }


    /**
     * Generates a secure random key of specified length for cryptographic purposes.
     *
     * @param length The length of the random key to generate, in bytes. It must be 16, 24, or 32 bytes
     * @return A string representing the generated random key, encoded in Base64, or null if the encoding is not supported.
     */
    public static String generateRandomKey(int length) {
        // Casual generation of 16 byte (128 bit)
        byte[] key = new byte[length];

        // Securely generate random sequences of bytes, making it suitable for generating cryptographic keys
        new SecureRandom().nextBytes(key);

        // Conversion to Base64 format for ease of use
        String base64Key = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            base64Key = Base64.getEncoder().encodeToString(key);
        }

        return base64Key;
    }
}
