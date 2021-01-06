package game.model.entity;

public class Account {

    private int id;

    private String login;

    private String password;

    public Account() {

    }
    public Account(int _id, String _login) {
        this.id = _id;
        this.login = _login;
    }

    public boolean isPasswordEquals(String _password) {
        return this.password.equals(_password);
    }

    public boolean isBanned() {
        return false;
    }

}
