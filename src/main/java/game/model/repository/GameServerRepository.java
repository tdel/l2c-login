package game.model.repository;

import com.google.inject.Inject;
import game.model.entity.GameServer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameServerRepository implements LoadableRepositoryInterface {

    private Map<Integer, GameServer> gameservers;

    @Inject
    public GameServerRepository() {
        this.gameservers = new HashMap<>();
    }

    @Override
    public void preload() {
        this.gameservers = new HashMap<>();
        /*
        TypedQuery<GameServer> query = this.em.createQuery("SELECT gs FROM GameServer gs", GameServer.class);

        List<GameServer> list = query.getResultList();
        for (GameServer gs : list) {
            this.gameservers.put(gs.getId(), gs);
        }

         */
    }

    public Collection<GameServer> getAll() {
        return this.gameservers.values();
    }

    public GameServer findOneByKey(String _key) {
        for (GameServer gs : this.gameservers.values()) {
            if (gs.getServerKey().equals(_key)) {
                return gs;
            }
        }

        return null;
    }

}
