package controller;

import app.kernel.Kernel;
import app.kernel.subsystem.network.gameclient.ChannelHandler;
import app.kernel.subsystem.network.gameclient.ControllerHandlerInterface;
import app.kernel.subsystem.network.gameclient.packets.IncomingGameClientPacketInterface;
import app.kernel.subsystem.network.gameclient.packets.PacketReader;
import com.google.inject.Inject;
import controller.gameclient.AuthGameGuard;
import controller.gameclient.RequestAuthLogin;
import controller.gameclient.RequestGameServerLogin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameClientControllerHandler implements ControllerHandlerInterface {

    private static final Logger logger = LogManager.getLogger();

    private Kernel kernel;

    @Inject
    public GameClientControllerHandler(Kernel _kernel) {
        this.kernel = _kernel;
    }

    public IncomingGameClientPacketInterface handle(PacketReader _reader, ChannelHandler _client) {
        final int packetId = _reader.readUnsignedByte() & 0xFF;

        IncomingGameClientPacketInterface packet = null;
        switch (_client.getState()) {
            case CONNECTED:
                switch (packetId) {
                    case 0x07:
                        packet = this.kernel.getService(AuthGameGuard.class);
                        break;

                    case 0x00:
                        packet = this.kernel.getService(RequestAuthLogin.class);
                        break;
                }

                break;
            case LOGGED_IN:
                switch (packetId) {
                    case 0x02:
                        packet = this.kernel.getService(RequestGameServerLogin.class);
                        break;
                }
                break;

        }

        if (null == packet) {
            logger.error("Packet not found : 0x" + String.format("%02X", packetId) + " - state " + _client.getState());
            //in.readerIndex(in.writerIndex());

            return null;
        }

        logger.info("Packet found 0x" + String.format("%02X", packetId) + " <" + packet.getClass().getName() + ">");

        return packet;
    }
}
