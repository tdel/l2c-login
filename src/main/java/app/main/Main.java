package app.main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import app.kernel.Kernel;
import app.kernel.KernelEnvironment;
import app.kernel.subsystem.AbstractKernelSubsystem;
import app.main.module.*;
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
