package service.playerlogin;

import com.google.inject.Inject;
import game.model.entity.Account;
import game.model.repository.AccountRepository;
import network.gameclient.security.PasswordSecurity;
import network.server.gameclient.GCClient;
import network.server.gameclient.GCClientState;
import service.ServiceOperationResult;

import java.util.ArrayList;
import java.util.List;

public class PlayerLoginService {

    public enum LoginReason {
        NOT_FOUND, INVALID_PASSWORD, BANNED,
    }

    private final PasswordSecurity passwordSecurity;
    private final AccountRepository accountRepository;

    private final List<GCClient> waitingAccounts;

    @Inject
    public PlayerLoginService(PasswordSecurity _passwordSecurity, AccountRepository _accountRepository) {
        this.passwordSecurity = _passwordSecurity;
        this.accountRepository = _accountRepository;
        this.waitingAccounts = new ArrayList<>();
    }

    public int putAccountAsLoggedIn(GCClient _account) {
        _account.setState(GCClientState.LOGGED_IN);

        int keys = _account.generateWaitingKeys();
        this.waitingAccounts.add(_account);

        return keys;
    }

    public ServiceOperationResult<Account> checkCredentials(String _login, String _password) {
        Account account = this.accountRepository.findOneByLogin(_login);

        if (null == account) {
            return ServiceOperationResult.failAsEnum(LoginReason.NOT_FOUND);
        }

        String password = this.passwordSecurity.encode(_password);
        if (!account.isPasswordEquals(password)) {
            return ServiceOperationResult.failAsEnum(LoginReason.INVALID_PASSWORD);
        }

        if (account.isBanned()) {
            return ServiceOperationResult.failAsEnum(LoginReason.BANNED);
        }

        return ServiceOperationResult.success(account);
    }

}
