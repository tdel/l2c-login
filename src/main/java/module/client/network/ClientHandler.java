package module.client.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import module.client.ClientModule;
import module.client.network.packets.PacketReader;
import module.client.network.packets.AbstractInPacket;
import module.client.network.packets.in.AuthGameGuard;
import module.client.network.packets.in.RequestAuthLogin;
import module.client.network.packets.in.RequestGameServerLogin;
import module.client.network.packets.AbstractOutPacket;
import module.client.network.packets.out.InitPacket;
import module.client.security.ScrambledRSAKeyPair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;


public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LogManager.getLogger();

    private ClientModule module;
    private Channel channel;

    private SecretKey secretKey;
    private ScrambledRSAKeyPair scrambledRSAKeyPair;

    public ClientHandler(ClientModule _module, SecretKey _secretKey, ScrambledRSAKeyPair _scrambledRSAKeyPair) {
        this.module = _module;
        this.secretKey = _secretKey;
        this.scrambledRSAKeyPair = _scrambledRSAKeyPair;
    }

    public ScrambledRSAKeyPair getScrambledRSAKeyPair() {
        return this.scrambledRSAKeyPair;
    }

    public void sendPacket(AbstractOutPacket _packet) {
        logger.info("Sending packet " + _packet.getClass());
        this.channel.writeAndFlush(_packet);
    }

    public int getSessionId() {
        return 1;
    }

    public void disconnect() {
        this.channel.disconnect();
    }

    @Override
    public void channelActive(ChannelHandlerContext _ctx) throws Exception {
        super.channelActive(_ctx);

        this.channel = _ctx.channel();
        InetSocketAddress address = (InetSocketAddress) _ctx.channel().remoteAddress();

        this.sendPacket(new InitPacket( this.scrambledRSAKeyPair.getScrambledModulus(), this.secretKey.getEncoded(), this.getSessionId()));
    }

    private ByteBuf readByteBuf(Object _msg) {
        ByteBuf in = (ByteBuf) _msg;

        if ((in == null) || !in.isReadable()) {
            return null;
        }

        if (in.order() != ByteOrder.LITTLE_ENDIAN) {
            in = in.order(ByteOrder.LITTLE_ENDIAN);
        }

        return in;
    }

    @Override
    public void channelRead(ChannelHandlerContext _ctx, Object _msg) throws Exception {

        ByteBuf in = this.readByteBuf(_msg);
        if (null == in) {
            return;
        }


        final int packetId = in.readUnsignedByte() & 0xFF;

        logger.info("packet ID : "+packetId);

        AbstractInPacket packet = null;
        switch (packetId) {
            case 0x07:
                packet = this.module.getInPacket(AuthGameGuard.class);
                break;

            case 0x00:
                packet = this.module.getInPacket(RequestAuthLogin.class);
                break;

            case 0x02:
                packet = this.module.getInPacket(RequestGameServerLogin.class);
                break;

            default:
                logger.error("Packet not found : " + packetId);
                in.readerIndex(in.writerIndex());

                return;
        }

        packet.read(new PacketReader(in));
        logger.info("Executing " + packet.getClass());

        try {
            packet.execute(this);
        } finally {
            // We always consider that we read whole packet.
            in.readerIndex(in.writerIndex());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext _ctx, Throwable _cause) {
        _cause.printStackTrace();
    }
}