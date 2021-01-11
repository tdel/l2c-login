package network.controller;

import com.google.inject.Inject;
import network.gameclient.GameServerControllerInterface;
import network.gameclient.packets.GCPacketInInterface;
import network.gameclient.packets.PacketReader;
import network.gameclient.security.NetworkSecurity;
import network.response.gameclient.InitPacket;
import network.server.gameclient.GCClient;
import network.server.gameclient.GCClientState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class GameClientController implements GameServerControllerInterface {

    private static final Logger logger = LogManager.getLogger();
    private final Set<GCPacketInInterface> controllers;

    private final NetworkSecurity blowfishGenerator;

    @Inject
    public GameClientController(Set<GCPacketInInterface> _controllers, NetworkSecurity _blowfishGenerator) {
        this.controllers = _controllers;
        this.blowfishGenerator = _blowfishGenerator;
    }


    public void active(GCClient _client) {
        _client.setState(GCClientState.CONNECTED);

        _client.sendPacket(
                new InitPacket(
                        _client.getPublicKey(),
                        this.blowfishGenerator.getEncodedSecretKey(),
                        _client.getSessionId()
                )
        );
    }

    public void handle(PacketReader _reader, GCClient _client) {

        for (GCPacketInInterface packet : this.controllers) {
            if (packet.supports(_reader, _client.getState())) {
                logger.info("Packet found 0x" + String.format("%02X", _reader.getPacketId()) + " <" + packet.getClass().getName() + ">");

                packet.execute(_reader, _client);
            }
        }

        throw new RuntimeException("Unknown packet : 0x" + String.format("%02X", _reader.getPacketId()) + " - state " + _client.getState());
    }
}
