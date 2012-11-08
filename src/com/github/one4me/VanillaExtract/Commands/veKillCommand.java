package com.github.one4me.VanillaExtract.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.KillCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import com.github.one4me.VanillaExtract.ImprovedOfflinePlayer.ImprovedOfflinePlayer;

public class veKillCommand extends KillCommand {
  public veKillCommand() {
    this.description = "Kills the specified player";
    this.usageMessage = "/kill <player> (force)";
    this.setPermissionMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
  }
  @Override
  public boolean execute(CommandSender sender, String currentAlias, String[] args) {
    if(args.length == 0) {
      if(!(sender instanceof Player)) {
        sender.sendMessage("You can only perform this command as a player");
        return true;
      }
      if(!sender.hasPermission("bukkit.command.kill")) {
        sender.sendMessage(this.getPermissionMessage());
        return true;
      }
      kill(sender, sender.getName(), false);
      return true;
    }
    if(args.length == 1) {
      if(args[0].equalsIgnoreCase("force")) {
        if(!(sender instanceof Player)) {
          sender.sendMessage("You can only perform this command as a player");
          return true;
        }
        if(!sender.hasPermission("bukkit.command.kill.force")) {
          sender.sendMessage(this.getPermissionMessage());
          return true;
        }
        kill(sender, sender.getName(), true);
        return true;
      }
      if(args[0].equalsIgnoreCase(sender.getName())) {
        if(!sender.hasPermission("bukkit.command.kill") && !sender.hasPermission("bukkit.command.other")) {
          sender.sendMessage(this.getPermissionMessage());
          return true;
        }
        kill(sender, args[0], false);
        return true;
      }
      if(!args[0].equalsIgnoreCase(sender.getName())) {
        if(!sender.hasPermission("bukkit.command.other")) {
          sender.sendMessage(this.getPermissionMessage());
          return true;
        }
        kill(sender, args[0], false);
        return true;
      }
    }
    if(args.length == 2) {
      if(args[1].equalsIgnoreCase("force")) {
        if(!sender.hasPermission("bukkit.command.other.force")) {
          sender.sendMessage(this.getPermissionMessage());
          return true;
        }
        kill(sender, args[0], true);
      }
      else {
        if(sender.hasPermission("bukkit.command.other.force")) {
          sender.sendMessage(ChatColor.RED + "Usage: /kill <player> (force)");
        }
        else if(sender.hasPermission("bukkit.command.other")){
          sender.sendMessage(ChatColor.RED + "Usage: /kill <player>");
        }
        else if(sender.hasPermission("bukkit.command.force")) {
          sender.sendMessage(ChatColor.RED + "Usage: /kill (force)");
        }
        else {
          sender.sendMessage(ChatColor.RED + "Usage: /kill");
        }
      }
    }
    return true;
  }
  public void kill(CommandSender sender, String name, boolean force) {
    if(name.equalsIgnoreCase("everyone")) {
      boolean attempt = false;
      for(OfflinePlayer offlineplayer : Bukkit.getOfflinePlayers()) {
        Player player = offlineplayer.getPlayer();
        if(player != null) {
          EntityDamageEvent ede = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1000);
          Bukkit.getPluginManager().callEvent(ede);
          if(ede.isCancelled() && !force) {
            if(sender.getName().equalsIgnoreCase(player.getName())) {
              player.sendMessage("You have attempted to kill yourself.");
            }
            else {
              player.sendMessage(sender.getName() + " has attempted to kill you.");
            }
            continue;
          }
          ede.getEntity().setLastDamageCause(ede);
          if(force) {
            player.setHealth(0);
          }
          else {
            player.damage(ede.getDamage());
          }
          if(player.isDead()) {
            if(sender.getName().equalsIgnoreCase(player.getName())) {
              player.sendMessage("You have killed yourself.");
            }
            else {
              player.sendMessage(sender.getName() + " has killed you.");
            }
          }
          else {
            if(sender.getName().equalsIgnoreCase(player.getName())) {
              player.sendMessage("You have attempted to kill yourself.");
            }
            else {
              player.sendMessage(sender.getName() + " has attempted to kill you.");
            }
            attempt = true;
          }
        }
        ImprovedOfflinePlayer iop = new ImprovedOfflinePlayer(offlineplayer);
        if(iop.exists()) {
          if((player != null ? player.getGameMode().getValue() : iop.getMiscGameMode()) != 1 || force) {
            iop.setMiscHealth((short)0);
          }
          else {
            if(player == null) {
              attempt = true;
            }
          }
        }
      }
      if(attempt) {
        sender.sendMessage("You have attempted to kill: everyone");
      }
      else {
        sender.sendMessage("You have killed: everyone");
      }
      return;
    }
    Player player = Bukkit.getPlayerExact(name);
    if(player != null) {
      EntityDamageEvent ede = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1000);
      Bukkit.getPluginManager().callEvent(ede);
      if(ede.isCancelled() && !force) {
        if(sender.getName().equalsIgnoreCase(player.getName())) {
          player.sendMessage("You have attempted to kill yourself.");
        }
        else {
          player.sendMessage(sender.getName() + " has attempted to kill you.");
          sender.sendMessage("You have attempted to kill: " + name);
        }
        return;
      }
      ede.getEntity().setLastDamageCause(ede);
      if(force) {
        player.setHealth(0);
      }
      else {
        player.damage(ede.getDamage());
      }
      if(player.isDead()) {
        if(sender.getName().equalsIgnoreCase(name)) {
          player.sendMessage("You have killed yourself.");
        }
        else {
          player.sendMessage(sender.getName() + " has killed you.");
          sender.sendMessage("You have killed: " + name);
        }
      }
      else {
        if(sender.getName().equalsIgnoreCase(name)) {
          player.sendMessage("You have attempted to kill yourself.");
        }
        else {
          player.sendMessage(sender.getName() + " has attempted to kill you.");
          sender.sendMessage("You have attempted to kill: " + name);
        }
      }
    }
    ImprovedOfflinePlayer iop = new ImprovedOfflinePlayer(name);
    if(iop.exists()) {
      if((player != null ? player.getGameMode().getValue() : iop.getMiscGameMode()) != 1 || force) {
        iop.setMiscHealth((short)0);
        if(player == null) {
          sender.sendMessage("You have killed: " + iop.getName());
        }
      }
      else {
        if(player == null) {
          sender.sendMessage("You have attempted to kill: " + iop.getName());
        }
      }
    }
    else {
      if(player == null) {
        sender.sendMessage("Can't find user " + name + ". No kill.");
      }
    }
  }
}
