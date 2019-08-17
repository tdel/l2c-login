package main;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import kernel.Kernel;
import kernel.KernelEnvironment;
import kernel.subsystem.AbstractKernelSubsystem;
import subsystem.network.NetworkSubsystem;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) throws Exception {

        Injector injector = Guice.createInjector(new JpaPersistModule("default"), new ServicesModule());

        List<AbstractKernelSubsystem> subsystems = new ArrayList<>();
        subsystems.add(injector.getInstance(NetworkSubsystem.class));

        Kernel kernel = injector.getInstance(Kernel.class);

        kernel.start(KernelEnvironment.DEV, injector, subsystems);
    }
}
