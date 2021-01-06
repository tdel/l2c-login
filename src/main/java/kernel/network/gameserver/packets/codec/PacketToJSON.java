package kernel.network.gameserver.packets.codec;


import kernel.network.gameserver.packets.OutgoingGameServerPacketInterface;
import kernel.network.gameserver.packets.PacketWriter;
import com.google.gson.Gson;
import com.google.inject.Inject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PacketToJSON extends MessageToByteEncoder<OutgoingGameServerPacketInterface> {

    private static final Logger logger = LogManager.getLogger();
    private Gson gson;

    @Inject
    public PacketToJSON(Gson _gson) {
        this.gson = _gson;
    }

    @Override
    protected void encode(ChannelHandlerContext _ctx, OutgoingGameServerPacketInterface _packet, ByteBuf _out) throws Exception {

        PacketWriter writer = new PacketWriter(this.gson);
        try {
            _packet.write(writer);

            _out.writeBytes(writer.getJSON().getBytes());
        } catch (Throwable e) {
            logger.error("Failed sending Packet("+_packet+")");
            e.printStackTrace();
            // Avoid sending the packet if some exception happened
            _out.clear();
        }

    }
}
