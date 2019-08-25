package view.gameserver;

import network.gameserver.packets.OutgoingGameServerPacketInterface;
import network.gameserver.packets.PacketWriter;

public class AuthResult implements OutgoingGameServerPacketInterface {

    private boolean success;
    private String errorMessage;

    private AuthResult() {
        this.success = true;
    }

    private AuthResult(String _errorMessage) {
        this.success = false;
        this.errorMessage = _errorMessage;
    }

    @Override
    public void write(PacketWriter _writer) {
        _writer.setCode("auth-result");

        _writer.add("success", this.success);
        if (null != this.errorMessage) {
            _writer.add("error_message", this.errorMessage);
        }

    }


    static public AuthResult createSuccess() {
        return new AuthResult();
    }

    static public AuthResult createFail(String _errorMessage) {
        return new AuthResult(_errorMessage);
    }
}
