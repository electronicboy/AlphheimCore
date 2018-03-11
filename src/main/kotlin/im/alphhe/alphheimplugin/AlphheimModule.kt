/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin

import com.google.inject.Binder
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import im.alphhe.alphheimplugin.components.UserManager

class AlphheimModule(private val alphheimCore: AlphheimCore) : Module {
    lateinit var injector: Injector

    fun createInjector(): Injector {
        injector = Guice.createInjector(this)
        return injector
    }

    override fun configure(binder: Binder) {
        binder.bind(AlphheimCore::class.java).toInstance(alphheimCore);
        //binder.bind(UserManager::class.java).to(UserManager::class.java)
    }
}

