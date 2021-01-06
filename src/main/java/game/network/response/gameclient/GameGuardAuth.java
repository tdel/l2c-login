package game.network.response.gameclient;

import kernel.network.gameclient.packets.OutgoingGameClientPacketInterface;
import kernel.network.gameclient.packets.PacketWriter;

public class GameGuardAuth implements OutgoingGameClientPacketInterface {

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
