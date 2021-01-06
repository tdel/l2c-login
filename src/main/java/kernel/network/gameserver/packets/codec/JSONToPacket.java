package kernel.network.gameserver.packets.codec;

import com.google.gson.Gson;
import com.google.inject.Inject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;

import java.util.List;
import java.util.Map;

public class JSONToPacket extends ByteToMessageDecoder {

    private final Gson gson;

    @Inject
    public JSONToPacket(Gson _gson) {
        this.gson = _gson;
    }

    @Override
    protected void decode(ChannelHandlerContext _ctx, ByteBuf _in, List<Object> _out) throws Exception {

        String s = _in.readCharSequence(_in.readableBytes(), StandardCharsets.UTF_8).toString();
        Map<String, Object> result = this.gson.fromJson(s, Map.class);

        _out.add(result);
    }
}
