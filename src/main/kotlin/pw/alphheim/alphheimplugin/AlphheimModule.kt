/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package pw.alphheim.alphheimplugin

import com.google.inject.Binder
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module

class AlphheimModule(private val alphheimCore: AlphheimCore) : Module {

    fun createInjector(): Injector {
        return Guice.createInjector(this)
    }

    override fun configure(binder: Binder) {
        binder.bind(AlphheimCore::class.java).toInstance(alphheimCore);
    }
}

