package com.atdit.booking.backend.database;

import com.atdit.booking.backend.exceptions.CryptographyException;
import com.atdit.booking.backend.exceptions.DecryptionException;
import com.atdit.booking.backend.exceptions.EncryptionException;
import com.atdit.booking.backend.exceptions.HashingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * A utility class that provides encryption, decryption and hashing functionality for secure data handling.
 * This implementation uses AES (Advanced Encryption Standard) with CBC (Cipher Block Chaining) mode
 * and PKCS5 padding for encryption/decryption operations.
 *
 * Security features:
 * - 256-bit AES encryption
 * - Secure key derivation using PBKDF2WithHmacSHA256
 * - Random IV (Initialization Vector) generation for each encryption
 * - SHA-256 hashing capability
 *
 * Note: This class is designed to be thread-safe and can be used across multiple threads.
 */
public class Encrypter {
    /**
     * The encryption algorithm specification string.
     * AES: Advanced Encryption Standard
     * CBC: Cipher Block Chaining mode
     * PKCS5Padding: Padding scheme used
     */
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * The key size in bits for the AES encryption.
     * 256-bit keys provide a very high level of security.
     */
    private static final int KEY_SIZE = 256;

    /**
     * Derives an encryption key from the user's email and password using PBKDF2 (Password-Based Key Derivation Function 2).
     * The method uses a fixed salt and 65536 iterations for key derivation to ensure consistent key generation.
     * The password is first hashed before being used in the key derivation process.
     *
     * @param email User's email address used as part of the key derivation input
     * @param password User's password that will be hashed and used for key derivation
     * @return SecretKey A 256-bit AES key for encryption/decryption operations
     * @throws CryptographyException If any error occurs during the key derivation process
     */
    private SecretKey deriveKey(String email, String password) throws CryptographyException {
        try{
            String salt = "1.FC Kaiserslautern";
            password = hashString(password);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, KEY_SIZE);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        }
        catch (Exception e) {
            throw new CryptographyException("Fehler bei der Schlüsselermittlung");
        }
    }

    /**
     * Encrypts a string value using AES encryption in CBC mode with PKCS5 padding.
     * The process includes:
     * 1. Generating a random 16-byte IV
     * 2. Deriving the encryption key from email and password
     * 3. Encrypting the data with the generated IV
     * 4. Combining the IV and encrypted data
     * 5. Encoding the result in Base64
     *
     * @param value The string to be encrypted
     * @param email User's email used for key derivation
     * @param password User's password used for key derivation
     * @return String A Base64 encoded string containing the IV and encrypted data
     * @throws EncryptionException If any error occurs during the encryption process
     */
    public String encrypt(String value, String email, String password) throws EncryptionException {
        try{
            SecretKey key = deriveKey(email, password);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] encrypted = cipher.doFinal(value.getBytes());
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
            return Base64.getEncoder().encodeToString(combined);
        }
        catch(Exception e){
            throw new EncryptionException("Fehler beim Verschlüsseln");
        }
    }

    /**
     * Decrypts an encrypted string using AES decryption in CBC mode with PKCS5 padding.
     * The process includes:
     * 1. Decoding the Base64 input
     * 2. Extracting the IV from the first 16 bytes
     * 3. Deriving the decryption key from email and password
     * 4. Decrypting the data using the extracted IV
     *
     * @param encrypted The Base64 encoded string containing IV and encrypted data
     * @param email User's email used for key derivation
     * @param password User's password used for key derivation
     * @return String The decrypted original string
     * @throws DecryptionException If any error occurs during the decryption process
     */
    public String decrypt(String encrypted, String email, String password) throws DecryptionException {
        try {
            SecretKey key = deriveKey(email, password);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] decoded = Base64.getDecoder().decode(encrypted);
            byte[] iv = Arrays.copyOfRange(decoded, 0, 16);
            byte[] data = Arrays.copyOfRange(decoded, 16, decoded.length);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            return new String(cipher.doFinal(data));
        } catch (Exception e) {
            throw new DecryptionException("Fehler beim Entschlüsseln");
        }
    }

    /**
     * Creates a SHA-256 (Secure Hash Algorithm 256-bit) hash of the input string.
     * The resulting hash is converted to a hexadecimal string representation.
     * This method is used internally for password hashing in the key derivation process
     * and can also be used independently for general string hashing.
     *
     * @param stringToHash The input string to be hashed
     * @return String A hexadecimal string representation of the SHA-256 hash
     * @throws HashingException If any error occurs during the hashing process
     */
    public String hashString(String stringToHash) throws HashingException {
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(stringToHash.getBytes());
            byte[] someByteArray = messageDigest.digest();
            return new BigInteger(1, someByteArray).toString(16);
        }
        catch (Exception e) {
            throw new HashingException("Error hashing string");
        }
    }
}