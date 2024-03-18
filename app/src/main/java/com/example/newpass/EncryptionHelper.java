package com.example.newpass;

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
     * @throws Exception If an error occurs during the encryption process.
     */
    public static String encrypt(String plainText) throws Exception {

        try {
            // Get AES key
            SecretKey secretKey = getOrCreateAESKey();

            if (secretKey == null) {
                Log.e("4950734", "Errore nell'ottenere la chiave AES");
                return null;
            }

            // Initialize the AES cipher with CBC mode and PKCS7Padding
            Cipher cipher = Cipher.getInstance(MODE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Generate a random IV
            byte[] iv = cipher.getIV();

            // Encrypt the string
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

            // Concatenate IV and encrypted data
            byte[] ivAndEncryptedBytes = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, ivAndEncryptedBytes, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, ivAndEncryptedBytes, iv.length, encryptedBytes.length);

            Log.i("4950734", "---------------- CIFRATURA ----------------");
            Log.i("4950734","plain  text:   " + plainText);
            Log.i("4950734","KEY:           " + secretKey);
            Log.i("4950734","IV:            " + Arrays.toString(Arrays.copyOfRange(ivAndEncryptedBytes, 0, 16)));
            Log.i("4950734","cipher text:   " + Base64.encodeToString(ivAndEncryptedBytes, Base64.DEFAULT));

            // Encode the result in Base64
            return Base64.encodeToString(ivAndEncryptedBytes, Base64.DEFAULT);

        } catch (Exception e) {
            Log.e("4950734", "Errore durante la cifratura: " + e.getMessage());
            return null;
        }
    }

    /**
     * Decrypts an encrypted string using the AES algorithm with the key obtained from the keystore.
     *
     * @param cipherText It must be in the form IV + encrypted_text. It will be decrypted with the IV
     * @return The decrypted string, or null if an error occurs during the decryption process.
     * @throws Exception If an error occurs during the decryption process.
     */
    public static String decrypt(String cipherText) throws Exception {
        try {
            // Get AES key
            SecretKey secretKey = getOrCreateAESKey();

            if (secretKey == null) {
                Log.e("4950734", "Errore nell'ottenere la chiave AES");
                return null;
            }

            Log.i("4950734", "Chiave ottenuta per la decifratura: " + secretKey);

            // Decode the Base64-encrypted string
            byte[] ivAndEncryptedBytes = Base64.decode(cipherText, Base64.DEFAULT);

            // Get the IV vector used for encryption from the first 16 bytes of cipher text
            byte[] iv = Arrays.copyOfRange(ivAndEncryptedBytes, 0, 16);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // Initialize the AES cipher with CBC mode
            Cipher cipher = Cipher.getInstance(MODE);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

            // Decrypt the string
            byte[] decryptedBytes = cipher.doFinal(ivAndEncryptedBytes);

            // Separate IV and plaintext and save plaintext in a variable
            String plainText = new String(Arrays.copyOfRange(decryptedBytes, 16, decryptedBytes.length));

            Log.i("4950734", "---------------- DECIFRATURA ----------------");
            Log.i("4950734","cipher text:   " + cipherText);
            Log.i("4950734","KEY:           " + secretKey);
            Log.i("4950734","IV:            " + Arrays.toString(iv));
            Log.i("4950734","plain text:    " + plainText);

            // returns the decrypted string
            return plainText;

        } catch (Exception e) {
            Log.e("4950734", "Errore durante la decifratura: " + e.getMessage());
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
            // Load the KeyStore
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            // Check if an AES key already exists in the KeyStore
            if (keyStore.containsAlias(KEY_ALIAS)) {
                // Retrieve the existing AES key from the KeyStore
                KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null);

                Log.w("4950734", "Chiave ottenuta dal Key Stone");

                return secretKeyEntry.getSecretKey();
            } else {
                // If it doesn't exist, generate a new AES key and store it in the KeyStore
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .setKeySize(128);
                keyGenerator.init(builder.build());

                Log.w("4950734", "La chiave non esiste ne creo una nuova");

                return keyGenerator.generateKey();
            }
        } catch (Exception e) {
            Log.e("4950734", "Errore durante il recupero/creazione della chiave AES: " + e.getMessage());
            return null;
        }
    }
}

