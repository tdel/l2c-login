package subsystem.gameclient.service.playerlogin;

import model.Account;

public class PlayerLoginResult {

    private boolean result;
    private PlayerLoginReason reason;
    private Account account;

    private PlayerLoginResult(PlayerLoginReason _reason) {
        this.result = false;
        this.reason = _reason;
    }

    private PlayerLoginResult(Account _account) {
        this.result = true;
        this.account = _account;
    }

    static public PlayerLoginResult LoginSuccess(Account _account) {
        return new PlayerLoginResult(_account);
    }
    static public PlayerLoginResult LoginFail(PlayerLoginReason _reason) {
        return new PlayerLoginResult(_reason);
    }

    public boolean isSuccess() {
        return this.result;
    }

    public PlayerLoginReason getReason() {
        return this.reason;
    }

    public Account getAccount() {
        return this.account;
    }

}
