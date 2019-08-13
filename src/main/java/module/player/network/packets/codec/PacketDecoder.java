package module.player.network.packets.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteOrder;
import java.util.List;
import java.util.logging.Logger;

public class PacketDecoder extends ByteToMessageDecoder {
    private static final Logger LOGGER = Logger.getLogger(PacketDecoder.class.getName());

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

        try {
            final short packetId = in.readUnsignedByte();


        } finally {
            // We always consider that we read whole packet.
            in.readerIndex(in.writerIndex());
        }
    }
}