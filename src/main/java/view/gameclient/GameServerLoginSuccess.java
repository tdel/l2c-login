package view.gameclient;

import app.kernel.subsystem.network.gameclient.packets.OutgoingGameClientPacketInterface;
import app.kernel.subsystem.network.gameclient.packets.PacketWriter;

public class GameServerLoginSuccess implements OutgoingGameClientPacketInterface {

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
