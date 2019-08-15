package module.client.network.packets.in;

import module.client.network.ClientHandler;
import module.client.network.packets.AbstractInPacket;
import module.client.network.packets.PacketReader;
import module.client.network.packets.out.GameServerLoginSuccess;

public class RequestGameServerLogin extends AbstractInPacket {

    @Override
    public void execute(PacketReader _reader, ClientHandler _client) {
        int sessionKey1 = _reader.readD();
        int sessionKey2 = _reader.readD();
        int serverId = _reader.readC();

        _client.sendPacket(new GameServerLoginSuccess(1, 1));
    }

}
