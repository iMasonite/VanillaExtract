package com.github.one4me.VanillaExtract.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.KickCommand;
import org.bukkit.entity.Player;

import com.github.one4me.VanillaExtract.VanillaExtract;

public class veKickCommand extends KickCommand {
  @Override
  public boolean execute(CommandSender sender, String currentAlias, String[] args) {
    if(!VanillaExtract.hasPermissionWild(sender, this.getPermission())) {
      return true;
    }
    if(args.length < 1 || args[0].length() == 0) {
      sender.sendMessage(ChatColor.RED + "Usage: " + this.usageMessage);
      return false;
    }
    if(args[0].equalsIgnoreCase("everyone")) {
      String reason = "You have been kicked by: " + sender.getName();
      if(args.length > 1) {
        reason = ChatColor.translateAlternateColorCodes('&', buildMessage(args, 1));
        if(reason.contains("§")) {
          StringBuilder sb = new StringBuilder(reason);
          int i = reason.length() - reason.replace("§", "").length();
          for(int offset = 0; offset < i; offset++) {
            sb.insert(0, " ");
          }
          reason = sb.toString();
        }
      }
      for(Player p : Bukkit.getOnlinePlayers()) {
        p.kickPlayer(reason);
      }
      Command.broadcastCommandMessage(sender, "Kicked everyone. With reason:\n" + reason.trim());
    }
    else {
      Player player = Bukkit.getPlayerExact(args[0]);
      if(player != null) {
        String reason = "You have been kicked by: " + sender.getName();
        if(args.length > 1) {
          reason = ChatColor.translateAlternateColorCodes('&', buildMessage(args, 1));
          if(reason.contains("§")) {
            StringBuilder sb = new StringBuilder(reason);
            int i = reason.length() - reason.replace("§", "").length();
            for(int offset = 0; offset < i; offset++) {
              sb.insert(0, " ");
            }
            reason = sb.toString();
          }
        }
        player.kickPlayer(reason);
        Command.broadcastCommandMessage(sender, "Kicked player " + player.getName() + ". With reason:\n" + reason.trim());
      }
      else {
        sender.sendMessage( args[0] + " not found.");
      }
    }
    return true;
  }
  public String buildMessage(String[] input, int startArg) {
    if(input.length <= startArg) {
      return "";
    }
    StringBuilder sb = new StringBuilder(input[startArg]);
    for(startArg++; startArg < input.length; startArg++) {
      sb.append(' ').append(input[startArg]);
    }
    return sb.toString();
  }
}
