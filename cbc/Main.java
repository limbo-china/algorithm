package cbc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Ettore Ciprian <cipettaro@gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AES aes = new AES();
        System.out.println("enter --generate --encrypt --decrypt");
        switch (args[0]) {

            case "--generate":

                if (args.length < 2) {
                    System.err.println("Please provide a path where to save the key: [KEY] KEYSIZE");
                    System.exit(1);
                } else {
                    if (args.length < 3) {
                        aes.printKeytoFile(args[1]);
                    } else if (!args[2].matches("(128|192|256)")) {
                        System.err.println("Please provide a valid key size (128, 192 or 256)");
                        System.exit(1);
                    } else {
                        aes.setKeySize(Integer.parseInt(args[2]));
                        aes.printKeytoFile(args[1]);
                    }
                }

                break;
            case "--encrypt":
                if (args.length < 3) {
                    System.err.println("Please provide correct arguments for encrypt operation: INPUT_PATH OUTPUT_PATH [KEY]");
                    System.exit(1);
                } else if (!Paths.get(args[1]).toFile().canRead() || args[2] == null) {
                    System.err.println("Please provide valid paths! Program terminating..");
                    System.exit(1);
                } else {
                    String in = "";
                    try {
                        in = readFile(args[1], Charset.defaultCharset());
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (args[3] == null) {
                        System.err.println("Please provide valid key path! Program terminating..");
                        System.exit(1);
                    } else {
                        String key = "";
                        try {
                            key = readFile(args[3], Charset.defaultCharset());
                        } catch (IOException ex) {
                            System.out.println("Please provide a valid key path! Program terminating..");
                            System.exit(1);
                        }
                        String out;
                        try {

                            out = aes.encrypt(in, key);    
                            Path outputPath = Paths.get(args[2]);
                            if (outputPath.toFile().exists()) {
                                outputPath.toFile().delete();
                            }
                            PrintWriter writer = new PrintWriter(outputPath.toFile(), "UTF-8");
                            System.out.println("encrypted cipher:");
                            System.out.println(out);
                            writer.print(out);
                            writer.close();
                        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (NoSuchPaddingException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (NoSuchAlgorithmException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                break;

            case "--decrypt":
                if (args.length < 3) {
                    System.err.println("Please provide correct arguments for decrypt operation: INPUT_PATH OUTPUT_PATH [KEY]");
                    System.exit(1);
                } else if (!Paths.get(args[1]).toFile().canRead() || args[2] == null) {
                    System.err.println("Please provide valid paths! Program terminating..");
                    System.exit(1);
                } else {
                    if (args[3] == null) {
                        System.err.println("Please provide valid key path! Program terminating..");
                        System.exit(1);
                    } else {
                        String out = "";
                        String key = "";
                        try {
                            key = readFile(args[3], Charset.defaultCharset());
                        } catch (IOException ex) {
                            System.out.println("Please provide a valid key path! Program terminating..");
                            System.exit(1);
                        }
                        try {
                            String in ;

                            in =readFile(args[1], Charset.defaultCharset());
                            System.out.println("encrypted cipher:");
                            System.out.println(in);
                            out = aes.decrypt(in, key, null);
                            Path outputPath = Paths.get(args[2]);
                            if (!outputPath.toFile().exists()) {
                                Files.createFile(outputPath);
                            } else {
                                outputPath.toFile().delete();
                            }
                            try (FileOutputStream outStream = new FileOutputStream(outputPath.toFile(), true)) {
                                outStream.write(out.getBytes());
                            }
                        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                break;

            default:
                System.err.println("Please provide a valid command! Program terminating..");
                System.exit(1);

        }
        System.exit(0);
    }

    
     // Read file to a single String
    private static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
