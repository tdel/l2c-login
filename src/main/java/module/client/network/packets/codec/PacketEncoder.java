package module.client.network.packets.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import module.client.network.packets.PacketWriter;
import module.client.network.packets.AbstractOutPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteOrder;

public class PacketEncoder extends MessageToByteEncoder<AbstractOutPacket>
{
    private static final Logger logger = LogManager.getLogger();

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
            logger.error("Failed sending Packet("+packet+")");
            e.printStackTrace();
            // Avoid sending the packet if some exception happened
            out.clear();

            return;
        }

        out.resetReaderIndex();

        byte[] full = new byte[out.readableBytes()];
        out.readBytes(full);

        StringBuilder sb = new StringBuilder();
        for (byte b : full) {
            sb.append(String.format("%02X ", b));
        }
        logger.info("Encrypting packet : " + sb.toString());

        out.resetReaderIndex();

    }
}
