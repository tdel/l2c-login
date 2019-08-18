package app.kernel.subsystem.network.gameclient.packets;

import io.netty.buffer.ByteBuf;

public class PacketWriter {

    private ByteBuf buffer;

    public PacketWriter(ByteBuf _buffer) {
        this.buffer = _buffer;
    }

    public final void writeD(final int value) {
        this.buffer.writeInt(value);
    }

    public final void writeQ(final long value) {
        this.buffer.writeLong(value);
    }

    public final void writeF(final double value) {
        this.buffer.writeDouble(value);
    }

    public final void writeC(final int data) {
        this.buffer.writeByte((byte) data);
    }

    public final void writeH(final int value) {
        this.buffer.writeShort((short) value);
    }

    public final void writeB(final byte[] data) {
        this.buffer.writeBytes(data);
    }

    public final void writeS(final String text) {
        if (text != null) {
            final int len = text.length();
            for (int i = 0; i < len; i++) {
                this.buffer.writeChar(text.charAt(i));
            }
        }

        this.buffer.writeChar('\000');
    }

}
