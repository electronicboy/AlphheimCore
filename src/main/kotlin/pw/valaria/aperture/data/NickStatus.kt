/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.data

/**
 * Created by shane on 8/25/16.
 */
enum class NickStatus(val value: kotlin.Int) {
    PENDING(0),
    APPROVED(1),
    DENIED(2),
    INFORMED(3);

}
