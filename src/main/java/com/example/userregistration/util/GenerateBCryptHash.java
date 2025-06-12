import org.mindrot.jbcrypt.BCrypt;

public class GenerateBCryptHash {
    public static void main(String[] args) {
        String password = "admin123";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("Generated hash for 'admin123': " + hash);
        
        // Verify the hash
        boolean matches = BCrypt.checkpw(password, hash);
        System.out.println("Verification result: " + matches);
    }
}
