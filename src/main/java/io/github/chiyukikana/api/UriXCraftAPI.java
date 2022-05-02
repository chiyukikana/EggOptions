package io.github.chiyukikana.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class UriXCraftAPI extends JavaPlugin {
    public static String getPluginVersion = "§a1.1.3f";

    public static void ConsoleLog(boolean usePrefixs, String prefixs, ChatColor color, String msg) {
        if (usePrefixs) {
            Bukkit.getConsoleSender().sendMessage(prefixs + " " + color + msg);
        } else {
            Bukkit.getConsoleSender().sendMessage(color + msg);
        }
    }
}
