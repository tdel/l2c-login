package module.player.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import module.player.network.model.Client;


public class ClientHandler extends ChannelInboundHandlerAdapter {

    private Client client;

    @Override
    public void channelActive(ChannelHandlerContext _ctx) throws Exception {
        super.channelActive(_ctx);

        this.client = new Client();

    }

    @Override
    public void channelRead(ChannelHandlerContext _ctx, Object _msg) throws Exception {
        super.channelRead(_ctx, _msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext _ctx, Throwable _cause) throws Exception {
        super.exceptionCaught(_ctx, _cause);

        _cause.printStackTrace();
    }
}