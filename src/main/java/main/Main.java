package main;

import com.google.inject.Stage;
import kernel.guice.KernelGuice;
import com.google.inject.Guice;
import com.google.inject.Injector;
import kernel.Kernel;
import game.guice.GameGuice;

public class Main {

    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT,
                new KernelGuice("src/main/resources/app.properties"),
                new GameGuice()
        );

        Kernel kernel = injector.getInstance(Kernel.class);
        kernel.boot();
    }
}
