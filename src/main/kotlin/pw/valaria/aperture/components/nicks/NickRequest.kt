/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2019.
 */

package pw.valaria.aperture.components.nicks

import pw.valaria.aperture.data.NickStatus
import java.util.*

class NickRequest(uuid: UUID) {

    lateinit var status: NickStatus
    var nickname: String? = null
    var requested: String? = null
    val nickStatus: NickStatus? = null


}