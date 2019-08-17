package main.module;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import kernel.Kernel;
import kernel.subsystem.AbstractKernelSubsystem;
import subsystem.network.NetworkSubsystem;
import subsystem.persistence.PersistenceSubsystem;

import java.util.HashSet;
import java.util.Set;

public class KernelModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(Kernel.class).in(Singleton.class);

        this.bind(PersistenceSubsystem.class).in(Singleton.class);
        this.bind(NetworkSubsystem.class).in(Singleton.class);
    }

    public Set<Class<? extends AbstractKernelSubsystem>> getSubsystems() {
        Set<Class<? extends AbstractKernelSubsystem>> set = new HashSet<>();

        set.add(PersistenceSubsystem.class);
        set.add(NetworkSubsystem.class);

        return set;
    }
}
