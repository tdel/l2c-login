package controller;


import com.google.inject.Inject;
import com.google.inject.Provider;
import controller.gameserver.RequestAuth;
import network.gameserver.GameServerChannelHandler;
import network.gameserver.packets.IncomingGameServerPacketInterface;
import network.gameserver.packets.PacketReader;

import java.util.HashMap;
import java.util.Map;

public class GameServerController {

    private Map<String, Provider<IncomingGameServerPacketInterface>> controllers;

    @Inject
    public GameServerController(Map<Class, Provider<IncomingGameServerPacketInterface>> _controllers) {
        this.controllers = new HashMap<>();
        this.controllers.put("auth", _controllers.get(RequestAuth.class));
    }

    public void handle(PacketReader _reader, GameServerChannelHandler _handler) {

        String code = _reader.getCode();
        if (null == code) {
            return;
        }

        Provider<IncomingGameServerPacketInterface> controller = this.controllers.get(code);

        controller.get().execute(_reader, _handler);

    }
}
