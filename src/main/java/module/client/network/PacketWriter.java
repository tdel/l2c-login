package module.client.network;

import io.netty.buffer.ByteBuf;

public class PacketWriter {

    private ByteBuf buffer;

    public PacketWriter(ByteBuf _buffer) {
        this.buffer = _buffer;
    }

    public final void putInt(final int value) {
        this.buffer.writeInt(value);
    }

    @Deprecated
    public final void writeD(final int value) {
        this.putInt(value);
    }

    public final void putLong(final long value) {
        this.buffer.writeLong(value);
    }

    @Deprecated
    public final void writeQ(final long value) {
        this.putLong(value);
    }

    public final void putDouble(final double value) {
        this.buffer.writeDouble(value);
    }

    @Deprecated
    public final void writeF(final double value) {
        this.putDouble(value);
    }

    public final void putFloat(final float value) {
        this.buffer.writeFloat(value);
    }

    public final void putByte(final int data) {
        this.buffer.writeByte((byte) data);
    }

    @Deprecated
    public final void writeC(final int data) {
        this.putByte(data);
    }

    public final void putShort(final int value) {
        this.buffer.writeShort((short) value);
    }

    @Deprecated
    public final void writeH(final int value) {
        this.putShort(value);
    }

    public final void putBytes(final byte[] data) {
        this.buffer.writeBytes(data);
    }

    @Deprecated
    public final void writeB(final byte[] data) {
        this.putBytes(data);
    }

    public final void putString(final String text) {
        if (text != null) {
            final int len = text.length();
            for (int i = 0; i < len; i++) {
                this.buffer.writeChar(text.charAt(i));
            }
        }

        this.buffer.writeChar('\000');
    }

    @Deprecated
    public final void writeS(final String text) {
        this.putString(text);
    }

}
