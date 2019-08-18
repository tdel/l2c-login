package model.entity;

import javax.persistence.*;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String login;

    @Column
    private String password;

    public Account() {

    }

    public boolean isPasswordEquals(String _password) {
        return this.password.equals(_password);
    }

    public boolean isBanned() {
        return false;
    }

}
