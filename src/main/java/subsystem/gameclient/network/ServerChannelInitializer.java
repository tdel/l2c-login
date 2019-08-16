package subsystem.gameclient.network;

import com.google.inject.Inject;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import kernel.Kernel;
import subsystem.gameclient.network.packets.codec.CryptCodec;
import subsystem.gameclient.network.packets.codec.LengthFieldBasedFrameEncoder;
import subsystem.gameclient.network.packets.codec.PacketEncoder;
import subsystem.gameclient.network.packets.security.Crypt;
import subsystem.gameclient.security.BlowfishGenerator;

import javax.crypto.SecretKey;
import java.nio.ByteOrder;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel>
{
    private final Kernel kernel;
    private final BlowfishGenerator blowfishGenerator;

    private final LengthFieldBasedFrameDecoder frameDecoder;
    private final LengthFieldBasedFrameEncoder frameEncoder;

    private final PacketEncoder packetEncoder;

    @Inject
    public ServerChannelInitializer(Kernel _kernel, BlowfishGenerator _blowfishGenerator) {
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
        ch.pipeline().addLast("handler", new ChannelHandler(this.kernel, secretKey, this.blowfishGenerator.getRandomScrambledRSAKeyPair()));
    }
}
