package module.client.service;

import model.GameServer;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class GameServers {


    private EntityManager em;

    public GameServers(EntityManager _em) {
        this.em = _em;
    }

    public List<GameServer> getLinkedGameServers() {

        TypedQuery<GameServer> query = this.em.createQuery("SELECT gs FROM GameServer gs", GameServer.class);
        List<GameServer> list = query.getResultList();

        return list;
        /*GameServer gs = new GameServer(
                1,
                "first",
                InetSocketAddress.createUnresolved("127.0.0.1", 7777),
                10,
                18,
                new HashSet<>(Arrays.asList(GameServerType.FREE))
        );
        gs.setOnline();*/

        /*
        list.add(gs);

        return list;*/
    }
}
