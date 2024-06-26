/*
 * Copyright (c) Shane Freeder 2019
 *
 * Unauthorized copying of this file is not permitted!
 * Written by shane - 2019.2.6.
 *
 */

package pw.valaria.aperture.components.tablist

import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.ScoreboardManager
import org.bukkit.scoreboard.Team
import pw.valaria.aperture.ApertureCore
import pw.valaria.aperture.components.AbstractHandler
import java.util.*

class TabListHandler(plugin: ApertureCore) : AbstractHandler(plugin) {

    private lateinit var manager: ScoreboardManager
    private lateinit var sb: Scoreboard
    private lateinit var teams: MutableMap<String, Team>
    @Volatile
    private var hasInit = false

    init {
        plugin.server.scheduler.runTask(plugin, Runnable {
            init()
        })
    }


    fun init() {
        if (!hasInit) {

            synchronized(this) {
                if (!hasInit) {
                    teams = HashMap()
                    manager = Bukkit.getScoreboardManager()

                    sb = manager.newScoreboard

                    teams["owner"] = getTeam("a_owner", "&1&4[&8Owner&4] &4")
                    teams["coowner"] = getTeam("b_coowner", "&1&4[&8Owner&4] &4")
                    teams["dev"] = getTeam("c_dev", "&a&4[&8Dev&4] &9")
                    teams["ha"] = getTeam("d_cm", "&c&4[&8HA&4] &9")
                    teams["admin"] = getTeam("e_staff", "&4[&8Admin&4] &9")
                    teams["mod"] = getTeam("f_mod", "&4[&8Mod&4] &9")


                    // humans
                    teams["hleader"] = getTeam("g_hleader", "&3[&bH&3]&6 ")
                    teams["hroyal"] = getTeam("h_hroyal", "&3[&bH&3]&3 ")
                    teams["hplayer"] = getTeam("i_hplayer", "&3[&bH&3]&7 ")

                    // Dwarfs
                    teams["dleader"] = getTeam("j_dleader", "&4[&cD&4]&6 ")
                    teams["droyal"] = getTeam("k_droyal", "&4[&cD&4]&3 ")
                    teams["dplayer"] = getTeam("l_dplayer", "&4[&cD&4]&7 ")

                    // Elfs
                    teams["eleader"] = getTeam("m_dleader", "&2[&aE&2]&6 ")
                    teams["eroyal"] = getTeam("n_droyal", "&2[&aE&2]&3 ")
                    teams["eplayer"] = getTeam("o_dplayer", "&2[&aE&2]&7 ")


                    // misc
                    teams["exile"] = getTeam("y_exile", "&8[Exile]&m ")
                    teams["zpm"] = getTeam("z_permless", "&7 ")

                    hasInit = true
                    plugin.server.onlinePlayers.forEach { setSB(it) }
                }
            }

        }
    }

    fun setSB(player: Player): Unit {
        init()

        player.scoreboard = sb
        sb.teams.forEach { t ->
            if (t.hasEntry(player.name))
                t.removeEntry(player.name)
        }

        if (player.hasPermission("group.owner")) {
            teams["owner"]?.addEntry(player.name)
            return
        }

        if (player.hasPermission("group.developer")) {
            teams["dev"]?.addEntry(player.name)
            return
        }

        if (player.hasPermission("group.coowner")) {
            teams["coowner"]?.addEntry(player.name)
            return
        }

        if (player.hasPermission("group.headadmin")) {
            teams["ha"]?.addEntry(player.name)
            return
        }

        if (player.hasPermission("group.admin")) {
            teams["admin"]?.addEntry(player.name)
            return
        }

        if (player.hasPermission("group.mod")) {
            teams["mod"]?.addEntry(player.name)
            return
        }

        // Human
        if (player.hasPermission("group.human")) {
            if (player.hasPermission("ranktrait.leader")) {
                teams["hleader"]?.addEntry(player.name)
                return
            }

            if (player.hasPermission("ranktrait.royal")) {
                teams["hroyal"]?.addEntry(player.name)
                return
            }

            teams["hplayer"]?.addEntry(player.name)
            return
        }

        // Elf
        if (player.hasPermission("group.elf")) {
            if (player.hasPermission("ranktrait.leader")) {
                teams["eleader"]?.addEntry(player.name)
                return
            }

            if (player.hasPermission("ranktrait.royal")) {
                teams["eroyal"]?.addEntry(player.name)
                return
            }

            teams["eplayer"]?.addEntry(player.name)
            return
        }

        // Dwarf
        if (player.hasPermission("group.dwarf")) {
            if (player.hasPermission("ranktrait.leader")) {
                teams["dleader"]?.addEntry(player.name)
                return
            }

            if (player.hasPermission("ranktrait.royal")) {
                teams["droyal"]?.addEntry(player.name)
                return
            }

            teams["dplayer"]?.addEntry(player.name)
            return
        }

        // Well, fuck....
        teams["zpm"]?.addEntry(player.name)

    }


    private fun getTeam(name: String, prefixArg: String) = getTeam(name, prefixArg, null)
    private fun getTeam(name: String, prefixArg: String, color: ChatColor?): Team {

        var prefix = prefixArg
        var team: Team? = sb.getTeam(name) ?: createTeam(name, prefixArg, color)

        if (team == null) {
            team = sb.registerNewTeam(name)
            prefix = ChatColor.translateAlternateColorCodes('&', prefix)

            if (prefix.length > 16) {
                prefix = prefix.substring(0, 16)
            }

            team.prefix = prefix
        }

        return team
    }

    private fun createTeam(name: String, prefix: String, color: ChatColor?): Team {
        var localColor = color
        val team = sb.registerNewTeam(name)
        var newPrefix = ChatColor.translateAlternateColorCodes('&', prefix)
        if (newPrefix.length > 16) {
            newPrefix = newPrefix.substring(0, 16)
        }
        team.prefix = newPrefix
        if (localColor == null && prefix.isNotEmpty()) {
            val components = TextComponent.fromLegacyText(newPrefix)
            val lastComponent = components[components.size-1]
            localColor = ChatColor.valueOf(lastComponent.color.getName().toUpperCase())

        }
        team.color = localColor!!
        return team
    }
}