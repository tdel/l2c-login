import kernel.Kernel;
import kernel.KernelEnvironment;

public class Main {
    public static void main(String[] args) throws Exception {
        Kernel kernel = new Kernel(KernelEnvironment.DEV);
        kernel.start();
    }
}
