package kernel.network.gameclient.packets.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.ByteOrder;
import java.util.List;


@ChannelHandler.Sharable
public class LengthFieldBasedFrameEncoder extends MessageToMessageEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        final ByteBuf buf = ctx.alloc().buffer(2);
        final short length = (short) (msg.readableBytes() + 2);
        buf.writeShort(buf.order() != ByteOrder.LITTLE_ENDIAN ? Short.reverseBytes(length) : length);
        out.add(buf);
        out.add(msg.retain());
    }
}