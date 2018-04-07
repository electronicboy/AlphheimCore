/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.components.tablist

import im.alphhe.alphheimplugin.AlphheimCore
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.ScoreboardManager
import org.bukkit.scoreboard.Team
import java.util.*

class TabListHandler(private val plugin: AlphheimCore) {

    private val manager: ScoreboardManager
    private val sb: Scoreboard
    private val teams: MutableMap<String, Team>

    init {
        teams = HashMap()
        manager = Bukkit.getScoreboardManager()

        sb = manager.newScoreboard

        teams["owner"] = getTeam("a_owner", "&1&4[&bOwner&4] &e")
        teams["dev"] = getTeam("b_dev", "&a&4[&8Dev&4] &9")
        teams["ha"] = getTeam("c_cm", "&c&4[&8HA&4] &9")
        teams["admin"] = getTeam("d_staff", "&4[&8Admin&4] &9")
        teams["mod"] = getTeam("e_mod", "&4[&8Mod&4] &9")


        // humans
        teams["hleader"] = getTeam("f_hleader", "&3[&bH&3]&6 ")
        teams["hroyal"] = getTeam("g_hroyal", "&3[&bH&3]&3 ")
        teams["hplayer"] = getTeam("h_hplayer", "&3[&bH&3]&7 ")

        // Dwarfs
        teams["dleader"] = getTeam("i_dleader", "&4[&cD&4]&6 ")
        teams["droyal"] = getTeam("j_droyal", "&4[&cD&4]&3 ")
        teams["dplayer"] = getTeam("k_dplayer", "&4[&cD&4]&7 ")

        // Elfs
        teams["eleader"] = getTeam("l_dleader", "&2[&aE&2]&6 ")
        teams["eroyal"] = getTeam("m_droyal", "&2[&aE&2]&3 ")
        teams["eplayer"] = getTeam("n_dplayer", "&2[&aE&2]&7 ")


        // misc
        teams["exile"] = getTeam("y_exile", "&8[Exile]&m ")
        teams["zpm"] = getTeam("z_permless", "&7 ")

        plugin.server.onlinePlayers.forEach { setSB(it) }
    }

    fun setSB(player: Player): Unit {
        player.scoreboard = sb
        sb.teams.forEach { t ->
            if (t.hasEntry(player.name))
                t.removeEntry(player.name)
        }

        if (player.hasPermission("alphheim.owner")) {
            teams["owner"]?.addEntry(player.name)
            return
        }
        
        if (player.hasPermission("alphheim.dev")) {
            teams["dev"]?.addEntry(player.name)
            return
        }

        if (player.hasPermission("alphheim.ha")) {
            teams["ha"]?.addEntry(player.name)
            return
        }

        if (player.hasPermission("alphheim.admin")) {
            teams["admin"]?.addEntry(player.name)
            return
        }

        if (player.hasPermission("alphheim.mod")) {
            teams["mod"]?.addEntry(player.name)
            return
        }

        // Human
        if (player.hasPermission("alphheim.human")) {
            if (player.hasPermission("alphheim.leader")) {
                teams["hleader"]?.addEntry(player.name)
                return
            }
            
            if (player.hasPermission("alphheim.royal")) {
                teams["hroyal"]?.addEntry(player.name)
                return
            }
            
            teams["hplayer"]?.addEntry(player.name)
            return
        }

        // Elf
        if (player.hasPermission("alphheim.elf")) {
            if (player.hasPermission("alphheim.leader")) {
                teams["eleader"]?.addEntry(player.name)
                return
            }

            if (player.hasPermission("alphheim.royal")) {
                teams["eroyal"]?.addEntry(player.name)
                return
            }

            teams["eplayer"]?.addEntry(player.name)
            return
        }

        // Dwarf
        if (player.hasPermission("alphheim.dwarf")) {
            if (player.hasPermission("alphheim.leader")) {
                teams["dleader"]?.addEntry(player.name)
                return
            }

            if (player.hasPermission("alphheim.royal")) {
                teams["droyal"]?.addEntry(player.name)
                return
            }

            teams["dplayer"]?.addEntry(player.name)
            return
        }

        // Well, fuck....
        teams["zpm"]?.addEntry(player.name)

    }


    private fun getTeam(name: String, prefixArg: String): Team {

        var prefix = prefixArg
        var team: Team? = sb.getTeam(name) ?: createTeam(name, prefixArg)

        if (team == null) {
            team = sb.registerNewTeam(name)
            prefix = ChatColor.translateAlternateColorCodes('&', prefix)

            if (prefix.length > 16) {
                prefix = prefix.substring(0, 16)
            }

            team!!.prefix = prefix
        }

        return team
    }

    private fun createTeam(name: String, prefix: String): Team {
        val team = sb.registerNewTeam(name)
        var newPrefix = ChatColor.translateAlternateColorCodes('&', prefix)
        if (newPrefix.length > 16) {
            newPrefix = newPrefix.substring(0, 16)
        }
        team.prefix = newPrefix
        return team
    }
}