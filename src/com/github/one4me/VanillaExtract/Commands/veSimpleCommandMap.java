package com.github.one4me.VanillaExtract.Commands;

import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BanCommand;
import org.bukkit.command.defaults.BanIpCommand;
import org.bukkit.command.defaults.BanListCommand;
import org.bukkit.command.defaults.DefaultGameModeCommand;
import org.bukkit.command.defaults.DeopCommand;
import org.bukkit.command.defaults.ExpCommand;
import org.bukkit.command.defaults.GiveCommand;
import org.bukkit.command.defaults.HelpCommand;
import org.bukkit.command.defaults.ListCommand;
import org.bukkit.command.defaults.MeCommand;
import org.bukkit.command.defaults.OpCommand;
import org.bukkit.command.defaults.PardonCommand;
import org.bukkit.command.defaults.PardonIpCommand;
import org.bukkit.command.defaults.SaveCommand;
import org.bukkit.command.defaults.SaveOffCommand;
import org.bukkit.command.defaults.SaveOnCommand;
import org.bukkit.command.defaults.SayCommand;
import org.bukkit.command.defaults.SeedCommand;
import org.bukkit.command.defaults.StopCommand;
import org.bukkit.command.defaults.TellCommand;
import org.bukkit.command.defaults.TimeCommand;
import org.bukkit.command.defaults.ToggleDownfallCommand;
import org.bukkit.command.defaults.WhitelistCommand;

public class veSimpleCommandMap extends SimpleCommandMap {
  public veSimpleCommandMap(Server server) {
    super(server);
  }
  static {
    fallbackCommands.clear();
    fallbackCommands.add(new ListCommand());
    fallbackCommands.add(new StopCommand());
    fallbackCommands.add(new SaveCommand());
    fallbackCommands.add(new SaveOnCommand());
    fallbackCommands.add(new SaveOffCommand());
    fallbackCommands.add(new OpCommand());
    fallbackCommands.add(new DeopCommand());
    fallbackCommands.add(new BanIpCommand());
    fallbackCommands.add(new PardonIpCommand());
    fallbackCommands.add(new BanCommand());
    fallbackCommands.add(new PardonCommand());
    fallbackCommands.add(new veKickCommand());
    fallbackCommands.add(new veTeleportCommand());
    fallbackCommands.add(new GiveCommand());
    fallbackCommands.add(new TimeCommand());
    fallbackCommands.add(new SayCommand());
    fallbackCommands.add(new WhitelistCommand());
    fallbackCommands.add(new TellCommand());
    fallbackCommands.add(new MeCommand());
    fallbackCommands.add(new veKillCommand());
    fallbackCommands.add(new veGameModeCommand());
    fallbackCommands.add(new HelpCommand());
    fallbackCommands.add(new ExpCommand());
    fallbackCommands.add(new ToggleDownfallCommand());
    fallbackCommands.add(new BanListCommand());
    fallbackCommands.add(new DefaultGameModeCommand());
    fallbackCommands.add(new SeedCommand());
  }
}
