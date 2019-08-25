package controller.gameclient;

import network.gameclient.GameClientChannelHandler;
import network.gameclient.packets.IncomingGameClientPacketInterface;
import network.gameclient.packets.PacketReader;
import view.gameclient.GameServerLoginSuccess;

public class RequestGameServerLogin implements IncomingGameClientPacketInterface {

    @Override
    public void execute(PacketReader _reader, GameClientChannelHandler _client) {
        int sessionKey1 = _reader.readD();
        int sessionKey2 = _reader.readD();
        int serverId = _reader.readC();

        _client.sendPacket(new GameServerLoginSuccess(1, 1));
    }

}
