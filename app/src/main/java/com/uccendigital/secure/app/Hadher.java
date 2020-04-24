package com.uccendigital.secure.app;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.security.SecureRandom;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Hadher {
    Context context;
    private SharedManager hadShared;

    public Hadher(Context context) {
        this.context = context;
        hadShared = new SharedManager(context, "hadher");
    }

    public void sousPoints(int points, long last) {
        int oldPoints = extractPoints();
        int idrimen = 0;
        if (points > oldPoints) {
            idrimen = oldPoints;
        } else idrimen = oldPoints - points;
        new SharedManager(context, "app").putLong("last",last);
        putIffer("idrimen", String.valueOf(idrimen + 34));
    }

    public void addPoints(int points) {
        int idrimen = extractPoints() + points;
        putIffer("idrimen", String.valueOf(idrimen + 34));
    }

    public void putPoints(int points) {
        putIffer("idrimen", String.valueOf(points + 34));
    }

    public int extractPoints() {
        String adrim = extractIffer("idrimen");

        if (!adrim.equals("")) {
            return Integer.parseInt(adrim) - 34;
        } else {
            return 0;
        }

    }

    public void putIffer (String iffer, String value) {
        encryption(iffer, value);
    }

    public String extractIffer (String string) {
        String iffer = decryption(string);
        return iffer;
    }

    public boolean checkIffer (String string) {

        String tasaruts = hadShared.getStr(string + "Tas");
        String tazwara = hadShared.getStr(string + "Taz");
        String iffer = hadShared.getStr(string);

        if (iffer.equals("") || tasaruts.equals("") || tazwara.equals("")) {
            return false;
        } else {
            return true;
        }

    }

    private void encryption(String string, String iffer) {
        //Encryption test
        byte[] bytes = iffer.getBytes();
        HashMap<String, byte[]> map = encryptBytes(bytes, "UserSuppliedPassword");

        byte salt[] = map.get("salt");
        byte iv[] = map.get("iv");
        byte encrypted[] = map.get("encrypted");

        String salt1 = Base64.encodeToString(salt, Base64.NO_WRAP);
        String iv1 = Base64.encodeToString(iv, Base64.NO_WRAP);
        String encrypted1 = Base64.encodeToString(encrypted, Base64.NO_WRAP);

        hadShared.putStr(string + "Tas", salt1);
        hadShared.putStr(string + "Taz", iv1);
        hadShared.putStr(string, encrypted1);

    }

    private String decryption (String string) {

        //Decryption test

        String Tas = hadShared.getStr(string + "Tas");
        String Taz = hadShared.getStr(string + "Taz");
        String a = hadShared.getStr(string);

        if (Tas.equals("") || Taz.equals("") || a.equals("")) {
            return "";
        }

        byte[] salt = Base64.decode(Tas, Base64.NO_WRAP);
        byte[] iv = Base64.decode(Taz, Base64.NO_WRAP);
        byte[] encrypted = Base64.decode(a, Base64.NO_WRAP);

        HashMap<String, byte[]> map = new HashMap<String, byte[]>();
        map.put("salt", salt);
        map.put("iv", iv);
        map.put("encrypted", encrypted);

        byte[] decrypted = decryptData(map, "UserSuppliedPassword");
        String decryptedString = null;
        if (decrypted != null)
        {
            decryptedString = new String(decrypted);
        }

        return decryptedString;

    }

    private HashMap<String, byte[]> encryptBytes(byte[] plainTextBytes, String passwordString){
        HashMap<String, byte[]> map = new HashMap<String, byte[]>();

        try
        {
            //Random salt for next step
            SecureRandom random = new SecureRandom();
            byte salt[] = new byte[256];
            random.nextBytes(salt);

            //PBKDF2 - derive the key from the password, don't use passwords directly
            char[] passwordChar = passwordString.toCharArray(); //Turn password into char[] array
            PBEKeySpec pbKeySpec = new PBEKeySpec(passwordChar, salt, 1324, 256); //1324 iterations
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = secretKeyFactory.generateSecret(pbKeySpec).getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            //Create initialization vector for AES
            SecureRandom ivRandom = new SecureRandom(); //not caching previous seeded instance of SecureRandom
            byte[] iv = new byte[16];
            ivRandom.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            //Encrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(plainTextBytes);

            map.put("salt", salt);
            map.put("iv", iv);
            map.put("encrypted", encrypted);
        }
        catch(Exception e)
        {
            Log.e("MYAPP", "encryption exception", e);
        }

        return map;
    }

    private byte[] decryptData(HashMap<String, byte[]> map, String passwordString){
        byte[] decrypted = null;
        try
        {
            byte salt[] = map.get("salt");
            byte iv[] = map.get("iv");
            byte encrypted[] = map.get("encrypted");

            //regenerate key from password
            char[] passwordChar = passwordString.toCharArray();
            PBEKeySpec pbKeySpec = new PBEKeySpec(passwordChar, salt, 1324, 256);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = secretKeyFactory.generateSecret(pbKeySpec).getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            //Decrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            decrypted = cipher.doFinal(encrypted);
        }
        catch(Exception e)
        {
            Log.e("MYAPP", "decryption exception", e);
        }

        return decrypted;
    }
}
