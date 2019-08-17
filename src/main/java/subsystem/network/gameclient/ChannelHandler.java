package subsystem.network.gameclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import kernel.Kernel;
import subsystem.network.gameclient.packets.PacketReader;
import subsystem.network.gameclient.packets.AbstractInPacket;
import controller.AuthGameGuard;
import controller.RequestAuthLogin;
import controller.RequestGameServerLogin;
import subsystem.network.gameclient.packets.AbstractOutPacket;
import subsystem.network.gameclient.packets.out.InitPacket;
import subsystem.network.gameclient.security.ScrambledRSAKeyPair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;


public class ChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LogManager.getLogger();

    private Kernel kernel;
    private Channel channel;

    private SecretKey secretKey;
    private ScrambledRSAKeyPair scrambledRSAKeyPair;

    private GameClientConnectionState connectionState;

    public ChannelHandler(Kernel _kernel, SecretKey _secretKey, ScrambledRSAKeyPair _scrambledRSAKeyPair) {
        this.kernel = _kernel;
        this.secretKey = _secretKey;
        this.scrambledRSAKeyPair = _scrambledRSAKeyPair;
    }

    public void setConnectionState(GameClientConnectionState _state ) {
        this.connectionState = _state;
    }

    public ScrambledRSAKeyPair getScrambledRSAKeyPair() {
        return this.scrambledRSAKeyPair;
    }

    public void sendPacket(AbstractOutPacket _packet) {
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
        this.connectionState = GameClientConnectionState.CONNECTED;

        logger.info("New client <" + address.getHostString() + ">");

        this.sendPacket(new InitPacket( this.scrambledRSAKeyPair.getScrambledModulus(), this.secretKey.getEncoded(), this.getSessionId()));
    }



    @Override
    public void channelRead(ChannelHandlerContext _ctx, Object _msg) throws Exception {

        ByteBuf in = this.readByteBuf(_msg);
        if (null == in) {
            return;
        }

        final int packetId = in.readUnsignedByte() & 0xFF;

        AbstractInPacket packet = null;
        switch (this.connectionState) {
            case CONNECTED:
                switch (packetId) {
                    case 0x07:
                        packet = this.kernel.getService(AuthGameGuard.class);
                        break;

                    case 0x00:
                        packet = this.kernel.getService(RequestAuthLogin.class);
                        break;
                }

                break;
            case LOGGED_IN:
                switch (packetId) {
                    case 0x02:
                        packet = this.kernel.getService(RequestGameServerLogin.class);
                        break;
                }
                break;

        }

        if (null == packet) {
            logger.error("Packet not found : 0x" + String.format("%02X", packetId) + " - state " + this.connectionState);
            in.readerIndex(in.writerIndex());

            return;
        }

        logger.info("Executing packet 0x" + String.format("%02X", packetId) + " <" + packet.getClass().getName() + ">");

        try {
            packet.execute(new PacketReader(in), this);
        } finally {
            // We always consider that we read whole packet.
            in.readerIndex(in.writerIndex());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext _ctx, Throwable _cause) {
        _cause.printStackTrace();
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
}