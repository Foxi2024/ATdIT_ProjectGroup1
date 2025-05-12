package com.atdit.booking.backend.database;

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

public class Encrypter {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 256;

    private SecretKey deriveKey(String email, String password) throws EncryptionException {

        try{
            String salt = "1.FC Kaiserslautern";
            password = hashString(password);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, KEY_SIZE);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        }
        catch (Exception e) {
            throw new EncryptionException("Error deriving key", e);
        }
    }

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
            throw new EncryptionException("Error encrypting value", e);
        }

    }

    public String decrypt(String encrypted, String email, String password) throws Exception {


        SecretKey key = deriveKey(email, password);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        byte[] decoded = Base64.getDecoder().decode(encrypted);
        byte[] iv = Arrays.copyOfRange(decoded, 0, 16);
        byte[] data = Arrays.copyOfRange(decoded, 16, decoded.length);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        return new String(cipher.doFinal(data));


    }

    public String hashString(String stringToHash) throws HashingException {

        try{
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(stringToHash.getBytes());
            byte[] someByteArray = messageDigest.digest();
            return new BigInteger(1, someByteArray).toString(16);
        }
        catch (NoSuchAlgorithmException e) {
            throw new HashingException("Error hashing string", e);
        }


    }
}