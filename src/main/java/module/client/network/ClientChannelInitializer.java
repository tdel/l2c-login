package module.client.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import kernel.Kernel;
import module.client.network.packets.codec.CryptCodec;
import module.client.network.packets.codec.LengthFieldBasedFrameEncoder;
import module.client.network.packets.codec.PacketEncoder;
import module.client.network.packets.security.Crypt;
import module.client.security.BlowfishGenerator;

import javax.crypto.SecretKey;
import java.nio.ByteOrder;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel>
{
    private Kernel kernel;
    private BlowfishGenerator blowfishGenerator;

    public ClientChannelInitializer(Kernel _kernel, BlowfishGenerator _blowfishGenerator) {
        this.kernel = _kernel;
        this.blowfishGenerator = _blowfishGenerator;
    }

    @Override
    protected void initChannel(SocketChannel ch)
    {
        SecretKey secretKey = this.blowfishGenerator.generateBlowfishKey();

        ch.pipeline().addLast("length-decoder", new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2, 0, 2, -2, 2, false));
        ch.pipeline().addLast("length-encoder", new LengthFieldBasedFrameEncoder());
        ch.pipeline().addLast("crypt-codec", new CryptCodec(new Crypt(secretKey)));
        // ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
        //ch.pipeline().addLast("packet-decoder", new PacketDecoder(ByteOrder.LITTLE_ENDIAN));
        ch.pipeline().addLast("packet-encoder", new PacketEncoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2));
        ch.pipeline().addLast("handler", new ClientHandler(this.kernel, secretKey, this.blowfishGenerator.getRandomScrambledRSAKeyPair()));
    }
}
