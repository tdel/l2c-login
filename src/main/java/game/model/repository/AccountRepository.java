package game.model.repository;

import database.DatabaseLoadableKernel;
import game.model.entity.Account;

import javax.inject.Inject;

public class AccountRepository {

    private final DatabaseLoadableKernel database;

    @Inject
    public AccountRepository(DatabaseLoadableKernel database) {
        this.database = database;
    }

    public Account findOneByLogin(String _login) {

        this.database.getConnection();
        return new Account(1, _login);
    }

}
