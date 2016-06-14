package cbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Ettore Ciprian <cipettaro@gmail.com>
 */
public class AES {

    private int key_size;
    private SecretKey key;

    public AES() {
        this.key_size = 128; //default set to 128 bit
        this.key = null;
    }

    public int getKeySize() {
        return key_size;
    }

    public void setKeySize(int key_size) {
        this.key_size = key_size;
    }

    public SecretKey getKey() {
        return key;
    }

    public void setKey(SecretKey key) {
        this.key = key;
    }

    /**
     * Generate a random secure Key with the KeyGenerator library standards
     *
     * @param {int} size of the key
     * @return SecretKey
     */
    private SecretKey generateKey() {
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        keyGen.init(this.getKeySize());
        setKey(keyGen.generateKey());
        return getKey();
    }

    //Encrypt message given
    public String encrypt(String message, String key) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException {

        byte[] iv = new byte[16];//Initialization vector 
        System.out.println("Key: " + key);
        System.out.println("Plain Text: " + message);
        message= toHex(toByteArray(message));
        System.out.println("Plain Text (toHex): " + message);
        byte[] encodedKey = toByteArray(key);
        SecretKey originalKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        byte[] f_encrypted = null;
        byte[][] pt, encrypted = null;  //Store plain text in block of 16 bytes

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");//Standard AES encryption initialization
                    pt = padString(message, 16); //Store plain text in block of 16 bytes
                    encrypted = new byte[pt.length][16];
                    System.out.println("ENCRYPTION MODE: Chaining Block Cipher");
                    iv = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    cipher.init(Cipher.ENCRYPT_MODE, originalKey);

                    encrypted[0] = cipher.doFinal(xor(pt[0], iv));//First xor with IV parameter
                    for (int i = 1; i < pt.length; i++) {
                        encrypted[i] = cipher.doFinal(xor(pt[i], encrypted[i - 1]));//Encrypt block with xor of the previous
                    }

                    if ((message.getBytes().length % 16) != 0) {
                        byte[] temp = encrypted[pt.length - 2]; // Swap last two blocks
                        encrypted[pt.length - 2] = encrypted[pt.length - 1];
                        encrypted[pt.length - 1] = temp;
                        temp = flatten(encrypted);
                        f_encrypted = new byte[message.getBytes().length];
                        System.arraycopy(temp, 0, f_encrypted, 0, f_encrypted.length);//Truncate to original length of plain text

                    } else {
                        f_encrypted = flatten(encrypted);
                    }
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Cipher length: " + f_encrypted.length + " bytes");
        return toHex(f_encrypted);
    }

    //Decrypt a byte message
    public String decrypt(String mes, String key, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException {

        Cipher cipher = null;
        byte[] message = toByteArray(mes);
        byte[] f_decrypted = null;
        byte[][] ct, decrypted = null;
        byte[] encodedKey = toByteArray(key);
        SecretKey originalKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        System.out.println("Key: " + key);

        try {
            cipher = Cipher.getInstance("AES/ECB/NoPadding");
                    ct = padBytes(message, 16); //Store cipher text in block of 16 bytes
                    decrypted = new byte[ct.length][16];
                    iv = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    cipher.init(Cipher.DECRYPT_MODE, originalKey);
                    int n = (message.length % 16);
                    //CBC stealing - https://en.wikipedia.org/wiki/Ciphertext_stealing#CBC_ciphertext_stealing_decryption_using_a_standard_CBC_interface
                    if (n != 0) {
                        //Decrypt second-to-last cipher text block
                        byte temp[] = xor(cipher.doFinal(ct[ct.length - 2]), ct[ct.length - 1]);
                        //Pad last cipher text block with last n bytes of decrypted temp
                        while (n < 16) {
                            ct[ct.length - 1][n] = temp[n];
                            n++;
                        }
                        // Swap last two blocks
                        byte[] temp1 = ct[ct.length - 2];
                        ct[ct.length - 2] = ct[ct.length - 1];
                        ct[ct.length - 1] = temp1;
                    }

                    decrypted[0] = xor(cipher.doFinal(ct[0]), iv);//First xor with IV parameter
                    for (int i = 1; i < ct.length; i++) {
                        decrypted[i] = xor(cipher.doFinal(ct[i]), ct[i - 1]);
                    }

                    if ((message.length % 16) != 0) {
                        byte[] temp = flatten(decrypted);
                        f_decrypted = new byte[message.length];
                        System.arraycopy(temp, 0, f_decrypted, 0, f_decrypted.length);
                    } else {
                        f_decrypted = flatten(decrypted);
                    }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }

        String decryptedMessage = new String(f_decrypted, Charset.forName("UTF-8"));
        System.out.println();
        System.out.println("Cipher decrypted! Bytes of plain text: " + f_decrypted.length + "");
        System.out.println("decrypted cipher: " + decryptedMessage + "\n");
        return new String(f_decrypted, Charset.defaultCharset());
    }

    //Pad String message into byte array blocks.
    private byte[][] padString(String message, int block_size) {
        byte[] source = message.getBytes();
        int len = source.length % block_size;

        byte[][] ret = new byte[(int) Math.ceil(source.length / (double) block_size)][block_size];

        int start = 0;
        for (int i = 0; i < ret.length; i++) {
            ret[i] = Arrays.copyOfRange(source, start, start + block_size);
            start += block_size;
        }

        if (source.length % block_size != 0) {
            try {
                padWithLen(ret[ret.length - 1], len, block_size - len);
            } catch (ShortBufferException ex) {
                Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Number of blocks: " + ret.length);

        return ret;
    }

    // Pad cipher text in blocks
    private byte[][] padBytes(byte[] source, int block_size) {
        byte[][] ret = new byte[(int) Math.ceil(source.length / (double) block_size)][block_size];
        int len = source.length % block_size;
        int start = 0;
        for (int i = 0; i < ret.length; i++) {
            ret[i] = Arrays.copyOfRange(source, start, start + block_size);
            start += block_size;
        }

        if (source.length % block_size != 0) {
            try {
                padWithLen(ret[ret.length - 1], len, block_size - len);
            } catch (ShortBufferException ex) {
                Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Number of cipher blocks: " + ret.length);

        return ret;
    }

    //Flatten 2D arrays in a 1D array  
    private byte[] flatten(byte[][] arr) {
        List<Byte> list = new ArrayList<Byte>();
        for (byte[] arr1 : arr) {
            for (int j = 0; j < arr1.length; j++) {
                list.add(arr1[j]);
            }
        }

        byte[] vector = new byte[list.size()];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = list.get(i);
        }
        return vector;
    }

    //Xor function for two arrays of bytes  
    private byte[] xor(byte[] array_1, byte[] array_2) {
        byte[] result = new byte[array_1.length];

        int i = 0;
        for (byte b : array_1) {
            result[i] = (byte) (b ^ array_2[i++]);
        }
        return result;
    }

   
    public void padWithLen(byte[] in, int off, int len)
            throws ShortBufferException {
        if (in == null) {
            return;
        }

        if ((off + len) > in.length) {
            throw new ShortBufferException("Buffer too small to hold padding");
        }

        byte paddingOctet = (byte) (len & 0xff);
        for (int i = 0; i < len; i++) {
            //System.out.println(i + off + " = " + in[i + off]);
            in[i + off] = paddingOctet;
            //System.out.println(i + off + " = " + in[i + off]);
        }
    }

    // Print the random generated key to a file 
    public void printKeytoFile(String path) {

        byte[] encoded = generateKey().getEncoded();
        try (PrintWriter writer = new PrintWriter(path, "UTF-8")) {
            System.out.println(this.getKeySize() + " bit key: ");
            String encodedKey = toHex(encoded);
            System.out.println(encodedKey);
            writer.print(encodedKey);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Please provide a valid path where to save the key");
        }

    }

    //Load the AES key of specified size from a file     
    public SecretKey loadKeyfromFile(File keyfile) {
        byte[] keybyte = new byte[(int) keyfile.length()];
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(keyfile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            fin.read(keybyte);
        } catch (IOException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            System.out.println("File path: " + keyfile.getCanonicalPath() + "\nI have got this key: " + toHex(keybyte));
        } catch (IOException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new SecretKeySpec(keybyte, "AES");
    }

    /**
     * *
     * Encode to hex a byte array
     *
     * @param input- the byte array to be parsed
     * @return the resulting String
     */
    public static String toHex(byte[] input) {
        if (input == null || input.length == 0) {
            return "";
        }

        int inputLength = input.length;
        StringBuilder output = new StringBuilder(inputLength * 2);

        for (int i = 0; i < inputLength; i++) {
            int next = input[i] & 0xff;
            if (next < 0x10) {
                output.append("0");
            }

            output.append(Integer.toHexString(next));
        }

        return output.toString();
    }

    /**
     * Convert String to byte array, to be used when parsing input from the user
     *
     * @param s - The string to be parsed
     * @return
     */
    public static byte[] toByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len-1; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}
