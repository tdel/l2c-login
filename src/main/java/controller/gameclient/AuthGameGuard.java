package controller.gameclient;

import network.gameclient.GameClientChannelHandler;
import network.gameclient.packets.IncomingGameClientPacketInterface;
import network.gameclient.packets.PacketReader;
import view.gameclient.GameGuardAuth;
import view.gameclient.LoginFail;

public class AuthGameGuard implements IncomingGameClientPacketInterface {

    @Override
    public void execute(PacketReader _reader, GameClientChannelHandler _client) {
        int sessionId = _reader.readD();
        int data1 = _reader.readD();
        int data2 = _reader.readD();
        int data3 = _reader.readD();
        int data4 = _reader.readD();

        if (_client.getSessionId() != sessionId) {
            _client.sendPacket(new LoginFail(LoginFail.LoginFailReason.REASON_ACCESS_FAILED));
            _client.disconnect();

            return;
        }

        _client.sendPacket(new GameGuardAuth(sessionId));

    }
}
