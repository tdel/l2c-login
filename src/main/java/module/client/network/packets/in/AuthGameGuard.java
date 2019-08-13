package module.client.network.packets.in;

import module.client.network.ClientHandler;
import module.client.network.packets.AbstractInPacket;
import module.client.network.packets.PacketReader;
import module.client.network.packets.out.GameGuardAuth;
import module.client.network.packets.out.LoginFail;

public class AuthGameGuard extends AbstractInPacket {

    private int sessionId;

    private int data1;
    private int data2;
    private int data3;
    private int data4;


    public void read(PacketReader _reader) {
        this.sessionId = _reader.readD();
        this.data1 = _reader.readD();
        this.data2 = _reader.readD();
        this.data3 = _reader.readD();
        this.data4 = _reader.readD();
    }

    public void execute(ClientHandler _client) {
        if (_client.getSessionId() != this.sessionId) {

            System.out.println("client session id " + _client.getSessionId() + " | request sessionid " + this.sessionId);

            _client.sendPacket(new LoginFail(LoginFail.LoginFailReason.REASON_ACCESS_FAILED));
            _client.disconnect();

            return;
        }

        _client.sendPacket(new GameGuardAuth(this.sessionId));
    }
}
