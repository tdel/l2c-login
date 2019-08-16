package subsystem.gameclient.network.packets;

abstract public class AbstractOutPacket {
    abstract public void write(PacketWriter _writer);
}
