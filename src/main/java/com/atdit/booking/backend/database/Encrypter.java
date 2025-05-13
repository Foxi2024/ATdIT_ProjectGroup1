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
 * Class for handling encryption, decryption and hashing operations.
 * Uses AES encryption with CBC mode and PKCS5 padding.
 */
public class Encrypter {
    /** Algorithm used for encryption/decryption */
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    /** Key size in bits for the encryption */
    private static final int KEY_SIZE = 256;

    /**
     * Derives an encryption key from email and password using PBKDF2.
     *
     * @param email User's email address
     * @param password User's password
     * @return SecretKey for encryption/decryption
     * @throws CryptographyException if key derivation fails
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
     * Encrypts a string value using AES encryption.
     * Uses a randomly generated IV for each encryption.
     *
     * @param value String to encrypt
     * @param email User's email for key derivation
     * @param password User's password for key derivation
     * @return Base64 encoded encrypted string with IV prepended
     * @throws EncryptionException if encryption fails
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
     * Decrypts an encrypted string using AES decryption.
     * Extracts the IV from the first 16 bytes of the decoded string.
     *
     * @param encrypted Base64 encoded encrypted string with IV
     * @param email User's email for key derivation
     * @param password User's password for key derivation
     * @return Decrypted string
     * @throws DecryptionException if decryption fails
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
     * Creates a SHA-256 hash of the input string.
     * The hash is returned as a hexadecimal string.
     *
     * @param stringToHash String to be hashed
     * @return Hexadecimal string representation of the hash
     * @throws HashingException if hashing fails
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