/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.data

import org.bukkit.ChatColor

/**
 * Created by shane on 9/23/16.
 * Created for Valaria
 * This class is used to store configurables that are accessed in more than one location
 * but inherently dont justify getting a class dedicated for their entire operation.
 */
public class ConfigurableData {


    val noPerms = ChatColor.RED.toString() + "You do not have permission to use this!"
    val noTarget = ChatColor.RED.toString() + "target has not been found!"

    val startingFunds = 5000.0


    companion object {

        private var HOLDER: ConfigurableData = ConfigurableData()

        fun getInstance(): ConfigurableData = HOLDER


    }


}
