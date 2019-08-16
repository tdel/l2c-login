package subsystem.gameclient.network.packets.out;

import subsystem.gameclient.network.packets.PacketWriter;
import subsystem.gameclient.network.packets.AbstractOutPacket;

public class LoginSuccess extends AbstractOutPacket {

    private final int firstKey;
    private final int secondKey;

    public LoginSuccess(int _firstKey, int _secondKey) {
        this.firstKey = _firstKey;
        this.secondKey = _secondKey;
    }

    @Override
    public void write(PacketWriter _writer) {
        _writer.writeC(0x03);
        _writer.writeD(this.firstKey);
        _writer.writeD(this.secondKey);
        _writer.writeD(0x00);
        _writer.writeD(0x00);
        _writer.writeD(0x000003ea);
        _writer.writeD(0x00);
        _writer.writeD(0x00);
        _writer.writeD(0x00);
        _writer.writeB(new byte[16]);
    }
}
