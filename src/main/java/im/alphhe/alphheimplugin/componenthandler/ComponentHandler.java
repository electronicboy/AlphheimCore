/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.componenthandler;

import java.util.Map;

import im.alphhe.alphheimplugin.components.AbstractHandler;

public class ComponentHandler<T extends AbstractHandler> {
    private Map<Class<T>, T> registered;


}
