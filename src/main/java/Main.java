import com.google.inject.Stage;
import configuration.ConfigGuiceModule;
import database.DatabaseGuiceModule;
import kernel.KernelGuiceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import kernel.Kernel;
import controller.ControllerGuiceModule;
import model.service.ServiceModule;
import network.NetworkGuiceModule;

public class Main {

    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT,
                new KernelGuiceModule(),
                new ConfigGuiceModule("src/main/resources/app.properties"),
                new DatabaseGuiceModule(),
                new NetworkGuiceModule(),
                new ServiceModule(),
                new ControllerGuiceModule()
        );

        Kernel kernel = injector.getInstance(Kernel.class);
        kernel.boot(injector);
    }
}
