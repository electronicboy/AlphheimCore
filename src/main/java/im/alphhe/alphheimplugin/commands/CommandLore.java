/*
 * Copyright (c) Shane Freeder
 * Unauthorized copying of this file is not permitted!
 * Written by Shane Freeder - 2018.
 */

package im.alphhe.alphheimplugin.commands;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CommandLore extends Command {
    private static HashMap<String, LinkedList<ItemStack>> undo = new HashMap<>();
    
    public CommandLore() {
        super("lore", "Lore command", "", new ArrayList<>());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sendHelp(sender);
            return true;
        }

        final Player player = (Player) sender;
        final ItemStack item = player.getInventory().getItemInHand();
        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(item.getType());
            if (meta == null) {
                player.sendMessage("§4The Item you are holding does not support Lore");
                return true;
            }
        }

        if (args.length < 1) {
            sendHelp(sender);
            return true;
        }

        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new LinkedList<>();
        }

        Action action;

        try {
            action = Action.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException notEnum) {
            sendHelp(sender);
            return true;
        }

        @SuppressWarnings("deprecation") final String id = player.getName() + "'" + item.getTypeId();

        if (action != Action.UNDO) {
            if (!CommandLore.undo.containsKey(id)) {
                CommandLore.undo.put(id, new LinkedList<>());
            }
            final LinkedList<ItemStack> list = CommandLore.undo.get(id);
            list.addFirst(item.clone());
            while (list.size() > 5) {
                list.removeLast();
            }
        }

        switch (action) {

            case NAME: {
                if (!sender.hasPermission("alphheim.lores.name") || args.length < 2) {
                    sendHelp(sender);
                    return true;
                }

                String name = concatArgs(sender, args, 1);
                String nameClean = ChatColor.stripColor(name);

                if (name.contains("|")) {
                    int max = name.replaceAll("§[0-9a-klmnor]", "").length();
                    for (String aLore : lore) {
                        max = Math.max(max, aLore.replaceAll("§[0-9a-klmnor]", "").length());
                    }
                    final int spaces = max - nameClean.length() - 1;
                    StringBuilder space = new StringBuilder(" ");
                    for (int i = 1; i < spaces * 1.5; ++i) {
                        space.append(' ');
                    }
                    name = name.replace("|", space.toString());
                }

                meta.setDisplayName(name);
                break;
            }

            case OWNER: {
                if (!sender.hasPermission("alphheim.lores.owner") || args.length < 2) {
                    sendHelp(sender);
                    return true;
                }

                if (!(meta instanceof SkullMeta)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4You may only set the Owner of a &6Skull"));
                    return true;
                }


                ((SkullMeta) meta).setOwner(args[1]);
                break;

            }

            case ADD: {
                if (!sender.hasPermission("alphheim.lores.lore") || args.length < 2) {
                    sendHelp(sender);
                    return true;
                }
                lore.add(concatArgs(sender, args, 1));
                break;
            }

            case DELETE: {
                if (!sender.hasPermission("alphheim.lores.lore")) {
                    sendHelp(sender);
                    return true;
                }

                switch (args.length) {
                    case 1: {
                        if (lore.size() < 1) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4There is nothing to delete!"));
                            return true;
                        }
                        lore.remove(lore.size() - 1);
                        break;
                    }

                    case 2: {
                        int index;
                        try {
                            index = Integer.parseInt(args[1]) - 1;
                        } catch (Exception e) {
                            return false;
                        }
                        if (lore.size() <= index || index < 0) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Invalid line number!"));
                            return true;
                        }
                        lore.remove(index);
                        break;
                    }

                    default: {
                        return false;
                    }

                }
                break;
            }

            case SET: {

                if (!sender.hasPermission("alphheim.lores.lore") || args.length < 3) {
                    sendHelp(sender);
                    return true;
                }

                int index;
                try {
                    index = Integer.parseInt(args[1]) - 1;
                } catch (Exception e) {
                    return false;
                }

                if (lore.size() <= index || index < 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&Invalid line number!"));
                    return true;
                }
                lore.set(index, concatArgs(sender, args, 2));
                break;
            }

            case INSERT: {
                if (!sender.hasPermission("alphheim.lores.lore") || args.length < 3) {
                    sendHelp(sender);
                    return true;
                }
                int j;

                try {
                    j = Integer.parseInt(args[1]) - 1;
                } catch (Exception e2) {
                    return false;
                }

                if (lore.size() <= j || j < 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Invalid line number!"));
                    return true;
                }

                lore.add(j, concatArgs(sender, args, 2));

                break;
            }

            case CLEAR: {
                if (!sender.hasPermission("alphheim.lores.lore") || args.length != 1) {
                    sendHelp(sender);
                    return true;
                }
                lore.clear();
                break;
            }

            case UNDO: {
                if (args.length != 1) {
                    return false;
                }
                final LinkedList<ItemStack> list2 = undo.get(id);
                if (list2 == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4You have not yet modified this Item!"));
                    return true;
                }
                if (list2.size() < 1) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4You cannot continue to undo for this Item!"));
                    return true;
                }
                final ItemStack undoneItem = list2.removeFirst();
                if (!item.isSimilar(undoneItem) && item.getType() != Material.SKULL_ITEM) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4You have not yet modified this Item!"));
                    return true;
                }
                final int stackSize = item.getAmount();
                if (undoneItem.getAmount() != stackSize) {
                    undoneItem.setAmount(stackSize);
                }
                player.getInventory().setItemInHand(undoneItem);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5The last modification you made on this item has been undone!"));
                return true;
            }
        }
        meta.setLore((lore));
        item.setItemMeta(meta);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5Lore successfully modified!"));
        return true;
    }

    private String concatArgs(final CommandSender sender, final String[] args, final int first) {
        final StringBuilder sb = new StringBuilder();
        if (first > args.length) {
            return "";
        }
        for (int i = first; i <= args.length - 1; ++i) {
            sb.append(" ");
            sb.append(ChatColor.translateAlternateColorCodes('&', args[i]));
        }
        final String string = sb.substring(1);
        final char[] charArray = string.toCharArray();
        boolean modified = false;
        for (int j = 0; j < charArray.length; ++j) {
            if (charArray[j] == '§' && !sender.hasPermission("alphheim.lores.color." + charArray[j + 1])) {
                charArray[j] = '?';
                modified = true;
            }
        }
        return modified ? String.copyValueOf(charArray) : string;
    }

    private void sendHelp(final CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e     Lores Help Page:"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5Each command will modify the Item in your hand"));
        if (sender.hasPermission("alphheim.lores.color") || sender.hasPermission("alphheim.lores.format")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&5Use &6& &5to add color with any command"));
        }
        if ( sender.hasPermission("alphheim.lores.name")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2/lore name <custom name> &bSet the new Name of the Item"));
        }
        if (sender.hasPermission("alphheim.lores.owner")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2/lore owner <player> &bChange the Owner of a Skull"));
        }
        if (sender.hasPermission("alphheim.lores.lore")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2/lore add <line of text> &bAdd a line to the lore"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2/lore set <line #> <line of text> &bChange a line of the lore"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2/lore insert <line #> <line of text> &bInsert a line into the lore"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2/lore delete [line #] &bDelete a line of the lore (last line by default)"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2/lore clear &bClear all lines of the lore"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2/lore undo &bUndoes your last modification (up to 5 times)"));
        }
    }

    public enum Action {
        NAME,
        OWNER,
        ADD,
        DELETE,
        SET,
        INSERT,
        CLEAR,
        UNDO
    }
}
