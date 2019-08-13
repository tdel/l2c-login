package module.client.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordSecurity {

    private MessageDigest digest;

    public PasswordSecurity() throws NoSuchAlgorithmException {
        this.digest = MessageDigest.getInstance("SHA-256");
    }

    public String encode(String _password) {
        byte[] encoded = this.digest.digest(_password.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(encoded);
    }

}
