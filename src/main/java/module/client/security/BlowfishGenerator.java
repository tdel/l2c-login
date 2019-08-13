package module.client.security;

import util.Rnd;

import java.security.GeneralSecurityException;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class BlowfishGenerator {

    private final KeyGenerator _blowfishKeyGenerator;
    private final ScrambledRSAKeyPair[] _scrambledRSAKeyPairs = new ScrambledRSAKeyPair[50];

    protected BlowfishGenerator() throws GeneralSecurityException
    {
        _blowfishKeyGenerator = KeyGenerator.getInstance("Blowfish");

        final KeyPairGenerator rsaKeyPairGenerator = KeyPairGenerator.getInstance("RSA");
        final RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4);
        rsaKeyPairGenerator.initialize(spec);

        for (int i = 0; i < _scrambledRSAKeyPairs.length; i++)
        {
            _scrambledRSAKeyPairs[i] = new ScrambledRSAKeyPair(rsaKeyPairGenerator.generateKeyPair());
        }

    }

    public SecretKey generateBlowfishKey()
    {
        return _blowfishKeyGenerator.generateKey();
    }

    public ScrambledRSAKeyPair getRandomScrambledRSAKeyPair()
    {
        return _scrambledRSAKeyPairs[Rnd.nextInt(_scrambledRSAKeyPairs.length)];
    }

    public static BlowfishGenerator getInstance()
    {
        return SingletonHolder._instance;
    }

    private static class SingletonHolder
    {
        protected static final BlowfishGenerator _instance;

        static {
            BlowfishGenerator instance = null;
            try {
                instance = new BlowfishGenerator();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } finally {
                _instance = instance;
            }
        }
    }

}
