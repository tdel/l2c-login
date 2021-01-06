package game.network.controller;

import kernel.network.gameclient.GameClientChannelHandler;
import kernel.network.gameclient.GameServerControllerInterface;
import kernel.network.gameclient.packets.IncomingGameClientPacketInterface;
import kernel.network.gameclient.packets.PacketReader;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class GameClientController implements GameServerControllerInterface {

    private static final Logger logger = LogManager.getLogger();
    private final Set<IncomingGameClientPacketInterface> controllers;

    @Inject
    public GameClientController(Set<IncomingGameClientPacketInterface> _controllers) {
        this.controllers = _controllers;
    }


    public void handle(PacketReader _reader, GameClientChannelHandler _client) {

        for (IncomingGameClientPacketInterface packet : this.controllers) {
            if (packet.supports(_reader, _client.getState())) {
                logger.info("Packet found 0x" + String.format("%02X", _reader.getPacketId()) + " <" + packet.getClass().getName() + ">");

                packet.execute(_reader, _client);
            }
        }

        throw new RuntimeException("Unknown packet : 0x" + String.format("%02X", _reader.getPacketId()) + " - state " + _client.getState());
    }
}
