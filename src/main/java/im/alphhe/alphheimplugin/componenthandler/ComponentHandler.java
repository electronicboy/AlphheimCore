/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.componenthandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import im.alphhe.alphheimplugin.AlphheimCore;
import im.alphhe.alphheimplugin.components.AbstractHandler;

public class ComponentHandler {
    private Map<Class<? extends AbstractHandler>, ComponentHolder<? extends AbstractHandler>> component = new HashMap<>();
    private AlphheimCore plugin;


    public ComponentHandler(AlphheimCore plugin) {
        this.plugin = plugin;
    }


    public <T extends AbstractHandler> T registerComponent(@Nonnull Class<T> handlerClass) {
        if (component.get(handlerClass) != null)
            throw new IllegalArgumentException(handlerClass.getCanonicalName() + " has already been registered!");

        try {
            Constructor<T> constructor = handlerClass.getConstructor(AlphheimCore.class);

            T t = constructor.newInstance(plugin);
            component.put(handlerClass, new ComponentHolder<>(t));
            plugin.getLogger().info("Registered component: " + handlerClass.getCanonicalName());
            return t;

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            plugin.getLogger().warning("Failed to register component: " + handlerClass.getCanonicalName());
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public <T extends AbstractHandler> T getComponent(@Nonnull Class<T> handlerClass) {
        ComponentHolder<? extends AbstractHandler> componentHolder = component.get(handlerClass);
        if (componentHolder != null) {
            return (T) componentHolder.getComponent();
        }
        return null;

    }

    public void disable() {

    }
}
