package main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import kernel.Kernel;
import kernel.KernelEnvironment;
import kernel.subsystem.AbstractKernelSubsystem;
import main.module.*;
import subsystem.network.NetworkSubsystem;
import subsystem.persistence.PersistenceSubsystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Main {

    public static void main(String[] args) throws Exception {

        KernelModule kernelModule = new KernelModule();
        Injector injector = Guice.createInjector(
                new JpaPersistModule("default"),
                kernelModule,
                new NetworkSubsystemModule(),
                new ControllerModule(),
                new ServiceModule()
        );

        Set<Class<? extends AbstractKernelSubsystem>> subsystems = kernelModule.getSubsystems();

        Kernel kernel = injector.getInstance(Kernel.class);
        kernel.start(KernelEnvironment.DEV, injector, subsystems);
    }
}
