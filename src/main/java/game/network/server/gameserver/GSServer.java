package game.network.server.gameserver;

import game.network.controller.GameServerController;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import kernel.configuration.Config;
import kernel.network.core.NetworkServer;
import com.google.inject.Inject;
import kernel.network.gameserver.packets.codec.JSONToPacket;
import kernel.network.gameserver.packets.codec.PacketToJSON;

public class GSServer extends NetworkServer {

    private final PacketToJSON encoder;
    private final JSONToPacket decoder;

    private final GameServerController frontController;
    private final Config config;

    @Inject
    public GSServer(Config _configuration, GameServerController _controller, PacketToJSON _encoder, JSONToPacket _decoder) {
        this.frontController = _controller;
        this.encoder = _encoder;
        this.decoder = _decoder;
        this.config = _configuration;
    }

    protected ChannelInitializer<SocketChannel> generateChannelInitializer() {
        return new ChannelInitializer<>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("decoder", decoder);
                ch.pipeline().addLast("encoder", encoder);
                ch.pipeline().addLast("handler", new GSClient(frontController));
            }
        };
    }

    protected int getPort() {
        return this.config.getInt("network.gameserver.server.port");
    }
}
