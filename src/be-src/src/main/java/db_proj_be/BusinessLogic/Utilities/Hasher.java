package db_proj_be.BusinessLogic.Utilities;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Hasher {

    public static String hash(String input) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Apply SHA-256 hashing to the input
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            Logger.logMsgFrom(Hasher.class.getName(), e.getMessage(), 1);
        }
        // Return default value in case of exception occurs
        return "ERROR";
    }

    public static void main(String[] args) {
        System.out.println(Hasher.hash("1234"));
    }

}
