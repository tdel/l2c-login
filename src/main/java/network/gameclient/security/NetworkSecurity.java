package network.gameclient.security;

import com.google.inject.Inject;
import network.gameclient.packets.codec.CryptCodec;
import network.gameclient.packets.security.Crypt;
import util.Rnd;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;

public class NetworkSecurity {

    private final ScrambledRSAKeyPair[] _scrambledRSAKeyPairs = new ScrambledRSAKeyPair[50];
    private SecretKey secretKey;

    @Inject
    public NetworkSecurity() {

    }

    public void load() throws Exception {
        KeyGenerator _blowfishKeyGenerator = KeyGenerator.getInstance("Blowfish");

        final KeyPairGenerator rsaKeyPairGenerator = KeyPairGenerator.getInstance("RSA");
        final RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4);
        rsaKeyPairGenerator.initialize(spec);

        for (int i = 0; i < _scrambledRSAKeyPairs.length; i++) {
            _scrambledRSAKeyPairs[i] = new ScrambledRSAKeyPair(rsaKeyPairGenerator.generateKeyPair());
        }

        this.secretKey = _blowfishKeyGenerator.generateKey();
    }

    public ScrambledRSAKeyPair getRandomScrambledRSAKeyPair() {
        return _scrambledRSAKeyPairs[Rnd.nextInt(_scrambledRSAKeyPairs.length)];
    }

    public CryptCodec getCryptCodec() {
        return new CryptCodec(new Crypt(secretKey.getEncoded()));
    }

    public byte[] getEncodedSecretKey() {
        return secretKey.getEncoded();
    }
}
