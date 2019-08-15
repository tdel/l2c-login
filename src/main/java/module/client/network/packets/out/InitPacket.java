package module.client.network.packets.out;

import module.client.network.packets.PacketWriter;
import module.client.network.packets.AbstractOutPacket;

/**
 * Format: dd b dddd s
 * d: sessionId
 * d: protocol revision
 * b: 0x90 bytes : 0x80 bytes for the scrambled RSA public key + 0x10 bytes at 0x00
 * d: unknown d: unknown d: unknown d: unknown
 * s: blowfish key
 */
public class InitPacket extends AbstractOutPacket {

    private final int sessionId;

    private final byte[] publicKey;
    private final byte[] blowfishKey;

    public InitPacket(byte[] _publickey, byte[] _blowfishkey, int _sessionId) {
        this.sessionId = _sessionId;
        this.publicKey = _publickey;
        this.blowfishKey = _blowfishkey;
    }

    @Override
    public void write(PacketWriter _writer) {
        _writer.writeC(0x00); // init packet id

        _writer.writeD(this.sessionId); // session id
        _writer.writeD(0x0000c621); // protocol revision

        _writer.writeB(this.publicKey); // RSA Public Key

        // Unknown
        _writer.writeD(0x29DD954E);
        _writer.writeD(0x77C39CFC);
        _writer.writeD(0x97ADB620);
        _writer.writeD(0x07BDE0F7);

        _writer.writeB(this.blowfishKey); // BlowFish key

        _writer.writeC(0x00); // null termination ;)
    }
}
