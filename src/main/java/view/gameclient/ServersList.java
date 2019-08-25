package view.gameclient;

import network.gameclient.packets.OutgoingGameClientPacketInterface;
import network.gameclient.packets.PacketWriter;
import model.entity.GameServer;

import java.net.InetSocketAddress;
import java.util.Collection;

public class ServersList implements OutgoingGameClientPacketInterface {

    private Collection<GameServer> gameservers;

    public ServersList(Collection<GameServer> _gameservers) {
        this.gameservers = _gameservers;
    }

    @Override
    public void write(PacketWriter _writer) {
        _writer.writeC(0x04);

        _writer.writeC(this.gameservers.size());
        _writer.writeC(1); // last id

        for (GameServer gs : this.gameservers) {
            InetSocketAddress address = gs.getAddress();

            String[] split = address.getHostString().split("\\.");

            _writer.writeC(gs.getId());
            for (String s : split) {
                _writer.writeC(Integer.parseInt(s));
            }
            _writer.writeD(address.getPort());

            _writer.writeC(gs.getMinAge());
            _writer.writeC(gs.isPKEnabled());
            _writer.writeH(gs.getPlayersCount());
            _writer.writeH(gs.getPlayersMaximum());
            _writer.writeC(gs.getStatus());
            _writer.writeD(gs.getTypesMask());
            _writer.writeC(0);
        }

        _writer.writeH(0); // unused

        // if char in servers : list them
        _writer.writeC(0);

    }

}
