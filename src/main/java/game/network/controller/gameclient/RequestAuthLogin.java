package game.network.controller.gameclient;

import com.google.inject.Inject;
import game.model.entity.Account;
import game.service.ServiceOperationResult;
import game.network.server.gameclient.GCClient;
import game.network.server.gameclient.GCClientState;
import kernel.network.gameclient.packets.IncomingGameClientPacketInterface;
import kernel.network.gameclient.packets.PacketReader;
import game.network.response.gameclient.LoginFail;
import game.network.response.gameclient.ServersList;
import game.service.playerlogin.PlayerLoginService;
import game.model.repository.GameServerRepository;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class RequestAuthLogin implements IncomingGameClientPacketInterface {

    private final PlayerLoginService loginService;
    private final GameServerRepository gameServers;

    @Inject
    public RequestAuthLogin(PlayerLoginService _loginService, GameServerRepository _gameServers) {
        this.loginService = _loginService;
        this.gameServers = _gameServers;
    }

    @Override
    public boolean supports(PacketReader _reader, GCClientState _state) {
        if (_state != GCClientState.CONNECTED) {
            return false;
        }

        return _reader.getPacketId() == 0x00;
    }


    @Override
    public void execute(PacketReader _reader, GCClient _client) {

        Map<String, String> parameters = this.parseRequest(_reader, _client);
        if (null == parameters) {
            return;
        }

        ServiceOperationResult<Account> result = this.loginService.checkCredentials(parameters.get("login"), parameters.get("password"));
        if (!result.isSuccess()) {
            if (result.errorEnumEquals(PlayerLoginService.LoginReason.NOT_FOUND) || result.errorEnumEquals(PlayerLoginService.LoginReason.INVALID_PASSWORD)) {
                _client.sendPacket(new LoginFail(LoginFail.LoginFailReason.REASON_USER_OR_PASS_WRONG));
            } else {
                System.err.println("Missing error return : " + result.errorEnumValue());
            }

            return;
        }

        _client.attachAccount(result.target());
        _client.setState(GCClientState.LOGGED_IN);
        _client.sendPacket(new ServersList(this.gameServers.getAll()));
    }

    private Map<String, String> parseRequest(PacketReader _reader, GCClient _client) {
        byte[] raw;
        byte[] raw2;
        boolean newAuth;

        byte[] decrypted;

        try {
            if (_reader.getReadableBytes() >= (128 + 128 + 4 + 16)) {
                raw = _reader.readB(128);
                raw2 = _reader.readB(128);
                decrypted = _client.decryptContent(raw, raw2);
                newAuth = true;
            } else if (_reader.getReadableBytes() >= (128 + 4 + 16)) {
                raw = _reader.readB(128);
                decrypted = _client.decryptContent(raw);
                newAuth = false;
            } else {
                throw new IllegalArgumentException();
            }
        } catch (GeneralSecurityException e) {
            _client.sendPacket(new LoginFail(LoginFail.LoginFailReason.REASON_ACCESS_FAILED));
            _client.disconnect();

            return null;
        }

        int connectionId = _reader.readD();
        byte[] gameGuard = _reader.readB(16);

        return this.extractParameters(decrypted, newAuth);
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
