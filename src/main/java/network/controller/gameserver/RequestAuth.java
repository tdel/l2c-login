package network.controller.gameserver;

import com.google.inject.Inject;
import game.model.entity.GameServer;
import game.model.repository.GameServerRepository;
import network.gameserver.packets.IncomingGameServerPacketInterface;
import network.gameserver.packets.PacketReader;
import network.response.gameserver.AuthResult;
import network.server.gameserver.GSClient;

public class RequestAuth implements IncomingGameServerPacketInterface {

    private final GameServerRepository gsRepository;

    @Inject
    public RequestAuth(GameServerRepository _gsRepository) {
        this.gsRepository = _gsRepository;
    }

    @Override
    public boolean supports(PacketReader _reader) {
        return _reader.getCode() != null;
    }

    @Override
    public void execute(PacketReader _reader, GSClient _client) {
        String key = _reader.get("key");

        GameServer gs = this.gsRepository.findOneByKey(key);
        if (null == gs) {
            _client.sendPacket(AuthResult.createFail("Key not found"));

            return;
        }

        gs.attachHandler(_client);

        _client.sendPacket(AuthResult.createSuccess());
    }
}
