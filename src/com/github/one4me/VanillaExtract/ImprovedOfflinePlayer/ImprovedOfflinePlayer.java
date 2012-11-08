/*
 * Copyright (C) 2012 one4me@github.com
 */
package com.github.one4me.VanillaExtract.ImprovedOfflinePlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.server.InventoryEnderChest;
import net.minecraft.server.NBTCompressedStreamTools;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagDouble;
import net.minecraft.server.NBTTagFloat;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.PlayerInventory;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.io.Files;

/**
 * @name ImprovedOfflinePlayer
 * @version 1.1.0
 * @author one4me
 */
public class ImprovedOfflinePlayer {
  private String player;
  private File file;
  private NBTTagCompound compound;
  private boolean exists = false;
  public ImprovedOfflinePlayer(String playername) {
    this.exists = loadPlayerData(playername);
  }
  public ImprovedOfflinePlayer(OfflinePlayer offlineplayer) {
    this.exists = loadPlayerData(offlineplayer.getName());
  }
  private boolean loadPlayerData(String name) {
    try {
      this.player = name;
      for(World w : Bukkit.getWorlds()) {
        this.file = new File(w.getWorldFolder(), "players" + File.separator + this.player + ".dat");
        if(this.file.exists()){
          this.exists = true;
          this.compound = NBTCompressedStreamTools.a(new FileInputStream(this.file));
          this.player = this.file.getCanonicalFile().getName().replace(".dat", "");
          return true;
        }
        this.exists = false;
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    return false;
  }
  private void savePlayerData() {
    if(this.exists) {
      try {
        NBTCompressedStreamTools.a(this.compound, new FileOutputStream(this.file));
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }
  }
  public boolean exists() {
    return this.exists;
  }
  public String getName() {
    return this.player;
  }
  /**@param Incomplete**/
  public void copyDataTo(String playername) {
    try {
      if(!playername.equalsIgnoreCase(this.player)) {
        Player to = Bukkit.getPlayerExact(playername);
        Player from = Bukkit.getPlayerExact(this.player);
        if(from != null) {
          from.saveData();
        }
        Files.copy(this.file, new File(this.file.getParentFile(), playername + ".dat"));
        if(to != null) {
          to.teleport(from == null ? getMiscLocation() : from.getLocation());
          to.loadData();
        }
      }
      else {
        Player player = Bukkit.getPlayerExact(this.player);
        if(player != null) {
          player.saveData();
        }
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  public NBTTagCompound getFileData() {
    return this.compound;
  }
  public void setFileData(NBTTagCompound nbttagcompound) {
    this.compound = nbttagcompound;
    savePlayerData();
  }
  public float getFoodExhaustionLevel() {
    return this.compound.getFloat("foodExhaustionLevel");
  }
  public void setFoodExhaustionLevel(float input) {
    this.compound.setFloat("foodExhaustionLevel", input);
    savePlayerData();
  }
  public int getFoodLevel() {
    return this.compound.getInt("foodLevel");
  }
  public void setFoodLevel(int input) {
    this.compound.setInt("foodLevel", input);
    savePlayerData();
  }
  public float getFoodSaturationLevel() {
    return this.compound.getFloat("foodSaturationLevel");
  }
  public void setFoodSaturationLevel(float input) {
    this.compound.setFloat("foodSaturationLevel", input);
    savePlayerData();
  }
  public int getFoodTickTimer() {
    return this.compound.getInt("foodTickTimer");
  }
  public void setFoodTickTimer(int input) {
    this.compound.setInt("foodTickTimer", input);
    savePlayerData();
  }
  public ItemStack[] getInventoryArmor() {
    PlayerInventory inventory = new PlayerInventory(null);
    inventory.b(this.compound.getList("Inventory"));
    return new CraftInventoryPlayer(inventory).getArmorContents();
  }
  public void setInventoryArmor(ItemStack[] armor) {
    CraftInventoryPlayer inventory = new CraftInventoryPlayer(new PlayerInventory(null));
    inventory.setArmorContents(armor);
    inventory.setContents(getInventoryItems());
    this.compound.set("Inventory", inventory.getInventory().a(new NBTTagList()));
    savePlayerData();
  }
  public ItemStack[] getInventoryEnd() {
    InventoryEnderChest endchest = new InventoryEnderChest();
    endchest.a(this.compound.getList("EnderItems"));
    return new CraftInventory(endchest).getContents();
  }
  public void setInventoryEnd(ItemStack[] items) {
    CraftInventory endchest = new CraftInventory(new InventoryEnderChest());
    endchest.setContents(items);
    this.compound.set("EnderItems", ((InventoryEnderChest)endchest.getInventory()).g());
    savePlayerData();
  }
  public ItemStack[] getInventoryItems() {
    PlayerInventory inventory = new PlayerInventory(null);
    inventory.b(this.compound.getList("Inventory"));
    return new CraftInventoryPlayer(inventory).getContents();
  }
  public void setInventoryItems(ItemStack[] items) {
    CraftInventoryPlayer inventory = new CraftInventoryPlayer(new PlayerInventory(null));
    inventory.setContents(items);
    inventory.setArmorContents(getInventoryArmor());
    this.compound.set("Inventory", inventory.getInventory().a(new NBTTagList()));
    savePlayerData();
  }
  public boolean getIsGrounded() {
    return compound.getBoolean("OnGround");
  }
  public void setInGrounded(boolean input) {
    this.compound.setBoolean("OnGround", input);
    savePlayerData();
  }
  public boolean getIsSleeping() {
    return this.compound.getBoolean("Sleeping");
  }
  public void setIsSleeping(boolean input) {
    this.compound.setBoolean("Sleeping", input);
    savePlayerData();
  }
  public float getMiscFallDistance() {
    return this.compound.getFloat("FallDistance");
  }
  public void setMiscFallDistance(float input) {
    this.compound.setFloat("FallDistance", input);
    savePlayerData();
  }
  public int getMiscGameMode() {
    return this.compound.getInt("playerGameType");
  }
  public void setMiscGameMode(int input) {
    this.compound.setInt("playerGameType", input);
    savePlayerData();
  }
  public short getMiscHealth() {
    return this.compound.getShort("Health");
  }
  public void setMiscHealth(short input) {
    this.compound.setShort("Health", input);
    savePlayerData();
  }
  public Location getMiscLocation() {
    NBTTagList position = this.compound.getList("Pos");
    NBTTagList rotation = this.compound.getList("Rotation");
    Location location = new Location(
      Bukkit.getWorld(new UUID(this.compound.getLong("WorldUUIDMost"), this.compound.getLong("WorldUUIDLeast"))),
      ((NBTTagDouble)position.get(0)).data,
      ((NBTTagDouble)position.get(1)).data,
      ((NBTTagDouble)position.get(2)).data,
      ((NBTTagFloat)rotation.get(0)).data,
      ((NBTTagFloat)rotation.get(1)).data
    );
    return location;
  }
  public void setMiscLocation(Location location) {
    World w = location.getWorld();
    UUID uuid = w.getUID();
    this.compound.setLong("WorldUUIDMost", uuid.getMostSignificantBits());
    this.compound.setLong("WorldUUIDLeast", uuid.getLeastSignificantBits());
    this.compound.setInt("Dimension", w.getEnvironment().getId());
    NBTTagList position = new NBTTagList();
    position.add(new NBTTagDouble(null, location.getX()));
    position.add(new NBTTagDouble(null, location.getY()));
    position.add(new NBTTagDouble(null, location.getZ()));
    this.compound.set("Pos", position);
    NBTTagList rotation = new NBTTagList();
    rotation.add(new NBTTagFloat(null, location.getYaw()));
    rotation.add(new NBTTagFloat(null, location.getPitch()));
    this.compound.set("Rotation", rotation);
    savePlayerData();
  }
  public double[] getMiscVelocity() {
    double[] velocity = new double[3];
    NBTTagList list = this.compound.getList("Motion");
    velocity[0] = ((NBTTagDouble)list.get(0)).data;
    velocity[1] = ((NBTTagDouble)list.get(1)).data;
    velocity[2] = ((NBTTagDouble)list.get(2)).data;
    return velocity;
  }
  public void setMiscVelocity(double x, double y, double z) {
    NBTTagList motion = new NBTTagList();
    motion.add(new NBTTagDouble(null, x));
    motion.add(new NBTTagDouble(null, y));
    motion.add(new NBTTagDouble(null, z));
    this.compound.set("Motion", motion);
    savePlayerData();
  }
  public ArrayList<PotionEffect> getPotionEffects() {
    ArrayList<PotionEffect> abilities = new ArrayList<PotionEffect>();
    if(this.compound.hasKey("ActiveEffects")) {
      NBTTagList list = this.compound.getList("ActiveEffects");
      for (int i = 0; i < list.size(); i++) {
        NBTTagCompound effect = (NBTTagCompound)list.get(i);
        byte amp = effect.getByte("Amplifier");
        byte id = effect.getByte("Id");
        int time = effect.getInt("Duration");
        abilities.add(new PotionEffect(PotionEffectType.getById(id), time, amp));
      }
    }
    return abilities;
  }
  public void setPotionEffects(ArrayList<PotionEffect> effects) {
    if(effects.isEmpty()) {
      this.compound.remove("ActiveEffects");
      savePlayerData();
      return;
    }
    NBTTagList activeEffects = new NBTTagList();
    for(PotionEffect pe : effects) {
      NBTTagCompound eCompound = new NBTTagCompound();
      eCompound.setByte("Amplifier", (byte)(pe.getAmplifier()));
      eCompound.setByte("Id", (byte)(pe.getType().getId()));
      eCompound.setInt("Duration", (int)(pe.getDuration()));
      activeEffects.add(eCompound);
    }
    this.compound.set("ActiveEffects", activeEffects);
    savePlayerData();
  }
  public short getTicksAir() {
    return this.compound.getShort("Air");
  }
  public void setTicksAir(short input) {
    this.compound.setShort("Air", input);
    savePlayerData();
  }
  public short getTicksFire() {
    return this.compound.getShort("Fire");
  }
  public void setTicksFire(short input) {
    this.compound.setShort("Fire", input);
    savePlayerData();
  }
  public short getTimeAttack() {
    return this.compound.getShort("AttackTime");
  }
  public void setTimeAttack(short input) {
    this.compound.setShort("AttackTime", input);
    savePlayerData();
  }
  public short getTimeDeath() {
    return this.compound.getShort("DeathTime");
  }
  public void setTimeDeath(short input) {
    this.compound.setShort("DeathTime", input);
    savePlayerData();
  }
  public short getTimeHurt() {
    return this.compound.getShort("HurtTime");
  }
  public void setTimeHurt(short input) {
    this.compound.setShort("HurtTime", input);
    savePlayerData();
  }
  public short getTimeSleep() {
    return this.compound.getShort("SleepTime");
  }
  public void setTimeSleep(short input) {
    this.compound.setShort("SleepTime", input);
    savePlayerData();
  }
  public int getXpLevel() {
    return this.compound.getInt("XpLevel");
  }
  public void setXpLevel(int input) {
    this.compound.setInt("XpLevel", input);
    savePlayerData();
  }
  public float getXpProgress() {
    return this.compound.getFloat("XpP");
  }
  public void setXpProgress(float input) {
    this.compound.setFloat("XpP", input);
    savePlayerData();
  }
  public int getXpTotal() {
    return this.compound.getInt("XpTotal");
  }
  public void setXpTotal(int input) {
    this.compound.setInt("XpTotal", input);
    savePlayerData();
  }
}
/*
 * Copyright (C) 2012 one4me@github.com
 */