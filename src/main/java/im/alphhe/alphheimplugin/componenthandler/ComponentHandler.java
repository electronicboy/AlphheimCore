/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.componenthandler;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import im.alphhe.alphheimplugin.EladriaCore;
import im.alphhe.alphheimplugin.componenthandler.annotations.Required;
import im.alphhe.alphheimplugin.components.AbstractHandler;
import im.alphhe.alphheimplugin.components.permissions.PermissionHandler;
import kotlin.reflect.KClass;

public class ComponentHandler {
    private Map<Class<? extends AbstractHandler>, ComponentHolder<? extends AbstractHandler>> component = new HashMap<>();
    private EladriaCore plugin;


    public ComponentHandler(EladriaCore plugin) {
        this.plugin = plugin;
    }


    public <T extends AbstractHandler> T registerComponent(@Nonnull Class<T> handlerClass) {
        if (component.get(handlerClass) != null)
            throw new IllegalArgumentException(handlerClass.getCanonicalName() + " has already been registered!");

        boolean isRequired = handlerClass.getAnnotation(Required.class) != null;


        try {
            Constructor<T> constructor = handlerClass.getConstructor(EladriaCore.class);

            T t = constructor.newInstance(plugin);
            component.put(handlerClass, new ComponentHolder<>(t));
            plugin.getLogger().info("Registered component: " + handlerClass.getCanonicalName());
            return t;

        } catch (Exception e) {
            plugin.getLogger().warning("Failed to register component: " + handlerClass.getCanonicalName());
            if (isRequired) {
                plugin.safeLockdown();
                throw new IllegalStateException("Missing dependencies", e);
            } else {
                return null;
            }
        }
    }

    @SuppressWarnings("unchecked")
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

    @NotNull
    public <T extends AbstractHandler> T getComponentOrThrow(@NotNull Class<T> java) {
        final T component = getComponent(java);
        if (component == null) {
            throw new IllegalStateException(java.getName() + " is not loaded, bailing!");
        }
        return component;
    }
}
