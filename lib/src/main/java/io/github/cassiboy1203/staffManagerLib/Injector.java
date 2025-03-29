package io.github.cassiboy1203.staffManagerLib;

import io.github.cassiboy1203.staffManagerLib.annotations.*;
import io.github.cassiboy1203.staffManagerLib.annotations.command.MCCommand;
import io.github.cassiboy1203.staffManagerLib.annotations.config.Config;
import io.github.cassiboy1203.staffManagerLib.exceptions.BeanInstantiationException;
import io.github.cassiboy1203.staffManagerLib.exceptions.BeanNotFoundException;
import io.github.cassiboy1203.staffManagerLib.exceptions.ClassNotAnBeanException;
import io.github.cassiboy1203.staffManagerLib.factories.CommandFactory;
import io.github.cassiboy1203.staffManagerLib.factories.ConfigFactory;
import io.github.cassiboy1203.staffManagerLib.factories.ListenerFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.zip.ZipInputStream;

public class Injector {

    private final JavaPlugin plugin;
    private final ConfigFactory configFactory;
    private final ListenerFactory listenerFactory;
    private final CommandFactory commandFactory;

    private final Map<Class<?>, Set<Tuple<String, Class<?>>>> diMap;
    private final Set<Object> instances;
    private final Map<String, Object> configs = new HashMap<>();

    public Injector(JavaPlugin plugin) {
        this(plugin, null, null, null);
    }

    public Injector(JavaPlugin plugin, ConfigFactory configFactory, ListenerFactory listenerFactory, CommandFactory commandFactory) {
        this.plugin = plugin;
        this.configFactory = Objects.requireNonNullElseGet(configFactory, () -> new ConfigFactory(plugin, this));
        this.listenerFactory = Objects.requireNonNullElseGet(listenerFactory, () -> new ListenerFactory(plugin, this));
        this.commandFactory = Objects.requireNonNullElseGet(commandFactory, () -> new CommandFactory(plugin, this));
        diMap = new HashMap<>();
        instances = new HashSet<>();
    }

    protected void start() {
        var classes = getClasses(plugin);
        for (var clazz : classes){
            if (clazz.isAnnotationPresent(Plugin.class)){
                registerPlugin(clazz);
            } else if (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(MCListener.class) || clazz.isAnnotationPresent(MCCommand.class) || clazz.isAnnotationPresent(Config.class)){
                registerClass(clazz);
            }
        }

        for (var clazz : classes){
            if (clazz.isAnnotationPresent(MCCommand.class)){
                commandFactory.registerCommand(clazz);
            }
            if (clazz.isAnnotationPresent(MCListener.class)){
                listenerFactory.registerListener(clazz);
            }
            if (clazz.isAnnotationPresent(Config.class)){
                var config = configFactory.registerConfig(clazz);
                configs.put(clazz.getAnnotation(Config.class).value(), config);
            }
        }
    }

    private void registerPlugin(Class<?> clazz) {

        registerDi(clazz, clazz, clazz.getName());
        registerDi(JavaPlugin.class, clazz, clazz.getName());
        instances.add(Bukkit.getPluginManager().getPlugin(clazz.getSimpleName()));
    }

    private void registerClass(Class<?> clazz){
        var interfaces = clazz.getInterfaces();
        var superClass = clazz.getSuperclass();

        var name = clazz.getName();

        if (clazz.isAnnotationPresent(Qualifier.class)){
            name = clazz.getAnnotation(Qualifier.class).value();
        }

        registerDi(clazz, clazz, name);

        if (superClass != null){
            registerDi(superClass, clazz, name);
        }


        for (var iFace : interfaces){
            registerDi(iFace, clazz, name);
        }
    }

    public <T> T getInstance(Class<T> clazz, String name)
    {
        var dis = diMap.get(clazz);

        Class<?> instanceClass;

        if (dis.size() == 1){
            instanceClass = dis.stream().findFirst().get().getV();
        } else if (name != null){
            var tuple = dis.stream().filter(t -> t.getT().equals(name)).findFirst().orElse(null);
            if (tuple == null)
                throw new BeanNotFoundException();

            instanceClass = tuple.getV();
        } else {
            throw new BeanNotFoundException();
        }

        if (instanceClass.isAnnotationPresent(Singleton.class) || clazz.isAnnotationPresent(MCListener.class) || clazz.isAnnotationPresent(MCCommand.class) || clazz.isAnnotationPresent(Config.class)){
            var singletonInstance = instances.stream().filter(o -> o.getClass().equals(instanceClass)).findFirst().orElse(null);

            if (singletonInstance != null){
                return (T) singletonInstance;
            }
        }

        var constructors = instanceClass.getConstructors();
        var methods = instanceClass.getMethods();

        Constructor<?> constructor = getConstructor(constructors, instanceClass);

        var parameters = constructor.getParameters();
        var args = new Object[parameters.length];

        for (var i = 0; i < parameters.length; i++){
            var parameter = parameters[i];
            if (parameter.isAnnotationPresent(Qualifier.class)){
                var parameterName = parameter.getAnnotation(Qualifier.class).value();
                args[i] = getInstance(parameter.getType(), parameterName);
            } else {
                args[i] = getInstance(parameter.getType());
            }
        }

        try {
            var instance = (T) constructor.newInstance(args);

            for (var method : methods){
                if (!method.isAnnotationPresent(Inject.class))
                    continue;

                var methodParameters = method.getParameters();
                var methodArgs = new Object[methodParameters.length];

                for (var i = 0; i < methodParameters.length; i++){
                    var parameter = methodParameters[i];
                    if (parameter.isAnnotationPresent(Qualifier.class)){
                        var parameterName = parameter.getAnnotation(Qualifier.class).value();
                        methodArgs[i] = getInstance(parameter.getClass(), parameterName);
                    } else {
                        methodArgs[i] = getInstance(parameter.getClass());
                    }
                }

                method.invoke(instance, methodArgs);
            }

            if (instanceClass.isAnnotationPresent(Singleton.class) || clazz.isAnnotationPresent(MCListener.class) || clazz.isAnnotationPresent(MCCommand.class) || clazz.isAnnotationPresent(Config.class)){
                instances.add(instance);
            }

            return instance;

        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new BeanInstantiationException(instanceClass.getName());
        }
    }

    private Constructor<?> getConstructor(Constructor<?>[] constructors, Class<?> instanceClass) {
        Constructor<?> constructor = null;

        for (var _constructor : constructors){
            if (!_constructor.isAnnotationPresent(Inject.class))
                continue;

            constructor = _constructor;
            break;
        }

        if (constructor == null) {
            try {
                constructor = instanceClass.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new ClassNotAnBeanException(instanceClass.getName());
            }
        }
        return constructor;
    }

    public <T> T getInstance(Class<T> clazz){
        return getInstance(clazz, null);
    }

    private void registerDi(Class<?> superClass, Class<?> clazz, String name) {
        if (superClass.equals(Object.class))
            return;

        Set<Tuple<String, Class<?>>> set;
        if (!diMap.containsKey(superClass)){
            set = new HashSet<>();
        } else {
            set = diMap.get(superClass);
        }


        set.add(new Tuple<>(name, clazz));
        diMap.put(superClass, set);
    }

    private List<Class<?>> getClasses(Object mainClass){
        var classes = new ArrayList<Class<?>>();

        ZipInputStream stream;
        try {
            stream = new ZipInputStream(new FileInputStream(mainClass.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()));


            for (var entry = stream.getNextEntry(); entry != null; entry = stream.getNextEntry()){
                if (entry.getName().endsWith(".class")) {
                    classes.add(Class.forName(entry.getName().substring(0, entry.getName().length() - 6).replaceAll("/", ".")));
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return classes;
    }
}
