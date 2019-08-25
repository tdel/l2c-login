package view.gameserver;

import network.gameserver.packets.OutgoingGameServerPacketInterface;
import network.gameserver.packets.PacketWriter;

public class InitPacket implements OutgoingGameServerPacketInterface {

    public void write(PacketWriter _writer) {
        _writer.setCode("init");
    }
}
