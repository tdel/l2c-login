package module.client.network.packets.out;

import module.client.network.PacketWriter;
import module.client.network.packets.AbstractOutPacket;

public class GameServerLoginSuccess extends AbstractOutPacket {

    private int sessionKey1;
    private int sessionKey2;

    public GameServerLoginSuccess(int _sessionKey1, int _sessionKey2) {
        this.sessionKey1 = _sessionKey1;
        this.sessionKey2 = _sessionKey2;
    }

    public void write(PacketWriter _writer) {
        _writer.writeC(0x07);
        _writer.writeD(this.sessionKey1);
        _writer.writeD(this.sessionKey2);
    }

}
