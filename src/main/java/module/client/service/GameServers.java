package module.client.service;

import com.google.inject.Inject;
import model.GameServer;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class GameServers {

    private EntityManager em;

    @Inject
    public GameServers(EntityManager _em) {
        this.em = _em;
    }

    public List<GameServer> getLinkedGameServers() {
        TypedQuery<GameServer> query = this.em.createQuery("SELECT gs FROM GameServer gs", GameServer.class);
        List<GameServer> list = query.getResultList();

        return list;
    }
}
