package module.client.network.packets.in;

import module.client.network.ClientHandler;
import module.client.network.packets.AbstractInPacket;
import module.client.network.packets.PacketReader;
import module.client.network.packets.out.LoginFail;
import module.client.network.packets.out.ServersList;
import module.client.service.playerlogin.PlayerLoginResult;
import module.client.service.playerlogin.PlayerLoginService;
import module.client.service.GameServers;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

public class RequestAuthLogin extends AbstractInPacket {

    private PlayerLoginService loginService;
    private GameServers gameServers;

    public RequestAuthLogin(PlayerLoginService _loginService, GameServers _gameServers) {
        this.loginService = _loginService;
        this.gameServers = _gameServers;
    }

    @Override
    public void execute(PacketReader _reader, ClientHandler _client) {

        byte[] raw;
        byte[] raw2;
        boolean newAuth;

        byte[] decrypted;
        PrivateKey privateKey = _client.getScrambledRSAKeyPair().getPrivateKey();

        try {
            if (_reader.getReadableBytes() >= (128 + 128 + 4 + 16)) {
                raw = _reader.readB(128);
                raw2 = _reader.readB(128);
                decrypted = this.decryptContent(privateKey, raw, raw2);
                newAuth = true;
            } else if (_reader.getReadableBytes() >= (128 + 4 + 16)) {
                raw = _reader.readB(128);
                decrypted = this.decryptContent(privateKey, raw);
                newAuth = false;
            } else {
                throw new IllegalArgumentException();
            }
        } catch (GeneralSecurityException e) {
            _client.sendPacket(new LoginFail(LoginFail.LoginFailReason.REASON_ACCESS_FAILED));
            _client.disconnect();

            return;
        }

        int connectionId = _reader.readD();
        byte[] gameGuard = _reader.readB(16);

        Map<String, String> parameters = this.extractParameters(decrypted, newAuth);
        PlayerLoginResult result = this.loginService.tryLogin(parameters.get("login"), parameters.get("password"));
        if (!result.isSuccess()) {
            LoginFail packet = null;

            switch (result.getReason()) {
                case NOT_FOUND:
                case INVALID_PASSWORD:
                    packet = new LoginFail(LoginFail.LoginFailReason.REASON_USER_OR_PASS_WRONG);
                    break;
                default:
                    System.err.println("missing : " + result.getReason().toString());
            }

            _client.sendPacket(packet);

            return;
        }

        _client.sendPacket(new ServersList(this.gameServers.getLinkedGameServers()));
    }

    private byte[] decryptContent(PrivateKey _key, byte[] _raw, byte[] _raw2) throws GeneralSecurityException {
        byte[] decrypted = new byte[256];

        final Cipher rsaCipher = Cipher.getInstance("RSA/ECB/NoPadding");
        rsaCipher.init(Cipher.DECRYPT_MODE, _key);
        rsaCipher.doFinal(_raw, 0, 128, decrypted, 0);
        rsaCipher.doFinal(_raw2, 0, 128, decrypted, 128);

        return decrypted;
    }

    private byte[] decryptContent(PrivateKey _key, byte[] _raw) throws GeneralSecurityException {
        byte[] decrypted = new byte[128];

        final Cipher rsaCipher = Cipher.getInstance("RSA/ECB/NoPadding");
        rsaCipher.init(Cipher.DECRYPT_MODE, _key);
        rsaCipher.doFinal(_raw, 0, 128, decrypted, 0);

        return decrypted;
    }

    private Map<String, String> extractParameters(byte[] _decrypted, boolean _newAuth) {

        final String login;
        final String password;
        final int ncotp;

        if (_newAuth) {
            login = new String(_decrypted, 0x4E, 50).trim() + new String(_decrypted, 0xCE, 14).trim();
            password = new String(_decrypted, 0xDC, 16).trim();
            ncotp = (_decrypted[0xFC] & 0xFF) | ((_decrypted[0xFD] & 0xFF) << 8) | ((_decrypted[0xFE] & 0xFF) << 16) | ((_decrypted[0xFF] & 0xFF) << 24);
        } else {
            login = new String(_decrypted, 0x5E, 14).trim();
            password = new String(_decrypted, 0x6C, 16).trim();
            ncotp = (_decrypted[0x7C] & 0xFF) | ((_decrypted[0x7D] & 0xFF) << 8) | ((_decrypted[0x7E] & 0xFF) << 16) | ((_decrypted[0x7F] & 0xFF) << 24);
        }

        Map<String, String> params = new HashMap<>();
        params.put("login", login);
        params.put("password", password);
        params.put("ncotp", String.valueOf(ncotp));

        return params;
    }
}
