package module.client.network;

import com.google.inject.Inject;
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
    private final Kernel kernel;
    private final BlowfishGenerator blowfishGenerator;

    private final LengthFieldBasedFrameDecoder frameDecoder;
    private final LengthFieldBasedFrameEncoder frameEncoder;

    private final PacketEncoder packetEncoder;

    @Inject
    public ClientChannelInitializer(Kernel _kernel, BlowfishGenerator _blowfishGenerator) {
        this.kernel = _kernel;
        this.blowfishGenerator = _blowfishGenerator;
        this.frameDecoder = new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2, 0, 2, -2, 2, false);
        this.frameEncoder = new LengthFieldBasedFrameEncoder();
        this.packetEncoder = new PacketEncoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2);
    }

    @Override
    protected void initChannel(SocketChannel ch)
    {
        SecretKey secretKey = this.blowfishGenerator.generateBlowfishKey();

        ch.pipeline().addLast("length-decoder", this.frameDecoder);
        ch.pipeline().addLast("length-encoder", this.frameEncoder);
        ch.pipeline().addLast("crypt-codec", new CryptCodec(new Crypt(secretKey.getEncoded())));
        ch.pipeline().addLast("packet-encoder", this.packetEncoder);
        ch.pipeline().addLast("handler", new ClientHandler(this.kernel, secretKey, this.blowfishGenerator.getRandomScrambledRSAKeyPair()));
    }
}
