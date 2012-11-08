package com.github.one4me.VanillaExtract.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.GameModeCommand;
import org.bukkit.entity.Player;

import com.github.one4me.VanillaExtract.VanillaExtract;
import com.github.one4me.VanillaExtract.ImprovedOfflinePlayer.ImprovedOfflinePlayer;

public class veGameModeCommand extends GameModeCommand {
  @Override
  public boolean execute(CommandSender sender, String currentAlias, String[] args) {
    if(!VanillaExtract.hasPermissionWild(sender, this.getPermission())) {
      return true;
    }
    if(args.length == 0) {
      sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
      return false;
    }
    String modeArg = args[0];
    String playerArg = sender.getName();
    if(args.length == 2) {
      playerArg = args[1];
    }
    Player player = Bukkit.getPlayerExact(playerArg);
    int value = -1;
    try {
      value = Integer.parseInt(modeArg);
    }
    catch(NumberFormatException ex) {
    }
    GameMode mode = GameMode.getByValue(value);
    if(mode == null) {
      if((modeArg.equalsIgnoreCase("default")) || (modeArg.equalsIgnoreCase("d")) || (modeArg.equalsIgnoreCase("-1"))) {
        mode = Bukkit.getDefaultGameMode();
      }
      else if((modeArg.equalsIgnoreCase("creative")) || (modeArg.equalsIgnoreCase("c"))) {
        mode = GameMode.CREATIVE;
      }
      else if((modeArg.equalsIgnoreCase("adventure")) || (modeArg.equalsIgnoreCase("a"))) {
        mode = GameMode.ADVENTURE;
      }
      else {
        mode = GameMode.SURVIVAL;
      }
    }
    if(playerArg.equalsIgnoreCase("everyone")) {
      for(OfflinePlayer offlineplayer : Bukkit.getOfflinePlayers()) {
        if(offlineplayer.isOnline()) {
          offlineplayer.getPlayer().setGameMode(mode);
        }
        ImprovedOfflinePlayer iop = new ImprovedOfflinePlayer(offlineplayer);
        if(iop.exists()) {
          iop.setMiscGameMode(mode.getValue());
        }
      }
      Command.broadcastCommandMessage(sender, "Set everyone's game mode to " + mode + " mode", false);
      return true;
    }
    if(player == null) {
      ImprovedOfflinePlayer iop = new ImprovedOfflinePlayer(playerArg);
      if(iop.exists()) {
        if(mode != GameMode.getByValue(iop.getMiscGameMode())) {
          iop.setMiscGameMode(mode.getValue());
          if(mode != GameMode.getByValue(iop.getMiscGameMode())) {
            sender.sendMessage("Game mode change for " + iop.getName() + " failed!");
          }
          else {
            Command.broadcastCommandMessage(sender, "Set " + iop.getName() + "'s game mode to " + mode.toString() + " mode", false);
          }
        }
        else {
          sender.sendMessage(iop.getName() + " already has game mode " + mode.getValue());
        }
      }
      else {
        sender.sendMessage("Can't find user " + iop.getName());
      }
      return true;
    }
    if(mode != player.getGameMode()) {
      player.setGameMode(mode);
      if(mode != player.getGameMode()) {
        sender.sendMessage("Game mode change for " + player.getName() + " failed!");
      }
      else if(player == sender) {
        Command.broadcastCommandMessage(sender, "Set own game mode to " + mode.toString() + " mode", false);
      }
      else {
        Command.broadcastCommandMessage(sender, "Set " + player.getName() + "'s game mode to " + mode.toString() + " mode", false);
      }
    }
    else {
      sender.sendMessage(player.getName() + " already has game mode " + mode.getValue());
    }
    return true;
  }
}
