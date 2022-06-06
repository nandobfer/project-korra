package me.schuster.addons.ability.air.AirJet;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;

/**
 * Listener for the AirJet ability
 * 
 * @author schuster
 *
 */
public class AirJetListener implements Listener {

	@EventHandler
	public void onSneaking(PlayerInteractEvent event) {
		if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK)
			return;

		Player player = event.getPlayer();
		
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		if (bPlayer == null)
			return;
		
		if (bPlayer.canBend(CoreAbility.getAbility(AirJet.class))) {
			new AirJet(player);
		}
	}
}
