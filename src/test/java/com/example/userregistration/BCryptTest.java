package com.example.userregistration;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import static org.junit.Assert.*;

public class BCryptTest {
    
    @Test
    public void testBCryptPasswordHashing() throws Exception {
        // The password we want to test
        String password = "admin123";
        
        // The hash from the database for password 'admin123'
        String storedHash = "$2a$10$jA9Xfs54kzgIDbO1WrKEJOx9Z2CkFo9ZXdp0vwhtuzSVbQkru5HtW";
        
        // Test 1: Verify the password against the stored hash
        boolean passwordMatches = BCrypt.checkpw(password, storedHash);
        System.out.println("Password matches hash: " + passwordMatches);
        
        // Test 2: Generate a new hash and verify it
        String newHash = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("New hash: " + newHash);
        boolean newHashMatches = BCrypt.checkpw(password, newHash);
        System.out.println("New hash matches password: " + newHashMatches);
        
        // Assertions
        assertTrue("Stored hash should match the password", passwordMatches);
        assertTrue("Newly generated hash should match the password", newHashMatches);
    }
}
