package game.network.server.gameclient;

import com.google.inject.Inject;
import game.network.controller.GameClientController;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import kernel.configuration.Config;
import kernel.network.core.NetworkServer;
import kernel.network.gameclient.packets.codec.CryptCodec;
import kernel.network.gameclient.packets.codec.LengthFieldBasedFrameEncoder;
import kernel.network.gameclient.packets.codec.PacketEncoder;
import kernel.network.gameclient.packets.security.Crypt;
import kernel.network.gameclient.security.BlowfishGenerator;

import javax.crypto.SecretKey;
import java.nio.ByteOrder;

public class GCServer extends NetworkServer {

    private final LengthFieldBasedFrameEncoder frameEncoder = new LengthFieldBasedFrameEncoder();
    private final PacketEncoder packetEncoder = new PacketEncoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2);

    private final Config config;
    private final BlowfishGenerator blowfishGenerator;
    private final GameClientController frontController;

    @Inject
    public GCServer(Config _configuration, GameClientController _controller, BlowfishGenerator _blowfishGenerator) {
        this.blowfishGenerator = _blowfishGenerator;
        this.frontController = _controller;
        this.config = _configuration;
    }
 
    protected int getPort() {
        return this.config.getInt("network.gameclient.server.port");
    }

    protected ChannelInitializer<SocketChannel> generateChannelInitializer() {
        return new ChannelInitializer<>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                SecretKey secretKey = blowfishGenerator.generateBlowfishKey();

                ch.pipeline().addLast("length-decoder",new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2, 0, 2, -2, 2, false));
                ch.pipeline().addLast("length-encoder", frameEncoder);
                ch.pipeline().addLast("crypt-codec", new CryptCodec(new Crypt(secretKey.getEncoded())));
                ch.pipeline().addLast("packet-encoder", packetEncoder);
                ch.pipeline().addLast("handler", new GCClient(frontController, secretKey, blowfishGenerator.getRandomScrambledRSAKeyPair()));
            }
        };

    }
}
