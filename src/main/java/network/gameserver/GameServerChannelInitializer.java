package network.gameserver;

import network.gameserver.packets.codec.JSONToPacket;
import network.gameserver.packets.codec.PacketToJSON;
import com.google.gson.Gson;
import com.google.inject.Inject;
import controller.GameServerController;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class GameServerChannelInitializer extends ChannelInitializer<SocketChannel>
{
    private final GameServerController controller;
    private final PacketToJSON encoder;
    private final JSONToPacket decoder;

    @Inject
    public GameServerChannelInitializer(GameServerController _controller, PacketToJSON _encoder, JSONToPacket _decoder) {
        this.controller = _controller;
        this.encoder = _encoder;
        this.decoder = _decoder;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline().addLast("decoder", this.decoder);
        ch.pipeline().addLast("encoder", this.encoder);
        ch.pipeline().addLast("handler", new GameServerChannelHandler(this.controller));
    }
}
