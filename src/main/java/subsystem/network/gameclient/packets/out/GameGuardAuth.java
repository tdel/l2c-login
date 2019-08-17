package subsystem.network.gameclient.packets.out;

import subsystem.network.gameclient.packets.PacketWriter;
import subsystem.network.gameclient.packets.AbstractOutPacket;

public class GameGuardAuth extends AbstractOutPacket {

    private final int response;

    public GameGuardAuth(int _response) {
        this.response = _response;
    }

    @Override
    public void write(PacketWriter _writer) {
        _writer.writeC(0x0b);
        _writer.writeD(this.response);
        _writer.writeD(0x00);
        _writer.writeD(0x00);
        _writer.writeD(0x00);
        _writer.writeD(0x00);
    }

}
