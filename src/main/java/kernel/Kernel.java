package kernel;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import model.Account;
import module.client.ClientModule;
import module.client.security.BlowfishGenerator;
import module.gameserver.GameServerModule;
import model.GameServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

public class Kernel {

    private static final Logger logger = LogManager.getLogger();

    private KernelEnvironment env;
    private KernelStatus status;

    private Map<String, Object> configuration;

    private Map<Class, AbstractKernelModule> modules;
    private Map<Class, Object> services;

    private Injector injector;

    @Inject
    public Kernel() {
        this.status = KernelStatus.STOPED;
        this.configuration = new HashMap<>();
        this.modules = new HashMap<>();
        this.services = new HashMap<>();
    }

    private void setStatus(KernelStatus _status) {
        logger.info("Switch state from " + this.status + " to " + _status);
        this.status = _status;
    }

    public void start(KernelEnvironment _env, Injector _injector) throws Exception {
        if (this.status != KernelStatus.STOPED) {
            throw new IllegalStateException("Kernel must be stopped to be started !");
        }
        this.setStatus(KernelStatus.STARTING);
        this.env = _env;
        this.injector = _injector;

        this.injector.getInstance(PersistService.class).start();


        this.loadConfiguration();
        this.loadModules();

        this.modules.forEach((k,v) -> { try { v.start(); } catch (Exception e) {
           e.printStackTrace();
        } });

        this.setStatus(KernelStatus.RUNNING);
    }

    public void stop() {
        if (this.status != KernelStatus.RUNNING) {
            throw new IllegalStateException("Kernel must be running to be stopped");
        }

        this.setStatus(KernelStatus.STOPING);

        this.modules.forEach((k,v) -> v.stop());

        this.setStatus(KernelStatus.STOPED);
    }


    private void loadConfiguration() {
        this.configuration.put("kernel.environment", this.env);
        this.configuration.put("module.player.server.port", 2106);
    }

    public <T> T getConfigParam(String _name) {
        if (!this.configuration.containsKey(_name)) {
            throw new IllegalArgumentException("Parameter "+ _name + " does not exist");
        }

        return (T) this.configuration.get(_name);
    }


    private void loadModules() {
        this.modules.put(GameServerModule.class, this.injector.getInstance(GameServerModule.class));
        this.modules.put(ClientModule.class, this.injector.getInstance(ClientModule.class));
    }

    public <T> T getService(Class<T> _class) {
        return this.injector.getInstance(_class);
    }

    public void registerService(Object _object) {
        this.registerService(_object.getClass(), _object);
    }

    public void registerService(Class _class) {
        this.services.put(_class, this.injector.getInstance(_class));
    }

    public void registerService(Class _class, Object _object) {
        this.services.put(_class, _object);
        logger.info("Registered service : " + _object.getClass());
    }
}
