/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2017.
 */

package im.alphhe.alphheimplugin.data

/**
 * Created by shane on 8/25/16.
 */
enum class NickStatus(val value: kotlin.Int) {
    PENDING(0),
    APPROVED(1),
    DENIED(2),
    INFORMED(3);

}
