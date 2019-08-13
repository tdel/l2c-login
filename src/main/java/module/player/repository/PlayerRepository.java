package module.player.repository;

import module.player.model.Player;

public class PlayerRepository {

    public Player findOnePlayerByLogin(String _login) {
        return new Player(1, _login, "password");
    }

    public boolean isBanned(Player _player) {
        return false;
    }

}
