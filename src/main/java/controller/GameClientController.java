package controller;

import com.google.inject.Provider;
import network.gameclient.GameClientChannelHandler;
import network.gameclient.GameClientConnectionState;
import network.gameclient.GameServerControllerInterface;
import network.gameclient.packets.IncomingGameClientPacketInterface;
import network.gameclient.packets.PacketReader;
import com.google.inject.Inject;
import controller.gameclient.AuthGameGuard;
import controller.gameclient.RequestAuthLogin;
import controller.gameclient.RequestGameServerLogin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class GameClientController implements GameServerControllerInterface {

    private static final Logger logger = LogManager.getLogger();

    private Map<GameClientConnectionState, Map<Integer, Provider<IncomingGameClientPacketInterface>>> controllers;


    @Inject
    public GameClientController(Map<Class, Provider<IncomingGameClientPacketInterface>> _controllers) {

        this.controllers = new HashMap<>();
        this.controllers.put(GameClientConnectionState.CONNECTED, new HashMap<>() {
            {
                put(0x07, _controllers.get(AuthGameGuard.class));
                put(0x00, _controllers.get(RequestAuthLogin.class));
            }
        });
        this.controllers.put(GameClientConnectionState.LOGGED_IN, new HashMap<>() {
            {
                put(0x02, _controllers.get(RequestGameServerLogin.class));
            }
        });

    }


    public IncomingGameClientPacketInterface handle(PacketReader _reader, GameClientChannelHandler _client) {
        final int packetId = _reader.readUnsignedByte() & 0xFF;

        Map<Integer, Provider<IncomingGameClientPacketInterface>> map = this.controllers.get(_client.getState());
        if (null == map) {
            logger.error("State not found" + _client.getState());
            return null;
        }

        Provider<IncomingGameClientPacketInterface> provider = map.get(packetId);
        if (null == provider) {
            logger.error("Provider not found : 0x" + String.format("%02X", packetId) + " - state " + _client.getState());
            return null;
        }

        IncomingGameClientPacketInterface packet = provider.get();
        if (null == packet) {
            logger.error("Provider not loaded : 0x" + String.format("%02X", packetId) + " - state " + _client.getState());
            return null;
        }

        logger.info("Packet found 0x" + String.format("%02X", packetId) + " <" + packet.getClass().getName() + ">");

        return packet;
    }
}
