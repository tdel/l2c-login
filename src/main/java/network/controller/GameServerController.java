package network.controller;


import com.google.inject.Inject;
import network.gameserver.packets.IncomingGameServerPacketInterface;
import network.gameserver.packets.PacketReader;
import network.response.gameserver.InitPacket;
import network.server.gameserver.GSClient;

import java.util.Set;

public class GameServerController {

    private final Set<IncomingGameServerPacketInterface> controllers;

    @Inject
    public GameServerController(Set<IncomingGameServerPacketInterface> _controllers) {
        this.controllers = _controllers;
    }

    public void active(GSClient _handler) {
        _handler.sendPacket(new InitPacket());
    }

    public void handle(PacketReader _reader, GSClient _handler) {

        String code = _reader.getCode();
        if (null == code) {
            return;
        }

        for (IncomingGameServerPacketInterface packet : this.controllers) {
            if (packet.supports(_reader)) {
                packet.execute(_reader, _handler);
            }
        }

    }
}
