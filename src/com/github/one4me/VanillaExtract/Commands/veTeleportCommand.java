package com.github.one4me.VanillaExtract.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.TeleportCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.github.one4me.VanillaExtract.VanillaExtract;
import com.github.one4me.VanillaExtract.ImprovedOfflinePlayer.ImprovedOfflinePlayer;

public class veTeleportCommand extends TeleportCommand {
  @Override
  public boolean execute(CommandSender sender, String currentAlias, String[] args) {
    if(!VanillaExtract.hasPermissionWild(sender, this.getPermission())) {
      return true;
    }
    if(args.length < 1 || args.length > 4) {
      sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
      return false;
    }
    Player player;
    if(args.length == 1 || args.length == 3) {
      if(sender instanceof Player) {
        player = (Player)sender;
      }
      else {
        sender.sendMessage("Please provide a player!");
        return true;
      }
    }
    else {
      player = Bukkit.getPlayerExact(args[0]);
    }
    if(args.length < 3) {
      Player target = Bukkit.getPlayerExact(args[args.length - 1]);
      if(args[0].equalsIgnoreCase("everyone")) {
        Location tl;
        ImprovedOfflinePlayer iot = new ImprovedOfflinePlayer(args[args.length - 1]);
        if(target == null) {
          if(iot.exists()) {
            tl = iot.getMiscLocation();
          }
          else {
            sender.sendMessage("Can't find user " + args[args.length - 1] + ". No tp.");
            return true;
          }
        }
        else {
          tl = target.getLocation();
        }
        for(OfflinePlayer offlineplayer : Bukkit.getOfflinePlayers()) {
          if(offlineplayer.isOnline()) {
            offlineplayer.getPlayer().teleport(tl, TeleportCause.COMMAND);
          }
          ImprovedOfflinePlayer iop = new ImprovedOfflinePlayer(offlineplayer);
          if(iop.exists()) {
            iop.setMiscLocation(tl);
          }
        }
        if(target == null) {
          Command.broadcastCommandMessage(sender, "Teleported everyone to " + iot.getName());
        }
        else {
          Command.broadcastCommandMessage(sender, "Teleported everyone to " + target.getName());
        }
      }
      else if(player == null || target == null) {
        ImprovedOfflinePlayer iop = new ImprovedOfflinePlayer(args[0]);
        ImprovedOfflinePlayer iot = new ImprovedOfflinePlayer(args[args.length - 1]);
        if(player != null && target == null) {
          if(!iot.exists()) {
            sender.sendMessage("Can't find user " + iot.getName() + ". No tp.");
            return true;
          }
          player.teleport(iot.getMiscLocation(), TeleportCause.COMMAND);
          Command.broadcastCommandMessage(sender, "Teleported " + player.getName() + " to " + iot.getName());
        }
        else if(player == null && target != null) {
          if(!iop.exists()) {
            sender.sendMessage("Can't find user " + iop.getName() + ". No tp.");
            return true;
          }
          iop.setMiscLocation(target.getLocation());
          Command.broadcastCommandMessage(sender, "Teleported " + iop.getName() + " to " + target.getName());
        }
        else {
          if(!iop.exists()) {
            sender.sendMessage("Can't find user " + iop.getName() + ". No tp.");
            return true;
          }
          if(!iot.exists()) {
            sender.sendMessage("Can't find user " + iot.getName() + ". No tp.");
            return true;
          }
          iop.setMiscLocation(iot.getMiscLocation());
          Command.broadcastCommandMessage(sender, "Teleported " + iop.getName() + " to " + iot.getName());
        }
      }
      else {
        player.teleport(target, TeleportCause.COMMAND);
        Command.broadcastCommandMessage(sender, "Teleported " + player.getName() + " to " + target.getName());
      }
    }
    else {
      int x = getInteger(sender, args[args.length - 3], -30000000, 30000000);
      int y = getInteger(sender, args[args.length - 2], 0, 256);
      int z = getInteger(sender, args[args.length - 1], -30000000, 30000000);
      ImprovedOfflinePlayer p = new ImprovedOfflinePlayer(args[0].toLowerCase());
      if(args[0].equalsIgnoreCase("everyone")) {
        for(OfflinePlayer offlineplayer : Bukkit.getOfflinePlayers()) {
          if(offlineplayer.isOnline()) {
            if(offlineplayer.getPlayer().getWorld() != null) {
              offlineplayer.getPlayer().teleport(new Location(offlineplayer.getPlayer().getWorld(), x, y, z), TeleportCause.COMMAND);
            }
          }
          ImprovedOfflinePlayer iop = new ImprovedOfflinePlayer(offlineplayer);
          if(iop.exists()) {
            if(iop.getMiscLocation().getWorld() != null) {
              iop.setMiscLocation(new Location(iop.getMiscLocation().getWorld(), x, y, z));
            }
          }
        }
      }
      else if(player == null) {
        if(p.exists()) {
          if(p.getMiscLocation().getWorld() != null) {
            p.setMiscLocation(new Location(p.getMiscLocation().getWorld(), x, y, z));
          }
        }
        else {
          sender.sendMessage("Can't find user " + p.getName() + ". No tp.");
          return true;
        }
      }
      else {
        Location location = new Location(player.getWorld(), x, y, z);
        player.teleport(location);
      }
      Command.broadcastCommandMessage(sender, "Teleported " + p.getName() + " to " + + x + "," + y + "," + z);
    }
    return true;
  }
}
