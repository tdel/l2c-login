package module.player.service.playerlogin;

import module.player.model.Player;
import module.player.repository.PlayerRepository;
import module.player.security.PasswordSecurity;

public class PlayerLoginService {

    private PasswordSecurity passwordSecurity;
    private PlayerRepository repository;

    public PlayerLoginService(PasswordSecurity _passwordSecurity, PlayerRepository _repository) {
        this.passwordSecurity = _passwordSecurity;
        this.repository = _repository;
    }

    public PlayerLoginResult tryLogin(String _login, String _password) {
        Player player = this.repository.findOnePlayerByLogin(_login);

        if (null == player) {
            return createLoginFailResult(PlayerLoginReason.NOT_FOUND);
        }

        String password = this.passwordSecurity.encode(_password);
        if (false == player.isPasswordEquals(password)) {
            return createLoginFailResult(PlayerLoginReason.INVALID_PASSWORD);
        }

        if (this.repository.isBanned(player)) {
            return this.createLoginFailResult(PlayerLoginReason.BANNED);
        }

        return PlayerLoginResult.LoginSuccess();
    }

    private PlayerLoginResult createLoginFailResult(PlayerLoginReason _reason) {
        return PlayerLoginResult.LoginFail(_reason);
    }
}
