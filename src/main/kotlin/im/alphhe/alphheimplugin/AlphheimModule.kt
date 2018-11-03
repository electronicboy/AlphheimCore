/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin

import com.google.inject.Binder
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module

class AlphheimModule(private val alphheimCore: EladriaCore) : Module {
    lateinit var injector: Injector

    fun createInjector(): Injector {
        injector = Guice.createInjector(this)
        return injector
    }

    override fun configure(binder: Binder) {
        binder.bind(EladriaCore::class.java).toInstance(alphheimCore);
        //binder.bind(UserManager::class.java).to(UserManager::class.java)
    }
}

