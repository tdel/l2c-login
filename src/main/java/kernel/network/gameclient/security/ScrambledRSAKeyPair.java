package kernel.network.gameclient.security;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

public class ScrambledRSAKeyPair
{
    private final KeyPair _pair;
    private final byte[] _scrambledModulus;

    public ScrambledRSAKeyPair(KeyPair pair)
    {
        _pair = pair;

        byte[] scrambledModulus = ((RSAPublicKey) _pair.getPublic()).getModulus().toByteArray();
        if ((scrambledModulus.length == 0x81) && (scrambledModulus[0] == 0))
        {
            scrambledModulus = Arrays.copyOfRange(scrambledModulus, 1, 0x81);
        }

        // step 1 : 0x4d-0x50 <-> 0x00-0x04
        for (int i = 0; i < 4; i++)
        {
            byte temp = scrambledModulus[0x00 + i];
            scrambledModulus[0x00 + i] = scrambledModulus[0x4d + i];
            scrambledModulus[0x4d + i] = temp;
        }

        // step 2 : xor first 0x40 bytes with last 0x40 bytes
        for (int i = 0; i < 0x40; i++)
        {
            scrambledModulus[i] = (byte) (scrambledModulus[i] ^ scrambledModulus[0x40 + i]);
        }

        // step 3 : xor bytes 0x0d-0x10 with bytes 0x34-0x38
        for (int i = 0; i < 4; i++)
        {
            scrambledModulus[0x0d + i] = (byte) (scrambledModulus[0x0d + i] ^ scrambledModulus[0x34 + i]);
        }

        // step 4 : xor last 0x40 bytes with first 0x40 bytes
        for (int i = 0; i < 0x40; i++)
        {
            scrambledModulus[0x40 + i] = (byte) (scrambledModulus[0x40 + i] ^ scrambledModulus[i]);
        }

        _scrambledModulus = scrambledModulus;
    }

    /**
     * Gets the private key.
     * @return the private key
     */
    public PrivateKey getPrivateKey()
    {
        return _pair.getPrivate();
    }

    /**
     * Gets the scrambled modulus.
     * @return the scrambled modulus
     */
    public byte[] getScrambledModulus()
    {
        return _scrambledModulus;
    }
}