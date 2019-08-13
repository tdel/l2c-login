package module.player.network.packets.security;

import io.netty.buffer.ByteBuf;
import util.Rnd;

import javax.crypto.SecretKey;
import java.nio.ByteOrder;

public class Crypt {
    private static final byte[] STATIC_BLOWFISH_KEY = {
                    (byte) 0x6b,
                    (byte) 0x60,
                    (byte) 0xcb,
                    (byte) 0x5b,
                    (byte) 0x82,
                    (byte) 0xce,
                    (byte) 0x90,
                    (byte) 0xb1,
                    (byte) 0xcc,
                    (byte) 0x2b,
                    (byte) 0x6c,
                    (byte) 0x55,
                    (byte) 0x6c,
                    (byte) 0x6c,
                    (byte) 0x6c,
                    (byte) 0x6c
            };

    private static final BlowfishEngine STATIC_BLOWFISH_ENGINE = new BlowfishEngine();

    static {
        STATIC_BLOWFISH_ENGINE.init(STATIC_BLOWFISH_KEY);
    }

    private final BlowfishEngine _blowfishEngine = new BlowfishEngine();
    private boolean _static = true;

    public Crypt(SecretKey blowfishKey) {
        _blowfishEngine.init(blowfishKey.getEncoded());
    }

    public void encrypt(ByteBuf buf) {

        // Ensure that byte order is little endian because we set new packet size in first 2 bytes
        if (buf.order() != ByteOrder.LITTLE_ENDIAN) {
            buf = buf.order(ByteOrder.LITTLE_ENDIAN);
        }

        // Checksum & XOR Key or Checksum only
        buf.writeZero(_static ? 8 : 4);

        // Padding
        buf.writeZero(8 - (buf.readableBytes() % 8));

        if (_static) {
            _static = false;

            int key = Rnd.nextInt();
            buf.skipBytes(4); // The first 4 bytes are ignored
            while (buf.readerIndex() < (buf.writerIndex() - 8)) {
                int data = buf.readInt();
                key += data;
                data ^= key;
                buf.setInt(buf.readerIndex() - 4, data);
            }
            buf.setInt(buf.readerIndex(), key);

            buf.resetReaderIndex();

            final byte[] block = new byte[8];
            while (buf.isReadable(8)) {
                buf.readBytes(block);
                STATIC_BLOWFISH_ENGINE.encryptBlock(block, 0, block, 0);
                buf.setBytes(buf.readerIndex() - block.length, block);
            }
        } else {
            int checksum = 0;
            while (buf.isReadable(8)) {
                checksum ^= buf.readInt();
            }
            buf.setInt(buf.readerIndex(), checksum);

            buf.resetReaderIndex();

            final byte[] block = new byte[8];
            while (buf.isReadable(8)) {
                buf.readBytes(block);
                _blowfishEngine.encryptBlock(block, 0, block, 0);
                buf.setBytes(buf.readerIndex() - block.length, block);
            }
        }
    }

    public void decrypt(ByteBuf buf) {
        // Packet size must be multiple of 8
        if ((buf.readableBytes() % 8) != 0) {
            buf.clear();
            return;
        }

        final byte[] block = new byte[8];
        while (buf.isReadable(8)) {
            buf.readBytes(block);
            _blowfishEngine.decryptBlock(block, 0, block, 0);
            buf.setBytes(buf.readerIndex() - block.length, block);
        }

        // verify checksum also dont forget!
    }
}