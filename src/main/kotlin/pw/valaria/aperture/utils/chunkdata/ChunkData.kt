/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.utils.chunkdata

import java.util.function.Supplier

abstract class ChunkData<D>( val test: Supplier<D>)