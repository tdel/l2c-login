import com.google.inject.Guice;
import com.google.inject.Injector;
import kernel.Kernel;
import kernel.KernelEnvironment;
import kernel.KernelGuiceModule;

public class Main {
    public static void main(String[] args) throws Exception {

        Injector injector = Guice.createInjector(new KernelGuiceModule());
        Kernel kernel = injector.getInstance(Kernel.class);

        kernel.start(KernelEnvironment.DEV, injector);
    }
}
