package module.client.network.packets.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteOrder;
import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    private final ByteOrder _byteOrder;

    public PacketDecoder(ByteOrder byteOrder) {
        _byteOrder = byteOrder;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if ((in == null) || !in.isReadable()) {
            return;
        }

        if (in.order() != _byteOrder) {
            in = in.order(_byteOrder);
        }

        out.add(in);
    }
}