package kernel;

import com.google.inject.Inject;

import java.util.Set;

public class KernelInit {

    private Set<LoadableKernelServiceInterface> services;

    @Inject
    public KernelInit(Set<LoadableKernelServiceInterface> _services) {
        this.services = _services;
    }

    public void start(Kernel _kernel) throws Exception {
        for (LoadableKernelServiceInterface service : this.services) {
            service.register(_kernel);
        }
    }
}
