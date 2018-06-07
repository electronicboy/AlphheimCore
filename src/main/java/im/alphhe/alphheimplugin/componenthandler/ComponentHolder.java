/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.componenthandler;

import im.alphhe.alphheimplugin.components.AbstractHandler;

public class ComponentHolder<T extends AbstractHandler> {

    private T component;

    ComponentHolder(T component) {
        this.component = component;
    }

    public T getComponent() {
        return component;
    }
}
