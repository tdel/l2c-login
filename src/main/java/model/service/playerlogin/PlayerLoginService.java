package model.service.playerlogin;

import com.google.inject.Inject;
import model.entity.Account;
import model.repository.AccountRepository;
import model.service.playerlogin.result.PlayerLoginReason;
import model.service.playerlogin.result.PlayerLoginResult;
import network.gameclient.GameClientChannelHandler;
import network.gameclient.security.PasswordSecurity;

import java.util.ArrayList;
import java.util.List;

public class PlayerLoginService {

    private PasswordSecurity passwordSecurity;
    private AccountRepository accountRepository;

    private List<GameClientChannelHandler> waitingAccounts;

    @Inject
    public PlayerLoginService(PasswordSecurity _passwordSecurity, AccountRepository _accountRepository) {
        this.passwordSecurity = _passwordSecurity;
        this.accountRepository = _accountRepository;
        this.waitingAccounts = new ArrayList<>();
    }

    public int addWaitingAccount(GameClientChannelHandler _account) {
        int keys = _account.generateWaitingKeys();
        this.waitingAccounts.add(_account);

        return keys;
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
