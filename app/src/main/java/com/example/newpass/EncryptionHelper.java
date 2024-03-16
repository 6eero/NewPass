package com.example.newpass;

import android.os.Build;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


public class EncryptionHelper {
    private static final String ALGORITHM = "AES";
    private static final String MODE = "AES/CBC/PKCS5Padding";
    private static final String IV = "abcdefghabcdefgh"; // IV must be 16 byte (AES/CBC)
    private static final String KEY = "0123456789abcdef"; // KEY must be 16 byte (AES)

    //TODO: Find a way to randomly generate the values of IV and KEY

    /**
     * Encrypts the given plaintext value using AES encryption with CBC mode and PKCS5 padding.
     *
     * @param value The plaintext value to encrypt.
     * @return The Base64-encoded ciphertext result of the encryption.
     * @throws Exception If an error occurs during the encryption process.
     */
    public static String encrypt(String value) throws Exception {

        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(MODE);

        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(IV.getBytes()));
        byte[] encryptedBytes = cipher.doFinal(value.getBytes());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } else {
            return null;
        }
    }

    /**
     * Decrypts the given Base64-encoded ciphertext value using AES decryption with CBC mode and PKCS5 padding.
     *
     * @param value The Base64-encoded ciphertext value to decrypt.
     * @return The decrypted plaintext result.
     * @throws Exception If an error occurs during the decryption process.
     */
    public static String decrypt(String value) throws Exception {

        byte[] encryptedBytes = new byte[0];

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encryptedBytes = Base64.getDecoder().decode(value);
        } else {
            return null;
        }

        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(MODE);

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(IV.getBytes()));
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
    }
}

