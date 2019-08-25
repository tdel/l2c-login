package network.gameclient;

import com.google.inject.Inject;
import controller.GameClientController;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import network.gameclient.packets.codec.CryptCodec;
import network.gameclient.packets.codec.LengthFieldBasedFrameEncoder;
import network.gameclient.packets.codec.PacketEncoder;
import network.gameclient.packets.security.Crypt;
import network.gameclient.security.BlowfishGenerator;

import javax.crypto.SecretKey;
import java.nio.ByteOrder;

public class GameClientChannelInitializer extends ChannelInitializer<SocketChannel>
{
    private final GameServerControllerInterface controller;
    private final BlowfishGenerator blowfishGenerator;
    private final LengthFieldBasedFrameEncoder frameEncoder;
    private final PacketEncoder packetEncoder;

    @Inject
    public GameClientChannelInitializer(GameClientController _controller, BlowfishGenerator _blowfishGenerator) {
        this.controller = _controller;
        this.blowfishGenerator = _blowfishGenerator;
        this.frameEncoder = new LengthFieldBasedFrameEncoder();
        this.packetEncoder = new PacketEncoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2);
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        SecretKey secretKey = this.blowfishGenerator.generateBlowfishKey();

        ch.pipeline().addLast("length-decoder",new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2, 0, 2, -2, 2, false));
        ch.pipeline().addLast("length-encoder", this.frameEncoder);
        ch.pipeline().addLast("crypt-codec", new CryptCodec(new Crypt(secretKey.getEncoded())));
        ch.pipeline().addLast("packet-encoder", this.packetEncoder);
        ch.pipeline().addLast("handler", new GameClientChannelHandler(this.controller, secretKey, this.blowfishGenerator.getRandomScrambledRSAKeyPair()));
    }
}