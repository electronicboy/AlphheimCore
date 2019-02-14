/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components

import pw.valaria.aperture.ApertureCore

abstract class AbstractHandler(open val plugin: ApertureCore) {

    open fun onDisable(){}
}