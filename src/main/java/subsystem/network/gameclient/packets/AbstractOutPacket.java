package subsystem.network.gameclient.packets;

abstract public class AbstractOutPacket {
    abstract public void write(PacketWriter _writer);
}
