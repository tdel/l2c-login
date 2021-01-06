package game.network.controller;

import game.network.response.gameclient.InitPacket;
import game.network.server.gameclient.GCClient;
import game.network.server.gameclient.GCClientState;
import kernel.network.gameclient.GameServerControllerInterface;
import kernel.network.gameclient.packets.IncomingGameClientPacketInterface;
import kernel.network.gameclient.packets.PacketReader;
import com.google.inject.Inject;
import kernel.network.gameclient.security.ScrambledRSAKeyPair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.util.Set;

public class GameClientController implements GameServerControllerInterface {

    private static final Logger logger = LogManager.getLogger();
    private final Set<IncomingGameClientPacketInterface> controllers;

    @Inject
    public GameClientController(Set<IncomingGameClientPacketInterface> _controllers) {
        this.controllers = _controllers;
    }


    public void active(GCClient _client, ScrambledRSAKeyPair _rsa, SecretKey _secretKey) {
        _client.setState(GCClientState.CONNECTED);

        _client.sendPacket(new InitPacket(_rsa.getScrambledModulus(), _secretKey.getEncoded(), _client.getSessionId()));
    }

    public void handle(PacketReader _reader, GCClient _client) {

        for (IncomingGameClientPacketInterface packet : this.controllers) {
            if (packet.supports(_reader, _client.getState())) {
                logger.info("Packet found 0x" + String.format("%02X", _reader.getPacketId()) + " <" + packet.getClass().getName() + ">");

                packet.execute(_reader, _client);
            }
        }

        throw new RuntimeException("Unknown packet : 0x" + String.format("%02X", _reader.getPacketId()) + " - state " + _client.getState());
    }
}
