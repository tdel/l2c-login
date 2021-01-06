package game.network.controller.gameclient;

import com.google.inject.Inject;
import game.service.playerlogin.PlayerLoginService;
import game.network.server.gameclient.GCClient;
import game.network.server.gameclient.GCClientState;
import kernel.network.gameclient.packets.IncomingGameClientPacketInterface;
import kernel.network.gameclient.packets.PacketReader;
import game.network.response.gameclient.GameServerLoginSuccess;

public class RequestGameServerLogin implements IncomingGameClientPacketInterface {

    private final PlayerLoginService loginService;

    @Inject
    public RequestGameServerLogin(PlayerLoginService _loginService) {
        this.loginService = _loginService;
    }

    @Override
    public boolean supports(PacketReader _reader, GCClientState _state) {
        if (_state != GCClientState.LOGGED_IN) {
            return false;
        }

        return _reader.getPacketId() == 0x02;
    }

    @Override
    public void execute(PacketReader _reader, GCClient _client) {
        int sessionKey1 = _reader.readD();
        int sessionKey2 = _reader.readD();
        int serverId = _reader.readC();

        int keys = this.loginService.addWaitingAccount(_client);
        _client.sendPacket(new GameServerLoginSuccess(keys, keys));
    }

}
