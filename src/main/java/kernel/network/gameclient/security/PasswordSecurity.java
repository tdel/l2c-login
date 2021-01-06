package kernel.network.gameclient.security;

import com.google.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordSecurity {

    private MessageDigest digest;

    @Inject
    public PasswordSecurity() throws NoSuchAlgorithmException {
        this.digest = MessageDigest.getInstance("SHA-256");
    }

    public String encode(String _password) {
        byte[] encoded = this.digest.digest(_password.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(encoded);
    }

}
