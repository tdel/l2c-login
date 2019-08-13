package module.client.model;

public class Player {

    private int id;
    private String login;
    private String password;

    public Player() {

    }

    public Player(int _id, String _login, String _password) {
        this.id = _id;
        this.login = _login;
        this.password = _password;
    }


    public boolean isPasswordEquals(String _password) {
        return this.password.equals(_password);
    }

}
