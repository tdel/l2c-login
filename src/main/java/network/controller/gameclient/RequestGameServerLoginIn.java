package network.controller.gameclient;

import com.google.inject.Inject;
import network.gameclient.packets.GCPacketInInterface;
import network.gameclient.packets.PacketReader;
import network.response.gameclient.GameServerLoginSuccess;
import network.server.gameclient.GCClient;
import network.server.gameclient.GCClientState;
import service.playerlogin.PlayerLoginService;

public class RequestGameServerLoginIn implements GCPacketInInterface {

    private final PlayerLoginService loginService;

    @Inject
    public RequestGameServerLoginIn(PlayerLoginService _loginService) {
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

        int keys = this.loginService.putAccountAsLoggedIn(_client);
        _client.sendPacket(new GameServerLoginSuccess(keys, keys));
    }

}
