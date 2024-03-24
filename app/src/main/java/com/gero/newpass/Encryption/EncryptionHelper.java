package com.gero.newpass.Encryption;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.security.KeyStore;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


public class EncryptionHelper {
    private static final String MODE = "AES/CBC/PKCS7Padding";
    private static final String KEY_ALIAS = "MyAesKey";

    /**
     * Encrypts the given plaintext using AES encryption with CBC mode.
     *
     * @param plainText The plaintext string to be encrypted.
     * @return A Base64-encoded string containing the encrypted data concatenated with the initialization vector (IV),
     *         or null if an error occurs during encryption.
     */
    public static String encrypt(String plainText) {

        try {
            // Get AES key
            SecretKey secretKey = getOrCreateAESKey();

            if (secretKey == null) {
                Log.e("4950734", "Failed to retrieve the AES key");
                return null;
            }

            Cipher cipher = Cipher.getInstance(MODE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] iv = cipher.getIV();

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

            byte[] ivAndEncryptedBytes = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, ivAndEncryptedBytes, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, ivAndEncryptedBytes, iv.length, encryptedBytes.length);

            Log.i("4950734", "---------------- Encryption ----------------");
            Log.i("4950734","plain  text:   " + plainText);
            Log.i("4950734","KEY:           " + secretKey);
            Log.i("4950734","IV:            " + Arrays.toString(Arrays.copyOfRange(ivAndEncryptedBytes, 0, 16)));
            Log.i("4950734","cipher text:   " + Base64.encodeToString(ivAndEncryptedBytes, Base64.DEFAULT));

            return Base64.encodeToString(ivAndEncryptedBytes, Base64.DEFAULT);

        } catch (Exception e) {
            Log.e("4950734", "Error during encryption: " + e.getMessage());
            return null;
        }
    }

    /**
     * Decrypts an encrypted string using the AES algorithm with the key obtained from the keystore.
     *
     * @param cipherText It must be in the form IV + encrypted_text. It will be decrypted with the IV
     * @return The decrypted string, or null if an error occurs during the decryption process.
     */
    public static String decrypt(String cipherText) {
        try {

            SecretKey secretKey = getOrCreateAESKey();

            if (secretKey == null) {
                Log.e("4950734", "Failed to retrieve the AES key");
                return null;
            }

            Log.i("4950734", "Decryption key obtained successfully: " + secretKey);

            byte[] ivAndEncryptedBytes = Base64.decode(cipherText, Base64.DEFAULT);

            byte[] iv = Arrays.copyOfRange(ivAndEncryptedBytes, 0, 16);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(MODE);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            byte[] decryptedBytes = cipher.doFinal(ivAndEncryptedBytes);


            String plainText = new String(Arrays.copyOfRange(decryptedBytes, 16, decryptedBytes.length));

            Log.i("4950734", "---------------- Decryption ----------------");
            Log.i("4950734","cipher text:   " + cipherText);
            Log.i("4950734","KEY:           " + secretKey);
            Log.i("4950734","IV:            " + Arrays.toString(iv));
            Log.i("4950734","plain text:    " + plainText);

            return plainText;

        } catch (Exception e) {
            Log.e("4950734", "Error during decryption: " + e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves or generates an AES key from the keystore.
     *
     * @return The AES key retrieved from the keystore, or a newly generated key if it doesn't exist in the keystore.
     *         Returns null if an error occurs during the retrieval or generation process.
     */
    private static SecretKey getOrCreateAESKey() {
        try {

            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            if (keyStore.containsAlias(KEY_ALIAS)) {

                KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null);

                Log.w("4950734", "Key obtained successfully form key store:");

                return secretKeyEntry.getSecretKey();
            } else {

                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .setKeySize(128);
                keyGenerator.init(builder.build());

                Log.w("4950734", "The key doesn't exist, I'll create a new one");

                return keyGenerator.generateKey();
            }
        } catch (Exception e) {
            Log.e("4950734", "Error getting/creating AES key: " + e.getMessage());
            return null;
        }
    }
}

