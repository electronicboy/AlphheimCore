/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.componenthandler;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pw.valaria.aperture.ApertureCore;
import pw.valaria.aperture.componenthandler.annotations.Required;
import pw.valaria.aperture.components.AbstractHandler;
import pw.valaria.aperture.ApertureCore;
import pw.valaria.aperture.componenthandler.annotations.Required;
import pw.valaria.aperture.components.AbstractHandler;

public class ComponentHandler {
    private Map<Class<? extends AbstractHandler>, ComponentHolder<? extends AbstractHandler>> component = new HashMap<>();
    private ApertureCore plugin;


    public ComponentHandler(ApertureCore plugin) {
        this.plugin = plugin;
    }


    public <T extends AbstractHandler> T registerComponent(@Nonnull Class<T> handlerClass) {
        if (component.get(handlerClass) != null)
            throw new IllegalArgumentException(handlerClass.getCanonicalName() + " has already been registered!");

        boolean isRequired = handlerClass.getAnnotation(Required.class) != null;


        try {
            Constructor<T> constructor = handlerClass.getConstructor(ApertureCore.class);

            T t = constructor.newInstance(plugin);
            component.put(handlerClass, new ComponentHolder<>(t));
            plugin.getLogger().info("Registered component: " + handlerClass.getCanonicalName());
            return t;

        } catch (Exception e) {
            plugin.getLogger().warning("Failed to register component: " + handlerClass.getCanonicalName());
            e.printStackTrace();
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
