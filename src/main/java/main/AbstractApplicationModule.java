package main;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import kernel.KernelServiceInterface;

abstract public class AbstractApplicationModule extends AbstractModule {


    protected AbstractApplicationModule bindToKernel(Class<? extends KernelServiceInterface> _class) {
        Multibinder<KernelServiceInterface> binder = Multibinder.newSetBinder(binder(), KernelServiceInterface.class);
        binder.addBinding().to(_class);

        return this;
    }

    protected <V> AbstractApplicationModule bindToMap(Class<V> _valueMap, Class<? extends V> _value) {
        MapBinder<Class, V> mapbinder = MapBinder.newMapBinder(binder(), Class.class, _valueMap);
        mapbinder.addBinding(_value).to(_value);

        return this;
    }
}
