package game.network.response.gameserver;

import kernel.network.gameserver.packets.OutgoingGameServerPacketInterface;
import kernel.network.gameserver.packets.PacketWriter;

public class InitPacket implements OutgoingGameServerPacketInterface {

    public void write(PacketWriter _writer) {
        _writer.setCode("init");
    }
}
