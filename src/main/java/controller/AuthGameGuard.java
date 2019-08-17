package controller;

import subsystem.network.gameclient.ChannelHandler;
import subsystem.network.gameclient.packets.AbstractInPacket;
import subsystem.network.gameclient.packets.PacketReader;
import subsystem.network.gameclient.packets.out.GameGuardAuth;
import subsystem.network.gameclient.packets.out.LoginFail;

public class AuthGameGuard extends AbstractInPacket {

    @Override
    public void execute(PacketReader _reader, ChannelHandler _client) {
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
