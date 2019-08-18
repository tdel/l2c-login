package app.kernel.subsystem.network.gameclient.packets.codec;

import app.kernel.subsystem.network.gameclient.packets.OutgoingGameClientPacketInterface;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import app.kernel.subsystem.network.gameclient.packets.PacketWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteOrder;


@ChannelHandler.Sharable
public class PacketEncoder extends MessageToByteEncoder<OutgoingGameClientPacketInterface>
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
    protected void encode(ChannelHandlerContext _ctx, OutgoingGameClientPacketInterface _packet, ByteBuf _out)
    {
        if (_out.order() != this.byteOrder) {
            _out = _out.order(this.byteOrder);
        }

        try {
            _packet.write(new PacketWriter(_out));
            if (_out.writerIndex() > this.maxPacketSize) {
                throw new IllegalStateException("Packet (" + _packet + ") size (" + _out.writerIndex() + ") is bigger than the limit (" + this.maxPacketSize + ")");
            }
        } catch (Throwable e) {
            logger.error("Failed sending Packet("+_packet+")");
            e.printStackTrace();
            // Avoid sending the packet if some exception happened
            _out.clear();
        }
    }
}
