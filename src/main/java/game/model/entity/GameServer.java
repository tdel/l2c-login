package game.model.entity;

import kernel.network.gameserver.GameServerChannelHandler;
import kernel.network.gameserver.packets.OutgoingGameServerPacketInterface;

import java.net.InetSocketAddress;


public class GameServer {

    private int id;

    private String name;

    private String host;

    private int port;

    private int playersCount;

    private int playersMaximum;

    private int minAge;

    private int status;

    private int types;

    private String serverKey;

    private GameServerChannelHandler handler;

    public GameServer() {

    }



    public int getId() {
        return this.id;
    }

    public InetSocketAddress getAddress() {
        return InetSocketAddress.createUnresolved(this.host, this.port);
    }

    public int getPlayersMaximum() {
        return this.playersMaximum;
    }

    public int getTypesMask() {
        return this.types;
        //return this.types.stream().mapToInt(GameServerType::getMask).reduce((r, e) -> r | e).orElse(0);
    }

    public int getPlayersCount() {
        return 1;
    }

    public int getMinAge() {
        return this.minAge;
    }

    public int getStatus() {
        return this.status;
    }

    public int isPKEnabled() {
        return 1;
    }


    public String getServerKey() {
        return this.serverKey;
    }

    public void attachHandler(GameServerChannelHandler _handler) {
        this.handler = _handler;
        this.status = 1;
    }

    public void detachHandler() {
        this.handler = null;
        this.status = 0;
    }

    public void sendPacket(OutgoingGameServerPacketInterface _packet) {
        if (this.handler == null) {
            return;
        }

        this.handler.sendPacket(_packet);
    }

}
