package network.controller.gameclient;

import network.gameclient.packets.GCPacketInInterface;
import network.gameclient.packets.PacketReader;
import network.response.gameclient.GameGuardAuth;
import network.response.gameclient.LoginFail;
import network.server.gameclient.GCClient;
import network.server.gameclient.GCClientState;

public class AuthGameGuardIn implements GCPacketInInterface {

    @Override
    public boolean supports(PacketReader _reader, GCClientState _state) {
        if (_state != GCClientState.CONNECTED) {
            return false;
        }

        return _reader.getPacketId() == 0x07;
    }

    @Override
    public void execute(PacketReader _reader, GCClient _client) {
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
