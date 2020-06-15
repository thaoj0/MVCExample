package Engine;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class FileCipher{
    private KeyGenerator keygenerator;
    private SecretKey cipherKey;
    private Cipher cipherer;
    public FileCipher(String aug){
        try{
            keygenerator = KeyGenerator.getInstance(aug);
            cipherKey = keygenerator.generateKey();
            cipherer = Cipher.getInstance(aug);
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public byte[] encrypt(String string){
        try{
            byte[] text = string.getBytes("UTF-8");
            cipherer.init(Cipher.ENCRYPT_MODE, cipherKey);
            return cipherer.doFinal(text);
        }catch(Exception e){
            System.out.println("encrypt: "+e);
        }
        return null;
    }
    public String decrypt(byte[] textEncrypted){
        try{
            cipherer.init(Cipher.DECRYPT_MODE, cipherKey);
            byte[] textDecrypted = cipherer.doFinal(textEncrypted);
            return new String(textDecrypted);
        }catch(Exception e){
            System.out.println("decrypt: "+e);
        }
        return null;
    }
    public static void main(String[] args) {
        FileCipher fc = new FileCipher("superencrytionkey");
        String text = "original text1!";
        System.out.println("Original Text:"+text);

        byte[] encryptedText = fc.encrypt(text);
        System.out.println("Encrypted Text:"+encryptedText);

        String decryptedText = fc.decrypt(encryptedText);
        System.out.println("Decrypted Text:"+decryptedText);
    }
}