/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.componenthandler;

import pw.valaria.aperture.components.AbstractHandler;
import pw.valaria.aperture.components.AbstractHandler;

public class ComponentHolder<T extends AbstractHandler> {

    private T component;

    ComponentHolder(T component) {
        this.component = component;
    }

    public T getComponent() {
        return component;
    }
}
