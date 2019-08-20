package model.service.playerlogin;

import com.google.inject.Inject;
import model.entity.Account;
import model.repository.AccountRepository;
import model.service.playerlogin.result.PlayerLoginReason;
import model.service.playerlogin.result.PlayerLoginResult;
import app.kernel.subsystem.network.gameclient.security.PasswordSecurity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class PlayerLoginService {

    private PasswordSecurity passwordSecurity;
    private AccountRepository accountRepository;

    @Inject
    public PlayerLoginService(PasswordSecurity _passwordSecurity, AccountRepository _accountRepository) {
        this.passwordSecurity = _passwordSecurity;
        this.accountRepository = _accountRepository;
    }

    public PlayerLoginResult tryLogin(String _login, String _password) {
        Account account = this.accountRepository.findOneByLogin(_login);

        if (null == account) {
            return this.createLoginFailResult(PlayerLoginReason.NOT_FOUND);
        }

        String password = this.passwordSecurity.encode(_password);
        if (!account.isPasswordEquals(password)) {
            return this.createLoginFailResult(PlayerLoginReason.INVALID_PASSWORD);
        }

        if (account.isBanned()) {
            return this.createLoginFailResult(PlayerLoginReason.BANNED);
        }

        return PlayerLoginResult.LoginSuccess(account);
    }

    private PlayerLoginResult createLoginFailResult(PlayerLoginReason _reason) {
        return PlayerLoginResult.LoginFail(_reason);
    }
}
