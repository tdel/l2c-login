package kernel.network.gameserver.packets;

public interface OutgoingGameServerPacketInterface {
    void write(PacketWriter _writer);
}
