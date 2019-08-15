package module.client.network.packets.in;

import module.client.network.ClientHandler;
import module.client.network.packets.AbstractInPacket;
import module.client.network.packets.PacketReader;
import module.client.network.packets.out.GameGuardAuth;
import module.client.network.packets.out.LoginFail;

public class AuthGameGuard extends AbstractInPacket {

    public void execute(PacketReader _reader, ClientHandler _client) {
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
