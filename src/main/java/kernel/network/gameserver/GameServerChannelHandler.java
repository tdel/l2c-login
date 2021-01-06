package kernel.network.gameserver;

import kernel.network.gameserver.packets.OutgoingGameServerPacketInterface;
import kernel.network.gameserver.packets.PacketReader;
import game.network.controller.GameServerController;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.network.response.gameserver.InitPacket;

import java.net.InetSocketAddress;
import java.util.Map;


public class GameServerChannelHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LogManager.getLogger();

    private Channel channel;

    private GameServerController controller;

    public GameServerChannelHandler(GameServerController _controller) {
        this.controller = _controller;
    }

    public void sendPacket(OutgoingGameServerPacketInterface _packet) {
        logger.info("Sending <" + _packet + ">");

        this.channel.writeAndFlush(_packet);
    }

    @Override
    public void channelActive(ChannelHandlerContext _ctx) throws Exception {
        super.channelActive(_ctx);

        this.channel = _ctx.channel();
        InetSocketAddress address = (InetSocketAddress) _ctx.channel().remoteAddress();

        logger.info("New client <" + address.getHostString() + ">");

        this.sendPacket(new InitPacket());
    }

    @Override
    public void channelRead(ChannelHandlerContext _ctx, Object _msg) {

        //String msg = (String) _msg;
        Map<String, Object> decoded = (Map<String, Object>) _msg;
        PacketReader reader = new PacketReader(decoded);

        this.controller.handle(reader, this);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext _ctx, Throwable _cause) {
        _cause.printStackTrace();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("GameServer disconnected");
    }

}