package subsystem.gameclient.network.packets.out;

import subsystem.gameclient.network.packets.PacketWriter;
import subsystem.gameclient.network.packets.AbstractOutPacket;

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
