/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components

import im.alphhe.alphheimplugin.EladriaCore

abstract class AbstractHandler(open val plugin: EladriaCore) {

    open fun onDisable(){};
}