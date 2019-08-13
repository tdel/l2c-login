package module.player.network.packets.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import module.player.network.PacketWriter;
import module.player.network.packets.out.AbstractOutPacket;

import java.nio.ByteOrder;
import java.util.logging.Logger;

public class PacketEncoder extends MessageToByteEncoder<AbstractOutPacket>
{
    private static final Logger LOGGER = Logger.getLogger(PacketEncoder.class.getName());

    private final ByteOrder byteOrder;
    private final int maxPacketSize;

    public PacketEncoder(ByteOrder _byteOrder, int _maxPacketSize) {
        super();
        this.byteOrder = _byteOrder;
        this.maxPacketSize = _maxPacketSize;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractOutPacket packet, ByteBuf out)
    {
        if (out.order() != this.byteOrder) {
            out = out.order(this.byteOrder);
        }

        try {
            packet.write(new PacketWriter(out));
            if (out.writerIndex() > this.maxPacketSize) {
                throw new IllegalStateException("Packet (" + packet + ") size (" + out.writerIndex() + ") is bigger than the limit (" + this.maxPacketSize + ")");
            }
        } catch (Throwable e) {
            LOGGER.warning("Failed sending Packet("+packet+")");
            // Avoid sending the packet if some exception happened
            out.clear();
        }
    }
}
