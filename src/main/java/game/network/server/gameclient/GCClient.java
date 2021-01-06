package game.network.server.gameclient;

import game.model.entity.Account;
import game.network.controller.GameClientController;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import kernel.network.gameclient.packets.OutgoingGameClientPacketInterface;
import kernel.network.gameclient.packets.PacketReader;
import org.apache.commons.lang3.RandomUtils;
import kernel.network.gameclient.security.ScrambledRSAKeyPair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;


public class GCClient extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LogManager.getLogger();

    private final GameClientController frontController;
    private final SecretKey secretKey;
    private final ScrambledRSAKeyPair scrambledRSAKeyPair;


    private GCClientState connectionState;
    private Channel channel;
    private int waitingKeys;


    public GCClient(GameClientController _frontController, SecretKey _secretKey, ScrambledRSAKeyPair _scrambledRSAKeyPair) {
        this.frontController = _frontController;
        this.secretKey = _secretKey;
        this.scrambledRSAKeyPair = _scrambledRSAKeyPair;
    }

    @Override
    public void channelActive(ChannelHandlerContext _ctx) throws Exception {
        super.channelActive(_ctx);

        this.channel = _ctx.channel();

        InetSocketAddress address = (InetSocketAddress) _ctx.channel().remoteAddress();
        logger.info("New client <" + address.getHostString() + ">");

        this.frontController.active(this, this.scrambledRSAKeyPair, this.secretKey);
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

    public void sendPacket(OutgoingGameClientPacketInterface _packet) {
        logger.info("Sending <" + _packet.getClass().getName() + ">");

        this.channel.writeAndFlush(_packet);
    }

    public void attachAccount(Account _account) {

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

    public byte[] decryptContent(byte[] _raw, byte[] _raw2) throws GeneralSecurityException {
        byte[] decrypted = new byte[256];

        final Cipher rsaCipher = Cipher.getInstance("RSA/ECB/NoPadding");
        rsaCipher.init(Cipher.DECRYPT_MODE, this.getRSAPrivateKey());
        rsaCipher.doFinal(_raw, 0, 128, decrypted, 0);
        rsaCipher.doFinal(_raw2, 0, 128, decrypted, 128);

        return decrypted;
    }

    public byte[] decryptContent(byte[] _raw) throws GeneralSecurityException {
        byte[] decrypted = new byte[128];

        final Cipher rsaCipher = Cipher.getInstance("RSA/ECB/NoPadding");
        rsaCipher.init(Cipher.DECRYPT_MODE, this.getRSAPrivateKey());
        rsaCipher.doFinal(_raw, 0, 128, decrypted, 0);

        return decrypted;
    }

    public int generateWaitingKeys() {
        this.waitingKeys = RandomUtils.nextInt();

        return this.waitingKeys;
    }

    public int getWaitingKeys() {
        return this.waitingKeys;
    }


    private PrivateKey getRSAPrivateKey() {
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