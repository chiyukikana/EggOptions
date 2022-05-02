package io.github.chiyukikana.main;

import io.github.chiyukikana.api.UriXCraftAPI;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.Random;

public class EggOptions extends JavaPlugin implements Listener {
    private boolean MythicMobs_depend = false;
    private String PREFIXS;
    private String SETTINGS_type;

    private boolean SETTINGS_none_playSound;
    private String[] SETTINGS_none_sound;

    private String SETTINGS_vanilla_entity;
    private double SETTINGS_vanilla_chance;
    private int SETTINGS_vanilla_min, SETTINGS_vanilla_max;
    private boolean SETTINGS_vanilla_playSound;
    private String[] SETTINGS_vanilla_success, SETTINGS_vanilla_failed;

    private String SETTINGS_mythicmobs_mmID;
    private double SETTINGS_mythicmobs_chance;
    private int SETTINGS_mythicmobs_min, SETTINGS_mythicmobs_max;
    private boolean SETTINGS_mythicmobs_playSound;
    private String[] SETTINGS_mythicmobs_success, SETTINGS_mythicmobs_failed;

    private String MESSAGES_reloadconfig, MESSAGES_valueout, MESSAGES_boundmust, MESSAGES_reloadperms, MESSAGES_MMnotfound;

    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();
        PREFIXS = getConfig().getString("MESSAGES.PREFIXS").replace('&', '§');
        SETTINGS_type = getConfig().getString("SETTINGS.type");

        SETTINGS_none_playSound = getConfig().getBoolean("SETTINGS.none.playSound");
        SETTINGS_none_sound = getConfig().getString("SETTINGS.none.sound").split("-");

        SETTINGS_vanilla_entity = getConfig().getString("SETTINGS.vanilla.entity").toUpperCase();
        SETTINGS_vanilla_chance = getConfig().getDouble("SETTINGS.vanilla.chance");
        SETTINGS_vanilla_min = getConfig().getInt("SETTINGS.vanilla.min");
        SETTINGS_vanilla_max = getConfig().getInt("SETTINGS.vanilla.max");
        SETTINGS_vanilla_playSound = getConfig().getBoolean("SETTINGS.vanilla.playSound");
        SETTINGS_vanilla_success = getConfig().getString("SETTINGS.vanilla.success").split("-");
        SETTINGS_vanilla_failed = getConfig().getString("SETTINGS.vanilla.failed").split("-");

        MESSAGES_reloadconfig = getConfig().getString("MESSAGES.reloadconfig").replace('&', '§').replaceAll("%p%", PREFIXS);
        MESSAGES_valueout = getConfig().getString("MESSAGES.valueout").replace('&', '§').replaceAll("%p%", PREFIXS);
        MESSAGES_boundmust = getConfig().getString("MESSAGES.boundmust").replace('&', '§').replaceAll("%p%", PREFIXS);
        MESSAGES_reloadperms = getConfig().getString("MESSAGES.reloadperms").replace('&', '§').replaceAll("%p%", PREFIXS);
        MESSAGES_MMnotfound = getConfig().getString("MESSAGES.MMnotfound").replace('&', '§').replaceAll("%p%", PREFIXS);

        SETTINGS_mythicmobs_mmID = getConfig().getString("SETTINGS.mythicmobs.mmID").replace('&', '§').replaceAll("%p%", PREFIXS);
        SETTINGS_mythicmobs_chance = getConfig().getDouble("SETTINGS.mythicmobs.chance");
        SETTINGS_mythicmobs_min = getConfig().getInt("SETTINGS.mythicmobs.min");
        SETTINGS_mythicmobs_max = getConfig().getInt("SETTINGS.mythicmobs.max");
        SETTINGS_mythicmobs_playSound = getConfig().getBoolean("SETTINGS.mythicmobs.playSound");
        SETTINGS_mythicmobs_success = getConfig().getString("SETTINGS.mythicmobs.success").split("-");
        SETTINGS_mythicmobs_failed = getConfig().getString("SETTINGS.mythicmobs.failed").split("-");
    }

    public void onEnable() {
        Bukkit.getPluginCommand("eggoptions").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        reloadConfig();
        Bukkit.getConsoleSender().sendMessage("");
        UriXCraftAPI.ConsoleLog(true, PREFIXS, ChatColor.WHITE, "本插件基于 Bukkit-1.15.2 编写, 理论全版本支持.");
        UriXCraftAPI.ConsoleLog(true, PREFIXS, ChatColor.WHITE, "原贴地址: https://www.mcbbs.net/thread--1-1.html");
        UriXCraftAPI.ConsoleLog(true, PREFIXS, ChatColor.WHITE, "当前版本: " + UriXCraftAPI.getPluginVersion);
        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            Bukkit.getConsoleSender().sendMessage(PREFIXS + " §7正在加载 §aMythicMobs §7插件功能.");
            this.MythicMobs_depend = true;
        } else {
            Bukkit.getConsoleSender().sendMessage(PREFIXS + " §7未安装 §cMythicMobs §7无法使用其功能.");
            this.MythicMobs_depend = false;
        }
        String type;
        if (SETTINGS_type.equalsIgnoreCase("none")) {
            type = "none";
            Bukkit.getConsoleSender().sendMessage(PREFIXS + " §7已选择类型: §b" + type);
        } else if (SETTINGS_type.equalsIgnoreCase("vanilla")) {
            type = "vanilla";
            Bukkit.getConsoleSender().sendMessage(PREFIXS + " §7已选择类型: §b" + type);
        } else if (SETTINGS_type.equalsIgnoreCase("mythicmobs")) {
            type = "mythicmobs";
            Bukkit.getConsoleSender().sendMessage(PREFIXS + " §7已选择类型: §b" + type);
            if (!(this.MythicMobs_depend)) {
                Bukkit.getConsoleSender().sendMessage(PREFIXS + " §c服务端中未安装 MythicMobs 无法使用其功能.");
            }
        } else {
            type = "invalid";
            Bukkit.getConsoleSender().sendMessage(PREFIXS + " §7已选择类型: §4" + type);
            Bukkit.getConsoleSender().sendMessage(PREFIXS + " §c无效的类型, 请检查您的配置文件.");
            Bukkit.getConsoleSender().sendMessage(PREFIXS + " §c可用类型: §fnone, vanilla, mythicmobs");
        }
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(PREFIXS + " §7插件已启用.");
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(PREFIXS + " §c插件已卸载, 感谢您的使用.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("eggoptions.reload")) {
            if (command.getName().equalsIgnoreCase("eggoptions")) {
                reloadConfig();
                String type;
                sender.sendMessage("");
                sender.sendMessage(MESSAGES_reloadconfig);
                if (SETTINGS_type.equalsIgnoreCase("none")) {
                    type = "none";
                    sender.sendMessage(PREFIXS + " §7已选择类型: §b" + type);
                } else if (SETTINGS_type.equalsIgnoreCase("vanilla")) {
                    type = "vanilla";
                    sender.sendMessage(PREFIXS + " §7已选择类型: §b" + type);
                } else if (SETTINGS_type.equalsIgnoreCase("mythicmobs")) {
                    type = "mythicmobs";
                    sender.sendMessage(PREFIXS + " §7已选择类型: §b" + type);
                    if (!(this.MythicMobs_depend)) {
                        sender.sendMessage(PREFIXS + " §c服务端中未安装 MythicMobs 无法使用其功能.");
                    }
                } else {
                    type = "invalid";
                    sender.sendMessage(PREFIXS + " §7已选择类型: §4" + type);
                    sender.sendMessage(PREFIXS + " §c无效的类型, 请检查您的配置文件.");
                    sender.sendMessage(PREFIXS + " §c可用类型: §fnone, vanilla, mythicmobs");
                }
                sender.sendMessage("");
                return true;
            }
            return true;
        } else {
            sender.sendMessage("");
            sender.sendMessage(MESSAGES_reloadperms);
            sender.sendMessage("");
        }
        return false;
    }

    @EventHandler
    public void PlayerEggThrow(PlayerEggThrowEvent event) {
        Player p = event.getPlayer();
        if (SETTINGS_type.equalsIgnoreCase("none")) {
            event.setHatching(false);
            if (SETTINGS_none_playSound) {
                p.playSound(p.getLocation(), Sound.valueOf(SETTINGS_none_sound[0]), Float.parseFloat(SETTINGS_none_sound[1]), Float.parseFloat(SETTINGS_none_sound[2]));
            }
        } else if (SETTINGS_type.equalsIgnoreCase("vanilla")) {
            if ((Math.random() > 0) && (Math.random() <= SETTINGS_vanilla_chance / 100)) {
                if (SETTINGS_vanilla_min <= SETTINGS_vanilla_max) {
                    if (!(SETTINGS_vanilla_min > 127 || SETTINGS_vanilla_max > 127)) {
                        event.setHatching(true);
                        event.setHatchingType(EntityType.valueOf(SETTINGS_vanilla_entity));
                        Random Min_Max = new Random();
                        event.setNumHatches(Byte.parseByte(String.valueOf(Min_Max.nextInt(SETTINGS_vanilla_max - SETTINGS_vanilla_min + 1) + SETTINGS_vanilla_min)));
                        if (SETTINGS_vanilla_playSound) {
                            p.playSound(p.getLocation(), Sound.valueOf(SETTINGS_vanilla_success[0]), Float.parseFloat(SETTINGS_vanilla_success[1]), Float.parseFloat(SETTINGS_vanilla_success[2]));
                        }
                    } else {
                        event.setHatching(false);
                        p.sendMessage(MESSAGES_valueout);
                    }
                } else {
                    event.setHatching(false);
                    p.sendMessage(MESSAGES_boundmust);
                }
            } else {
                event.setHatching(false);
                if (SETTINGS_vanilla_playSound) {
                    p.playSound(p.getLocation(), Sound.valueOf(SETTINGS_vanilla_failed[0]), Float.parseFloat(SETTINGS_vanilla_failed[1]), Float.parseFloat(SETTINGS_vanilla_failed[2]));
                }
            }
        } else if (SETTINGS_type.equalsIgnoreCase("mythicmobs")) {
            event.setHatching(false);
            if (this.MythicMobs_depend) {
                if ((Math.random() > 0) && (Math.random() <= SETTINGS_mythicmobs_chance / 100)) {
                    if (SETTINGS_mythicmobs_min <= SETTINGS_mythicmobs_max) {
                        if (!(SETTINGS_mythicmobs_min > 127 || SETTINGS_mythicmobs_max > 127)) {
                            event.setHatching(false);
                            Random Min_Max = new Random();
                            DecimalFormat floatNumber = new DecimalFormat("0.##");
                            Entity getEggEntity = event.getEgg();
                            Location getEggLocation = getEggEntity.getLocation();
                            World getEggWorld = getEggEntity.getWorld();
                            String getEggWorldString = getEggWorld.toString();
                            String EggWorld = getEggWorldString.substring(getEggWorldString.indexOf("=") + 1, getEggWorldString.indexOf("}"));
                            String EggLocation = floatNumber.format(getEggLocation.getX()) + "," + floatNumber.format(getEggLocation.getY()) + "," + floatNumber.format(getEggLocation.getZ()) + "," + floatNumber.format(getEggLocation.getYaw()) + "," + floatNumber.format(getEggLocation.getPitch());
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "mm mobs spawn " + SETTINGS_mythicmobs_mmID + " " + Byte.parseByte(String.valueOf(Min_Max.nextInt(SETTINGS_mythicmobs_max - SETTINGS_mythicmobs_min + 1) + SETTINGS_mythicmobs_min)) + " " + EggWorld + "," + EggLocation);
                            if (SETTINGS_mythicmobs_playSound) {
                                p.playSound(p.getLocation(), Sound.valueOf(SETTINGS_mythicmobs_success[0]), Float.parseFloat(SETTINGS_mythicmobs_success[1]), Float.parseFloat(SETTINGS_mythicmobs_success[2]));
                            }
                        } else {
                            event.setHatching(false);
                            p.sendMessage(MESSAGES_valueout);
                        }
                    } else {
                        event.setHatching(false);
                        p.sendMessage(MESSAGES_boundmust);
                    }
                } else {
                    event.setHatching(false);
                    if (SETTINGS_mythicmobs_playSound) {
                        p.playSound(p.getLocation(), Sound.valueOf(SETTINGS_mythicmobs_failed[0]), Float.parseFloat(SETTINGS_mythicmobs_failed[1]), Float.parseFloat(SETTINGS_mythicmobs_failed[2]));
                    }
                }
            } else {
                event.setHatching(false);
                p.sendMessage(MESSAGES_MMnotfound);
            }
        } else {
            event.setHatching(false);
            p.sendMessage(PREFIXS + " §c无效的类型, 请检查您的配置文件.");
            p.sendMessage(PREFIXS + " §c可用类型: §fnone, vanilla, mythicmobs");
        }
    }
}