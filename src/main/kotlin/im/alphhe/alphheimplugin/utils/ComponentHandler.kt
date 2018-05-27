/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.utils

import com.google.common.base.Preconditions
import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.AbstractHandler
import pw.alphheim.api.services.InternalService
import pw.alphheim.api.services.InternalServiceHolder
import kotlin.reflect.KClass

class ComponentHandler<H : KClass<I>, I>(val plugin: AlphheimCore) where I: AbstractHandler {

    private val components: HashMap<H, ComponentHolder<I>> = HashMap()


    fun registerComponent(component: H): I {
        val instance = components[component]
        if (instance != null) {
            plugin.logger.warning("Component ${component.qualifiedName} is already registered, failing!")
            throw IllegalStateException("Component ${component.qualifiedName} is already registered, failing!")
        }

        return component.constructors.first().call(this)
    }

    fun getComponent(component: H): I? {
        val componentsa: Map<H, ComponentHolder<I>>  = components
        val componentHolder = componentsa[component]
        return componentHolder?.component
    }

}