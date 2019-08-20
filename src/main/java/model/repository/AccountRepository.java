package model.repository;


import com.google.inject.Inject;
import com.google.inject.Provider;
import model.entity.Account;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class AccountRepository {

    private Provider<EntityManager> provider;

    @Inject
    public AccountRepository(Provider<EntityManager> _provider) {
        this.provider = _provider;
    }

    public Account findOneByLogin(String _login) {
        TypedQuery<Account> query = this.provider.get().createQuery("SELECT a FROM Account a WHERE a.login = :login", Account.class);
        query.setParameter("login", _login);

        return query.getResultList().stream().findFirst().orElse(null);
    }

}
