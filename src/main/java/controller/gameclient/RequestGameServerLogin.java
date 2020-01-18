package controller.gameclient;

import com.google.inject.Inject;
import model.service.playerlogin.PlayerLoginService;
import network.gameclient.GameClientChannelHandler;
import network.gameclient.packets.IncomingGameClientPacketInterface;
import network.gameclient.packets.PacketReader;
import view.gameclient.GameServerLoginSuccess;

public class RequestGameServerLogin implements IncomingGameClientPacketInterface {

    private final PlayerLoginService loginService;

    @Inject
    public RequestGameServerLogin(PlayerLoginService _loginService) {
        this.loginService = _loginService;
    }

    @Override
    public void execute(PacketReader _reader, GameClientChannelHandler _client) {
        int sessionKey1 = _reader.readD();
        int sessionKey2 = _reader.readD();
        int serverId = _reader.readC();

        int keys = this.loginService.addWaitingAccount(_client);
        _client.sendPacket(new GameServerLoginSuccess(keys, keys));
    }

}
