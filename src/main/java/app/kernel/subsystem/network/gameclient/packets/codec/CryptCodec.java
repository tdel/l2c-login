package app.kernel.subsystem.network.gameclient.packets.codec;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.channel.ChannelHandlerContext;
import app.kernel.subsystem.network.gameclient.packets.security.Crypt;

import java.util.List;

public class CryptCodec extends ByteToMessageCodec<ByteBuf> {

    private final Crypt crypt;

    public CryptCodec(Crypt _crypt) {
        super();
        this.crypt = _crypt;
    }

    @Override
    protected void encode(ChannelHandlerContext _ctx, ByteBuf _msg, ByteBuf _out) {
        // Check if there are any data to encrypt.
        if (!_msg.isReadable()) {
            return;
        }

        _msg.resetReaderIndex();
        this.crypt.encrypt(_msg);
        _msg.resetReaderIndex();
        _out.writeBytes(_msg);

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        in.resetReaderIndex();
        this.crypt.decrypt(in);
        in.readerIndex(in.writerIndex());
        out.add(in.copy(0, in.writerIndex()));

    }
}