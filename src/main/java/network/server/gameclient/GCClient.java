package network.server.gameclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import network.controller.GameClientController;
import network.gameclient.packets.OutgoingGameClientPacketInterface;
import network.gameclient.packets.PacketReader;
import network.gameclient.security.NetworkSecurity;
import network.gameclient.security.ScrambledRSAKeyPair;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.security.PrivateKey;


public class GCClient extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LogManager.getLogger();

    private final GameClientController frontController;
    private ScrambledRSAKeyPair scrambledRSAKeyPair;

    private final NetworkSecurity networkSecurity;

    private GCClientState connectionState;
    private Channel channel;
    private int waitingKeys;


    public GCClient(GameClientController _frontController, NetworkSecurity _networkSecurity) {
        this.frontController = _frontController;
        this.networkSecurity = _networkSecurity;
    }

    @Override
    public void channelActive(ChannelHandlerContext _ctx) throws Exception {
        super.channelActive(_ctx);

        this.channel = _ctx.channel();

        InetSocketAddress address = (InetSocketAddress) _ctx.channel().remoteAddress();
        logger.info("New client <" + address.getHostString() + ">");

        this.scrambledRSAKeyPair = this.networkSecurity.getRandomScrambledRSAKeyPair();

        this.frontController.active(this);
    }

    @Override
    public void channelRead(ChannelHandlerContext _ctx, Object _msg) {

        ByteBuf in = this.readByteBuf(_msg);
        if (null == in) {
            return;
        }

        PacketReader reader = new PacketReader(in);

        try {
            this.frontController.handle(reader, this);
        } finally {
            in.readerIndex(in.writerIndex());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext _ctx, Throwable _cause) {
        _cause.printStackTrace();
    }

    public byte[] getPublicKey() {
        return this.scrambledRSAKeyPair.getScrambledModulus();
    }

    public void sendPacket(OutgoingGameClientPacketInterface _packet) {
        logger.info("Sending <" + _packet.getClass().getName() + ">");

        this.channel.writeAndFlush(_packet);
    }

    public void disconnect() {
        this.channel.disconnect();
    }

    public GCClientState getState() {
        return this.connectionState;
    }

    public void setState(GCClientState _state ) {
        this.connectionState = _state;
    }

    public int getSessionId() {
        return 1;
    }



    public int generateWaitingKeys() {
        this.waitingKeys = RandomUtils.nextInt();

        return this.waitingKeys;
    }

    public int getWaitingKeys() {
        return this.waitingKeys;
    }


    public PrivateKey getRSAPrivateKey() {
        return this.scrambledRSAKeyPair.getPrivateKey();
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