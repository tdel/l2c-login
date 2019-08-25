package network.gameclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import network.gameclient.packets.IncomingGameClientPacketInterface;
import network.gameclient.packets.OutgoingGameClientPacketInterface;
import network.gameclient.packets.PacketReader;
import view.gameclient.InitPacket;
import network.gameclient.security.ScrambledRSAKeyPair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;


public class GameClientChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LogManager.getLogger();

    private Channel channel;

    private SecretKey secretKey;
    private ScrambledRSAKeyPair scrambledRSAKeyPair;

    private GameClientConnectionState connectionState;

    private GameServerControllerInterface controller;

    public GameClientChannelHandler(GameServerControllerInterface _controller, SecretKey _secretKey, ScrambledRSAKeyPair _scrambledRSAKeyPair) {
        this.controller = _controller;
        this.secretKey = _secretKey;
        this.scrambledRSAKeyPair = _scrambledRSAKeyPair;

    }

    public GameClientConnectionState getState() {
        return this.connectionState;
    }
    public void setConnectionState(GameClientConnectionState _state ) {
        this.connectionState = _state;
    }

    public ScrambledRSAKeyPair getScrambledRSAKeyPair() {
        return this.scrambledRSAKeyPair;
    }

    public void sendPacket(OutgoingGameClientPacketInterface _packet) {
        logger.info("Sending <" + _packet.getClass().getName() + ">");

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
    public void channelRead(ChannelHandlerContext _ctx, Object _msg) {

        ByteBuf in = this.readByteBuf(_msg);
        if (null == in) {
            return;
        }

        PacketReader reader = new PacketReader(in);

        try {
            IncomingGameClientPacketInterface packet = this.controller.handle(reader, this);
            if (packet == null) {
                return;
            }
            packet.execute(reader, this);
        } finally {
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