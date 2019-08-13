package module.player.network.packets.out;

import module.player.network.PacketWriter;

public class GameGuardAuth extends AbstractOutPacket {

    private final int response;

    public GameGuardAuth(int _response) {
        this.response = _response;
    }

    @Override
    public void write(PacketWriter _writer) {
        _writer.putByte(0x0b);
        _writer.putInt(this.response);
        _writer.putInt(0x00);
        _writer.putInt(0x00);
        _writer.putInt(0x00);
        _writer.putInt(0x00);
    }

}
