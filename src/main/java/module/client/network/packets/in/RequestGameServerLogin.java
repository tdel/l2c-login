package module.client.network.packets.in;

import module.client.network.ClientHandler;
import module.client.network.packets.AbstractInPacket;
import module.client.network.packets.PacketReader;
import module.client.network.packets.out.GameServerLoginSuccess;

public class RequestGameServerLogin extends AbstractInPacket {

    private int sessionKey1;
    private int sessionKey2;
    private int serverId;

    @Override
    public void read(PacketReader _reader) {
        this.sessionKey1 = _reader.readD();
        this.sessionKey2 = _reader.readD();
        this.serverId = _reader.readC();
    }

    @Override
    public void execute(ClientHandler _client) {
        _client.sendPacket(new GameServerLoginSuccess(1, 1));
    }

}
