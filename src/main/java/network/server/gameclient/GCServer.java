package network.server.gameclient;

import com.google.inject.Inject;
import configuration.Config;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import network.controller.GameClientController;
import network.gameclient.packets.codec.LengthFieldBasedFrameEncoder;
import network.gameclient.packets.codec.PacketEncoder;
import network.gameclient.security.NetworkSecurity;
import network.server.NetworkServer;

import java.nio.ByteOrder;

public class GCServer extends NetworkServer {

    private final LengthFieldBasedFrameEncoder frameEncoder = new LengthFieldBasedFrameEncoder();
    private final PacketEncoder packetEncoder = new PacketEncoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2);

    private final Config config;
    private final NetworkSecurity blowfishGenerator;
    private final GameClientController frontController;

    @Inject
    public GCServer(Config _configuration, GameClientController _controller, NetworkSecurity _blowfishGenerator) {
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
                ch.pipeline().addLast("length-decoder",new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2, 0, 2, -2, 2, false));
                ch.pipeline().addLast("length-encoder", frameEncoder);
                ch.pipeline().addLast("crypt-codec", blowfishGenerator.getCryptCodec());
                ch.pipeline().addLast("packet-encoder", packetEncoder);
                ch.pipeline().addLast("handler", new GCClient(frontController, blowfishGenerator)); //secretKey, blowfishGenerator.getRandomScrambledRSAKeyPair()));
            }
        };

    }
}
