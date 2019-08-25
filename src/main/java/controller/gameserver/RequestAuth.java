package controller.gameserver;

import com.google.inject.Inject;
import model.entity.GameServer;
import model.repository.GameServerRepository;
import network.gameserver.GameServerChannelHandler;
import network.gameserver.packets.IncomingGameServerPacketInterface;
import network.gameserver.packets.PacketReader;
import view.gameserver.AuthResult;

public class RequestAuth implements IncomingGameServerPacketInterface {

    private GameServerRepository gsRepository;

    @Inject
    public RequestAuth(GameServerRepository _gsRepository) {
        this.gsRepository = _gsRepository;
    }

    @Override
    public void execute(PacketReader _reader, GameServerChannelHandler _client) {
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
