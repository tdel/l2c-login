package kernel.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kernel.Kernel;
import kernel.KernelServiceInterface;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseService implements KernelServiceInterface {

    private HikariDataSource dataSource;

    @Override
    public void onBoot(Kernel _kernel) {
        HikariConfig config = new HikariConfig("datasource.properties");
        this.dataSource = new HikariDataSource(config);
    }

    @Override
    public void onHalt(Kernel _kernel) {

    }

    public Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
