//Copyright (C) 2012 one4me@github.com
package com.github.one4me.VanillaExtract;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.one4me.VanillaExtract.Commands.veSimpleCommandMap;

//@SuppressWarnings("all")
public class VanillaExtract extends JavaPlugin implements Listener {
  public void onEnable() {
    loadCommands();
  }
  public void onDisable() {
  }
  public void loadCommands() {
    try {
      Field field = CraftServer.class.getDeclaredField("commandMap");
      field.setAccessible(true);
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
      SimpleCommandMap smp = new veSimpleCommandMap(Bukkit.getServer());
      for(Plugin p : Bukkit.getPluginManager().getPlugins()) {
        List<Command> pluginCommands = PluginCommandYamlParser.parse(p);
        if(!pluginCommands.isEmpty()) {
          smp.registerAll(p.getDescription().getName(), pluginCommands);
        }
      }
      smp.registerServerAliases();
      field.set(Bukkit.getServer(), smp);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public static boolean hasPermissionWild(CommandSender sender, String node) {
    if(sender.hasPermission(node) || sender.hasPermission(node + ".*") || sender.hasPermission("*")) {
      return true;
    }
    while(node.indexOf(".") > -1) {
      node = node.substring(0, node.lastIndexOf("."));
      if(sender.hasPermission(node + ".*")) {
        return true;
      }
    }
    return false;
  }
}
