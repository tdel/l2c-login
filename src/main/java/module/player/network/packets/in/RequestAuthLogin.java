package module.player.network.packets.in;

import module.player.network.packets.PacketReader;

public class RequestAuthLogin extends AbstractInPacket {

    private byte[] _raw;
    private byte[] _raw2;
    private int _connectionId;
    private byte[] _gameGuard;
    private boolean _newAuth;

    @Override
    public void read(PacketReader _reader) {
        if (_reader.getReadableBytes() >= (128 + 128 + 4 + 16)) {
            _raw = _reader.readB(128);
            _raw2 = _reader.readB(128);
            _connectionId = _reader.readD();
            _gameGuard = _reader.readB(16);
            _newAuth = true;
        } else if (_reader.getReadableBytes() >= (128 + 4 + 16)) {
            _raw = _reader.readB(128);
            _connectionId = _reader.readD();
            _gameGuard = _reader.readB(16);
            _newAuth = false;
        }
    }

    @Override
    public void execute() {

    }

}
