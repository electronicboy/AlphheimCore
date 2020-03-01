/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.spawn

import co.aikar.timings.Timings
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import pw.valaria.aperture.components.spawn.command.CommandSpawn
import pw.valaria.aperture.components.spawn.listeners.SpawnListener
import pw.valaria.aperture.utils.MessageUtil
import pw.valaria.aperture.utils.TeleportUtil

class SpawnHandler(plugin: ApertureCore) : AbstractHandler(plugin) {

    private var spawnBook: ItemStack
    lateinit var spawn: World

    init {

        Bukkit.getScheduler().runTask(plugin) { t ->

            Timings.reset()
            var spawnWorld =  plugin.server.getWorld("spawn")
            if (spawnWorld == null) {
                spawnWorld = WorldCreator("spawn")
                        .environment(World.Environment.NORMAL)
                        .type(WorldType.FLAT)
                        .generateStructures(false)
                        .createWorld();
            }

            spawnWorld!!

            spawnWorld.setSpawnFlags(false, false) // No mobs/animals
            spawn = spawnWorld

        };

        CommandSpawn(this, plugin)
        SpawnListener(this)

        val book = ItemStack(Material.WRITTEN_BOOK)

        var meta = book.itemMeta as BookMeta
        meta.title = "Welcome Guide"
        meta.author = "Valaria"
        //language=TEXT
        meta.addPage(
                ChatColor.translateAlternateColorCodes('&', "&2&lValaria\n&0\n&0\n&0&nGetting Started&0\n&0\n&0This book is made to help all new players navigate the server of Valaria!&0\n&0Pay close attention to any informaton given to you."),
                ChatColor.translateAlternateColorCodes('&', "&nWebsite&0\n&0\n&3www.valaria.pw&0\n&0\n&0&nDynmap&0\n&0\n&6www.alphhe.im/dynmap&0\n&0\n&0&nDiscord&0\n&0\n&9https://discord.gg/8FUwt75"),
                ChatColor.translateAlternateColorCodes('&', "&nServer Races:&0\n&0\n&9Human&0\n&0\n&2Elf&0\n&0\n&4Dwarf&0\n&0\n&0Each race provided has their own buffs that will be given when one chooses a race. A race change costs in game money."),
                ChatColor.translateAlternateColorCodes('&', "&nKingdoms&0\n&0\n&0There are kingdoms throughout Valaria. To get a kingdom, go in &cOOC&0. Town leaders will be glad to add you to a town! If you need help contact a staff member."),
                ChatColor.translateAlternateColorCodes('&', "&nBasic Rules&0\n&0\n&0 No cheating, hacking, glitching&0\n&0Do not use an offensive skin, username or nickname&0\n&0No abusing exploits, report them immediately&0\n&0Keep inappropriate roleplay in the /whisper chat or /party chat, No spamming or advertising&0\n&0Respect fellow players and staff&0\n&0No constant caps when chatting&0\n&0 No metagaming or powergaming&0\n&0\n&0For our full list of rules, visit &dwww.alphhe.im/rules"),
                ChatColor.translateAlternateColorCodes('&', "&nChats&0\n&0\n&c/ooc &0- Out of Chatacter&0\n&7/g &0- Global&0\n&8/l &0- Local&0\n&4/w &0- Whisper&0\n&1/tc &0- Towny Chat&0\n&2/gc &0- Group Chat&0\n&9/ic &0- In Character"),
                ChatColor.translateAlternateColorCodes('&', "&nHelpful Commands&0\n&0\n&5/nick request&0\n&0Request a nickname&0\n&3/vote&0\n&0Vote daily for rewards&0\n&6/bal&0\n&0Check your balance&0\n&a/warp&0\n&0Our free warps&0\n&c/mcmmo&0\n&0McMMO info page&0\n&d/realname&0\n&0Helps with rp names"),
                ChatColor.translateAlternateColorCodes('&', "&nDonation Store&0\n&0\n&0If you wish to &2help &0the server, stop by the &6donation store&0. You can buy &ccommands&0, &9in game items&0, &0and &5donor ranks &0which come with &8kits &0and &3more&0!"),
                ChatColor.translateAlternateColorCodes('&', "&nStaff&0\n&0\n&0If you need help, contact the staff!&0\n" +
                        "\n" +
                        "&1&lEnjoy Valaria!")
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
            MessageUtil.sendInfo(teleporter, "Teleported ${teleportee.player!!.name} to spawn!")
        }


    }


    internal fun resolveSpawn(player: Player): Location {
        val spawnWorld = Bukkit.getWorld("spawn")
        val selectSpawn = Location(spawnWorld, -1283.5, 33.0, -1597.5, 90f, 0f)//Location(Bukkit.getWorlds()[0], 723.0, 6.0, -1692.0, 0f, 0f)

        return if (player.hasPermission("alphheim.raceselected"))
            Bukkit.getWorlds()[0].spawnLocation
        else
            selectSpawn
    }

}