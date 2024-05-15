package assignmentds;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class for Password Hashing
 */
public class SecureEncryptor {

    private static final int SALT_LENGTH = 16;

    public static byte[] generateSalt() {
        //generate cryptographically secure random values
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    private static String encryptString(String input, byte[] salt) {
        try {
            //initializes a MessageDigest instance for the SHA-256 algorithm
            //incorporates the salt into the hash computation
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            
            //obtain final hash value
            //initialized to build the hexadecimal representation of the hash
            //and append each byte of the message digest in a hexadecimal format
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e) {
            Logger.getLogger(SecureEncryptor.class.getName()).log(Level.SEVERE, null, e);
            System.exit(1);
            return null;
        }
    }
    
    public static String hashPassword(String password, byte[] salt) {
        return SecureEncryptor.encryptString(password, salt);
    }
}
