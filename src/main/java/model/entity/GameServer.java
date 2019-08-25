package model.entity;

import network.gameserver.GameServerChannelHandler;
import network.gameserver.packets.OutgoingGameServerPacketInterface;

import javax.persistence.*;
import java.net.InetSocketAddress;

@Entity
@Table(name = "gameserver")
public class GameServer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String host;

    @Column
    private int port;

    @Transient
    private int playersCount;

    @Column
    private int playersMaximum;

    @Column
    private int minAge;

    @Column
    private int status;

    @Column
    private int types;

    @Column(name="server_key")
    private String serverKey;

    @Transient
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
