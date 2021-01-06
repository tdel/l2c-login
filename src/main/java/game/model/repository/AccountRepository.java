package game.model.repository;

import kernel.database.DatabaseService;
import game.model.entity.Account;

import javax.inject.Inject;

public class AccountRepository {

    private final DatabaseService database;

    @Inject
    public AccountRepository(DatabaseService database) {
        this.database = database;
    }

    public Account findOneByLogin(String _login) {

        this.database.getConnection();
        return new Account(1, _login);
    }

}
