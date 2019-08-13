package module.player.network;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import module.player.network.packets.codec.LengthFieldBasedFrameEncoder;
import module.player.network.packets.codec.PacketEncoder;

import java.nio.ByteOrder;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel>
{
    @Override
    protected void initChannel(SocketChannel ch)
    {
        ch.pipeline().addLast("length-decoder", new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2, 0, 2, -2, 2, false));
        ch.pipeline().addLast("length-encoder", new LengthFieldBasedFrameEncoder());
        //ch.pipeline().addLast("crypt-codec", new CryptCodec(new Crypt(blowfishKey)));
        // ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
        //ch.pipeline().addLast("packet-decoder", new PacketDecoder<>(ByteOrder.LITTLE_ENDIAN, IncomingPackets.PACKET_ARRAY, this.handler));
        ch.pipeline().addLast("packet-encoder", new PacketEncoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2));
        ch.pipeline().addLast("handler", new ClientHandler());
    }
}
