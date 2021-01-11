package network.server.gameserver;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import network.controller.GameServerController;
import network.gameserver.packets.OutgoingGameServerPacketInterface;
import network.gameserver.packets.PacketReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Map;


public class GSClient extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LogManager.getLogger();
    private final GameServerController controller;

    private Channel channel;


    public GSClient(GameServerController _controller) {
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

        this.controller.active(this);
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