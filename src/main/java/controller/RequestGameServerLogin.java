package controller;

import subsystem.network.gameclient.ChannelHandler;
import subsystem.network.gameclient.packets.AbstractInPacket;
import subsystem.network.gameclient.packets.PacketReader;
import subsystem.network.gameclient.packets.out.GameServerLoginSuccess;

public class RequestGameServerLogin extends AbstractInPacket {

    @Override
    public void execute(PacketReader _reader, ChannelHandler _client) {
        int sessionKey1 = _reader.readD();
        int sessionKey2 = _reader.readD();
        int serverId = _reader.readC();

        _client.sendPacket(new GameServerLoginSuccess(1, 1));
    }

}
