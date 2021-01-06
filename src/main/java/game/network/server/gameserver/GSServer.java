package game.network.server.gameserver;

import game.network.controller.GameServerController;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import kernel.network.core.NetworkServer;
import com.google.inject.Inject;
import kernel.network.gameserver.packets.codec.JSONToPacket;
import kernel.network.gameserver.packets.codec.PacketToJSON;

public class GSServer extends NetworkServer {

    @Inject
    public GSServer(GameServerController _controller, PacketToJSON _encoder, JSONToPacket _decoder) {
        ChannelInitializer<SocketChannel> channelInitializer = new ChannelInitializer<>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("decoder", _decoder);
                ch.pipeline().addLast("encoder", _encoder);
                ch.pipeline().addLast("handler", new GSClient(_controller));
            }
        };

        this.setChannelInitializer(channelInitializer);
    }

}
