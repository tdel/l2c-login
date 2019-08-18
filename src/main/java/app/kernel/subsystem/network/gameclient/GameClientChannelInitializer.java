package app.kernel.subsystem.network.gameclient;

import com.google.inject.Inject;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import app.kernel.Kernel;
import app.kernel.subsystem.network.gameclient.packets.codec.CryptCodec;
import app.kernel.subsystem.network.gameclient.packets.codec.LengthFieldBasedFrameEncoder;
import app.kernel.subsystem.network.gameclient.packets.codec.PacketEncoder;
import app.kernel.subsystem.network.gameclient.packets.security.Crypt;
import app.kernel.subsystem.network.gameclient.security.BlowfishGenerator;

import javax.crypto.SecretKey;
import java.nio.ByteOrder;

public class GameClientChannelInitializer extends ChannelInitializer<SocketChannel>
{
    private final Kernel kernel;
    private final BlowfishGenerator blowfishGenerator;

    private final LengthFieldBasedFrameEncoder frameEncoder;

    private final PacketEncoder packetEncoder;

    @Inject
    public GameClientChannelInitializer(Kernel _kernel, BlowfishGenerator _blowfishGenerator) {
        this.kernel = _kernel;
        this.blowfishGenerator = _blowfishGenerator;
        this.frameEncoder = new LengthFieldBasedFrameEncoder();
        this.packetEncoder = new PacketEncoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2);
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        SecretKey secretKey = this.blowfishGenerator.generateBlowfishKey();

        ch.pipeline().addLast("length-decoder",new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, 0x8000 - 2, 0, 2, -2, 2, false));
        ch.pipeline().addLast("length-encoder", this.frameEncoder);
        ch.pipeline().addLast("crypt-codec", new CryptCodec(new Crypt(secretKey.getEncoded())));
        ch.pipeline().addLast("packet-encoder", this.packetEncoder);
        ch.pipeline().addLast("handler", new ChannelHandler(this.kernel, secretKey, this.blowfishGenerator.getRandomScrambledRSAKeyPair()));
    }
}
