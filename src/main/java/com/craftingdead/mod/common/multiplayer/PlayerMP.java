package com.craftingdead.mod.common.multiplayer;

import java.util.UUID;

import com.craftingdead.mod.common.CraftingDead;
import com.craftingdead.mod.common.item.TriggerableItem;
import com.craftingdead.mod.common.multiplayer.message.MessageUpdateStatistics;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Wraps around the vanilla {@link EntityPlayerMP} so we can associate our own
 * data, functions and logic. This class performs most of the logic associated
 * with the player and forwards data to the client's
 * {@link com.craftingdead.mod.client.multiplayer.PlayerSP} via messages
 * 
 * @author Sm0keySa1m0n
 *
 */
public class PlayerMP implements INBTSerializable<NBTTagCompound>, ITickable {

	/**
	 * The vanilla entity
	 */
	private final EntityPlayerMP entity;
	/**
	 * The server instance used by this session
	 */
	private LogicalServer server;
	/**
	 * Used to calculate if a day has passed by
	 */
	private boolean lastDay;
	/**
	 * Used to calculate {@link #daysSurvived}
	 */
	private int timeAlive;
	/**
	 * Days survived
	 */
	private int daysSurvived;
	/**
	 * Used to detect if {@link #daysSurvived} has changed since it was last sent to
	 * the client
	 */
	private int lastDaysSurvived = Integer.MIN_VALUE;
	/**
	 * Zombie kills
	 */
	private int zombieKills;
	/**
	 * Used to detect if {@link #zombieKills} has changed since it was last sent to
	 * the client
	 */
	private int lastZombieKills = Integer.MIN_VALUE;
	/**
	 * Player kills
	 */
	private int playerKills;
	/**
	 * Used to detect if {@link #playerKills} has changed since it was last sent to
	 * the client
	 */
	private int lastPlayerKills = Integer.MIN_VALUE;

	private boolean triggerDown;
	private int triggerDownTicks = 0;
	private int lastTriggerDownTicks = Integer.MIN_VALUE;

	public PlayerMP(LogicalServer server, EntityPlayerMP entity) {
		this.server = server;
		this.entity = entity;
	}

	public EntityPlayerMP getVanillaEntity() {
		return entity;
	}

	/**
	 * Send a message to the player's client
	 * 
	 * @param message - the {@link IMessage} to send
	 */
	public void sendMessage(IMessage msg) {
		CraftingDead.NETWORK_WRAPPER.sendTo(msg, this.getVanillaEntity());
	}

	public LogicalServer getLogicalServer() {
		return this.server;
	}

	/**
	 * Serialises the class to a {@link NBTTagCompound} so it can be saved to
	 * {@link EntityPlayerMP#getEntityData()}
	 */
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("timeAlive", timeAlive);
		nbt.setInteger("daysSurvived", daysSurvived);
		nbt.setInteger("zombieKills", zombieKills);
		nbt.setInteger("playerKills", playerKills);
		return nbt;
	}

	/**
	 * Deserialises the class from an {@link NBTTagCompound} which is usually read
	 * from {@link EntityPlayerMP#getEntityData()}
	 */
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.timeAlive = nbt.getInteger("timeAlive");
		this.daysSurvived = nbt.getInteger("daysSurvived");
		this.zombieKills = nbt.getInteger("zombieKills");
		this.playerKills = nbt.getInteger("playerKills");
	}

	/**
	 * Update the player
	 */
	@Override
	public void update() {
		boolean isDay = entity.getEntityWorld().isDaytime();
		// If it was night time and is now day time then increment their days survived
		// by 1
		if (!lastDay && isDay) {
			daysSurvived++;
		}
		lastDay = isDay;

		if (this.daysSurvived != this.lastDaysSurvived || this.zombieKills != this.lastZombieKills
				|| this.playerKills != this.lastPlayerKills) {
			this.sendMessage(new MessageUpdateStatistics(this.daysSurvived, this.zombieKills, this.playerKills));
			this.lastDaysSurvived = this.daysSurvived;
			this.lastZombieKills = this.zombieKills;
			this.lastPlayerKills = this.playerKills;
		}
	}

	public void incrementZombieKills() {
		this.zombieKills++;
	}

	public void incrementPlayerKills() {
		this.playerKills++;
	}

	public void handleItemTrigger(boolean cancel) {
		// Never trust the client, they may not even have a gun
		if (entity.getHeldItemMainhand().getItem() instanceof TriggerableItem) {
			System.out.println("SERVER: Fire gun, should cancel? => " + cancel);
		}
	}

	/**
	 * Called when the player's health reaches 0
	 * 
	 * @param cause - the cause of death
	 */
	public void onDeath(DamageSource cause) {
		this.timeAlive = 0;
		this.daysSurvived = 0;
		this.zombieKills = 0;
		this.playerKills = 0;
	}

	public UUID getUUID() {
		return this.entity.getPersistentID();
	}

}
