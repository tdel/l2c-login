package controller.gameclient;

import app.kernel.subsystem.network.gameclient.ChannelHandler;
import app.kernel.subsystem.network.gameclient.packets.IncomingGameClientPacketInterface;
import app.kernel.subsystem.network.gameclient.packets.PacketReader;
import view.gameclient.GameServerLoginSuccess;

public class RequestGameServerLogin implements IncomingGameClientPacketInterface {

    @Override
    public void execute(PacketReader _reader, ChannelHandler _client) {
        int sessionKey1 = _reader.readD();
        int sessionKey2 = _reader.readD();
        int serverId = _reader.readC();

        _client.sendPacket(new GameServerLoginSuccess(1, 1));
    }

}
