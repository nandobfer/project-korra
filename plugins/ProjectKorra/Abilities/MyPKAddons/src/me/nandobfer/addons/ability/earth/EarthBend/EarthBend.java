package me.nandobfer.addons.ability.earth.EarthBend;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;

public class EarthBend extends EarthAbility implements AddonAbility {

    private EarthBendListener listener;
    private Permission permission;
    private Block block;

    public EarthBend(Player player) {
        super(player);
        EarthBend old = getAbility(player, EarthBend.class);
        if (old != null) {
            old.remove();
            return;
        }
        if (!bPlayer.canBend(this)) {
            return;
        }

        this.start();
    }

    @Override
    public void progress() {
        if (player.isDead() || !player.isOnline() || !bPlayer.canBind(this)) {
            remove();
            return;
        }
        block = player.getTargetBlock(null, 50);
        block.setType(Material.AIR);
        remove();

    }

    @Override
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public boolean isHarmlessAbility() {
        return true;
    }

    @Override
    public long getCooldown() {
        return 0;
    }

    @Override
    public String getName() {
        return "EarthBend";
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public void load() {
        listener = new EarthBendListener();
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(listener, ProjectKorra.plugin);
        permission = new Permission("bending.ability.earthbend");
        permission.setDefault(PermissionDefault.OP);
        ProjectKorra.plugin.getServer().getPluginManager().addPermission(permission);

    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(listener);
        ProjectKorra.plugin.getServer().getPluginManager().removePermission(permission);

    }

    @Override
    public String getAuthor() {
        // TODO Auto-generated method stub
        return "Burgos & Schuster";
    }

    @Override
    public String getVersion() {
        // TODO Auto-generated method stub
        return "0.1.0";
    }

}
