package ru.otus.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import static java.util.Comparator.comparingInt;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        List<Method> methods = Arrays.stream(configClass.getMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .toList();

        methods.forEach(method -> {
            Object[] args = Arrays.stream(method.getParameterTypes())
                    .map(this::getAppComponent)
                    .toArray();

            Object appComponent;
            try {
                appComponent = method.invoke(configClass.getDeclaredConstructor().newInstance(), args);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
                throw new RuntimeException(e);
            }
            String appComponentName = method.getAnnotation(AppComponent.class).name();
            if (appComponentsByName.containsKey(appComponentName)) {
                throw new RuntimeException("Duplicate component was found.");
            }

            appComponents.add(appComponent);
            appComponentsByName.put(appComponentName, appComponent);
        });
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> components = appComponents.stream()
                .filter(appComponent -> componentClass.isAssignableFrom(appComponent.getClass()))
                .toList();
        if (components.size() != 1) {
            throw new RuntimeException("Invalid count of components.");
        }
        return (C) components.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object currAppComponents = appComponentsByName.get(componentName);
        if (currAppComponents == null) {
            throw new RuntimeException("Failed while getting components.");
        }
        return (C) currAppComponents;
    }
}
