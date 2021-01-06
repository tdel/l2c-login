package game.network.controller;


import com.google.inject.Inject;
import kernel.network.gameserver.GameServerChannelHandler;
import kernel.network.gameserver.packets.IncomingGameServerPacketInterface;
import kernel.network.gameserver.packets.PacketReader;

import java.util.Set;

public class GameServerController {

    private final Set<IncomingGameServerPacketInterface> controllers;

    @Inject
    public GameServerController(Set<IncomingGameServerPacketInterface> _controllers) {
        this.controllers = _controllers;
    }

    public void handle(PacketReader _reader, GameServerChannelHandler _handler) {

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
