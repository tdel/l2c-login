package subsystem.gameclient.network.packets.in;

import subsystem.gameclient.network.ChannelHandler;
import subsystem.gameclient.network.packets.AbstractInPacket;
import subsystem.gameclient.network.packets.PacketReader;
import subsystem.gameclient.network.packets.out.GameServerLoginSuccess;

public class RequestGameServerLogin extends AbstractInPacket {

    @Override
    public void execute(PacketReader _reader, ChannelHandler _client) {
        int sessionKey1 = _reader.readD();
        int sessionKey2 = _reader.readD();
        int serverId = _reader.readC();

        _client.sendPacket(new GameServerLoginSuccess(1, 1));
    }

}
