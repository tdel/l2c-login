package module.client.service.playerlogin;

import model.Account;
import module.client.security.PasswordSecurity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class PlayerLoginService {

    private PasswordSecurity passwordSecurity;
    private EntityManager em;

    public PlayerLoginService(PasswordSecurity _passwordSecurity, EntityManager _em) {
        this.passwordSecurity = _passwordSecurity;
        this.em = _em;
    }

    public PlayerLoginResult tryLogin(String _login, String _password) {
        TypedQuery<Account> query = this.em.createQuery("SELECT a FROM Account a WHERE a.login = :login", Account.class);
        query.setParameter("login", _login);
        Account account = query.getResultList().stream().findFirst().orElse(null);

        if (null == account) {
            return createLoginFailResult(PlayerLoginReason.NOT_FOUND);
        }

        this.em.detach(account);

        String password = this.passwordSecurity.encode(_password);
        if (!account.isPasswordEquals(password)) {
            return createLoginFailResult(PlayerLoginReason.INVALID_PASSWORD);
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
