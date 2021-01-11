package network.server.gameserver;

import com.google.inject.Inject;
import configuration.Config;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import network.controller.GameServerController;
import network.gameserver.packets.codec.JSONToPacket;
import network.gameserver.packets.codec.PacketToJSON;
import network.server.NetworkServer;

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
