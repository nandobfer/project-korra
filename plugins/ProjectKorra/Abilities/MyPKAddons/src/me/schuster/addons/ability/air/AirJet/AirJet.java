package me.schuster.addons.ability.air.AirJet;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * AirJet
 * Launches the player in the air with high speed
 * Useful to get out of holes, escape from lava and swim back to surface
 * 
 * @author schuster
 * @version 0.1.0
 *
 */
public class AirJet extends AirAbility implements AddonAbility {

//	@Attribute(Attribute.COOLDOWN)
	private long cooldown;

//	@Attribute(Attribute.HEIGHT)
	private long duration;
	
	private Listener listener;
	private Permission permission;
	
	public AirJet(Player player) {
		super(player);
		
		// doesn't fire if on cooldown
		if (bPlayer.isOnCooldown(this)) {
			return;
		}

		final AirJet airJet = getAbility(player, AirJet.class);
		if (airJet != null) {
			airJet.remove();
			return;
		}

		if (!this.bPlayer.canBend(this)) {
			return;
		}
		

		// configure initial state
		setFields();
		
		// fire the ability
		start();
	}
	
	/** Configure the initial state of the ability */
	private void setFields() {
//		cooldown = getConfig().getLong("Abilities.Air.AirJet.Cooldown");
//		height = getConfig().getDouble("Abilities.Air.AirJet.Duration");
		cooldown = 1000;
		duration = 3000;
		
		flightHandler.createInstance(player, getName());
	}

	private void allowFlight() {
		if (!this.player.getAllowFlight()) {
			this.player.setAllowFlight(true);
		}
		if (!this.player.isFlying()) {
			this.player.setFlying(true);
		}
	}
	
	private Block getGround() {
		final Block standingblock = this.player.getLocation().getBlock();
		for (int i = 0; i <= 5; i++) {
			final Block block = standingblock.getRelative(BlockFace.DOWN, i);
			if (GeneralMethods.isSolid(block)) {
				return block;
			}
		}
		return null;
	}
	
	/**
	 * Determine what the ability should do as time passes
	 * and the player's state changes
	 */
	@Override
	public void progress() {
		if (player.isDead() || !player.isOnline() || !bPlayer.canBendIgnoreBinds(this) || !bPlayer.canBind(this)) {
			remove();
			return;
		} else if (duration != 0 && System.currentTimeMillis() > getStartTime() + duration) {
			bPlayer.addCooldown(this);
			remove();
			return;
		} else if (player.isSneaking()) {
			bPlayer.addCooldown(this);
			remove();
			return;
		}

		player.setFallDistance(0);
		player.setSprinting(false);
	
		final Block ground = getGround();
		if (ground != null) {
			this.allowFlight();
		}

		playAbilityParticles();
		playAirbendingSound(player.getLocation());
	}
	
	private void playAbilityParticles() {

		Location playerLoc = player.getLocation();
		Location particleLoc = new Location(
			playerLoc.getWorld(), 
			playerLoc.getX(),
			playerLoc.getY(),
			playerLoc.getZ()
		);

		for (int i = 1; i <= 10; i++) {
			final Location particleLoc2 = new Location(
				particleLoc.getWorld(),
				particleLoc.getX(),
				playerLoc.getY() - i,
				particleLoc.getZ()
			);
			
			playAirbendingParticles(particleLoc2, 1, 0.4F, 0.4F, 0.4F);
		}
		
	}

	@Override
	public void remove() {
		super.remove();
		flightHandler.removeInstance(player, getName());
	}
	
	/*
	 * This method is run whenever the ability is loaded into a server.
	 * Restart/reload
	 */
	@Override
	public void load() {
		// the event listener for the ability
		listener = new AirJetListener();
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(listener, ProjectKorra.plugin);
		permission = new Permission("bending.ability.airjet");
		permission.setDefault(PermissionDefault.OP);
		ProjectKorra.plugin.getServer().getPluginManager().addPermission(permission);
	}

	@Override
	public void stop() {
		HandlerList.unregisterAll(listener);
		ProjectKorra.plugin.getServer().getPluginManager().removePermission(permission);
	}

	@Override
	public long getCooldown() {
		return cooldown;
	}

	@Override
	public Location getLocation() {
		return this.player != null ? this.player.getLocation() : null;
	}

	/*
	 * Is this ability inoffensive?
	 */
	@Override
	public boolean isHarmlessAbility() {
		return true;
	}

	@Override
	public boolean isSneakAbility() {
		return true;
	}
	
	
	/*
	 * The description for the ability.
	 * Displays in /b h [abilityname]
	 */
	@Override
	public String getDescription() {
		return "Launches the bender in the air with high speed\n"
				+ "Useful to get out of holes, escape from lava and swim back to surface!";
	}
	
	/*
	 * The instruction for the ability.
	 * Displays in /b h [abilityname]
	 */
	@Override
	public String getInstructions() {
		return "Left click with the ability selected to fire the jet and be lifted up\n"
				+ "Sneak to stop the ability";
	}

	@Override
	public String getName() {
		return "AirJet";
	}

	/*
	 * The author of the ability.
	 * Displays in /b h [abilityname] in more recent versions of ProjectKorra.
	 */
	@Override
	public String getAuthor() {
		return "Dahan Schuster";
	}

	/*
	 * The version of the ability.
	 * Displays in /b h [abilityname] in more recent versions of ProjectKorra.
	 */
	@Override
	public String getVersion() {
		return "0.1.0";
	}
}
