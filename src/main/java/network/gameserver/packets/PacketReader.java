package network.gameserver.packets;


import java.util.Map;

public class PacketReader {

    private final Map<String,Object> result;

    public PacketReader(Map<String, Object> _decoded) {
        this.result = _decoded;
    }

    public <T> T get(String _key) {
        return (T) this.result.get(_key);
    }

    public String getCode() {
        return (String) this.result.get("code");
    }
}