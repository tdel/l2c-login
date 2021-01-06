package game.network.controller.gameclient;

import game.network.server.gameclient.GCClient;
import game.network.server.gameclient.GCClientState;
import kernel.network.gameclient.packets.IncomingGameClientPacketInterface;
import kernel.network.gameclient.packets.PacketReader;
import game.network.response.gameclient.GameGuardAuth;
import game.network.response.gameclient.LoginFail;

public class AuthGameGuard implements IncomingGameClientPacketInterface {

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
