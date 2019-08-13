package module.player.service.playerlogin;

public class PlayerLoginResult {

    private boolean result;
    private PlayerLoginReason reason;

    private PlayerLoginResult(boolean _result, PlayerLoginReason _reason) {
        this.result = _result;
        this.reason = _reason;
    }

    static public PlayerLoginResult LoginSuccess() {
        return new PlayerLoginResult(true, null);
    }
    static public PlayerLoginResult LoginFail(PlayerLoginReason _reason) {
        return new PlayerLoginResult(false, _reason);
    }

    public boolean isSuccess() {
        return this.result;
    }

    public PlayerLoginReason getReason() {
        return this.reason;
    }

}
