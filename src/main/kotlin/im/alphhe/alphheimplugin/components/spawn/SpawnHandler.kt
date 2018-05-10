/*
 * Copyright (c) Alphheim
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.spawn

import im.alphhe.alphheimplugin.AlphheimCore
import im.alphhe.alphheimplugin.components.spawn.command.CommandSpawn
import im.alphhe.alphheimplugin.components.spawn.listeners.SpawnListener
import im.alphhe.alphheimplugin.utils.MessageUtil
import im.alphhe.alphheimplugin.utils.TeleportUtil
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

class SpawnHandler(val plugin: AlphheimCore) {


    private val worldSpawn = Location(Bukkit.getWorlds()[0], 850.0, 37.0, -1696.0, 180f, 0f)
    private val selectSpawn = Location(Bukkit.getWorlds()[0], 723.0, 6.0, -1692.0, 0f, 0f)

    private var spawnBook: ItemStack

    init {
        CommandSpawn(this, plugin)
        SpawnListener(this)

        val book = ItemStack(Material.WRITTEN_BOOK)

        var meta = book.itemMeta as BookMeta
        meta.title = "Welcome Guide"
        meta.author = "Alphheim"
        meta.addPage(
                ChatColor.translateAlternateColorCodes('&', "&2&lAlphheim 1.0\n&0\n&0\n&0&nGetting Started&0\n&0\n&0This book is made to help all new players navigate the server of Alphheim!&0\n&0Pay close attention to any informaton given to you."),
                ChatColor.translateAlternateColorCodes('&', "&nWebsite&0\n&0\n&3www.alphhe.im&0\n&0\n&0&nDynmap&0\n&0\n&6www.alphhe.im/dynmap&0\n&0\n&0&nDiscord&0\n&0\n&9https://discord.gg/8FUwt75"),
                ChatColor.translateAlternateColorCodes('&', "&nServer Races:&0\n&0\n&9Human&0\n&0\n&2Elf&0\n&0\n&4Dwarf&0\n&0\n&0Each race provided has their own buffs that will be given when one chooses a race. A race change costs in game money."),
                ChatColor.translateAlternateColorCodes('&', "&nKingdoms&0\n&0\n&0There are kingdoms throughout Alphheim. To get a kingdom, go in &cOOC&0. Town leaders will be glad to add you to a town! If you need help contact a staff member."),
                ChatColor.translateAlternateColorCodes('&', "&nBasic Rules&0\n&0\n&0 No cheating, hacking, glitching&0\n&0Do not use an offensive skin, username or nickname&0\n&0No abusing exploits, report them immediately&0\n&0Keep inappropriate roleplay in the /whisper chat or /party chat, No spamming or advertising&0\n&0Respect fellow players and staff&0\n&0No constant caps when chatting&0\n&0 No metagaming or powergaming&0\n&0\n&0For our full list of rules, visit &dwww.alphhe.im/rules"),
                ChatColor.translateAlternateColorCodes('&', "&nChats&0\n&0\n&c/ooc &0- Out of Chatacter&0\n&7/g &0- Global&0\n&8/l &0- Local&0\n&4/w &0- Whisper&0\n&1/tc &0- Towny Chat&0\n&2/gc &0- Group Chat&0\n&9/ic &0- In Character"),
                ChatColor.translateAlternateColorCodes('&', "&nHelpful Commands&0\n&0\n&5/nick request&0\n&0Request a nickname&0\n&3/vote&0\n&0Vote daily for rewards&0\n&6/bal&0\n&0Check your balance&0\n&a/warp&0\n&0Our free warps&0\n&c/mcmmo&0\n&0McMMO info page&0\n&d/realname&0\n&0Helps with rp names"),
                ChatColor.translateAlternateColorCodes('&', "&nDonation Store&0\n&0\n&0If you wish to &2help &0the server, stop by the &6donation store&0. You can buy &ccommands&0, &9in game items&0, &0and &5donor ranks &0which come with &8kits &0and &3more&0!"),
                ChatColor.translateAlternateColorCodes('&', "&nStaff&0\n&0\n&0If you need help, contact the staff!&0\n&6Dark_SargeWolf&0\n&6MissCake00&0\n&5electronicboy&0\n&3Galduron&0\n&cBiznitcash&0\n&2A&3n&6a&8r&5c&bh&ci&ds&et&0Fly&0\n&bPluvix&0\n&bPepperGuardian\n&bNacho\n&1&lEnjoy Alphheim!")
        )
        book.itemMeta = meta
        spawnBook = book

    }


    fun getBook(): ItemStack = spawnBook


    fun goSpawn(teleportee: Player, teleporter: CommandSender?, silent: Boolean = false) {
        if (teleportee == teleporter) {
            val spawn = resolveSpawn(teleportee)
            TeleportUtil(teleportee, spawn, 10, plugin).process()

        } else if (teleporter == null || teleporter is ConsoleCommandSender || silent) {
            val spawn = resolveSpawn(teleportee)
            TeleportUtil(teleportee, spawn, -1, plugin).process()
        } else if (teleporter is Player) {
            val spawn = resolveSpawn(teleportee)
            TeleportUtil(teleportee, spawn, -1, plugin).process()
            MessageUtil.sendInfo(teleportee, "You have been teleported to spawn by ${teleporter.name}")
            MessageUtil.sendInfo(teleporter, "Teleported ${teleportee.player.name} to spawn!")
        }


    }


    internal fun resolveSpawn(player: Player): Location {
        return if (player.hasPermission("alphheim.raceselected"))
            worldSpawn
        else
            selectSpawn
    }

}