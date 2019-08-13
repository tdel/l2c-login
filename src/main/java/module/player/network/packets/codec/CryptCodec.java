package module.player.network.packets.codec;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.channel.ChannelHandlerContext;
import module.player.network.packets.security.Crypt;

import java.util.List;

public class CryptCodec extends ByteToMessageCodec<ByteBuf> {

    private final Crypt crypt;

    public CryptCodec(Crypt _crypt) {
        super();
        this.crypt = _crypt;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) {
        // Check if there are any data to encrypt.
        if (!msg.isReadable()) {
            return;
        }

        msg.resetReaderIndex();
        this.crypt.encrypt(msg);
        msg.resetReaderIndex();
        out.writeBytes(msg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        in.resetReaderIndex();
        this.crypt.decrypt(in);
        in.readerIndex(in.writerIndex());
        out.add(in.copy(0, in.writerIndex()));
    }
}