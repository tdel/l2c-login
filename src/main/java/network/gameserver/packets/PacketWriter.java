package network.gameserver.packets;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class PacketWriter {

    private final Gson gson;

    private Map<String,Object> result;

    public PacketWriter(Gson _gson) {
        this.gson = _gson;
        this.result = new HashMap<>();
    }


    public void setCode(String _value) {
        this.result.put("code", _value);
    }

    public void add(String _key, Object _value) {
        this.result.put(_key, _value);
    }

    public String getJSON() {
        return this.gson.toJson(this.result);
    }

}
