package me.otisdiver.otisprojectile.targeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

import me.otisdiver.otisprojectile.OtisProjectile;
import me.otisdiver.otisprojectile.Utils;

public class EnderPearlTargeter extends Targeter {

    // Target we're trying to get to.
    private Entity target;

    public EnderPearlTargeter(OtisProjectile main, Projectile projectile) {
        super(main, projectile);
    }

    @Override
    public void updateTarget() {
        if (isProjectileGone()) {
            cancelTargetingTask();
            return;
        }
        
        if (target == null) return;
        
        Location projectileLocation = projectile.getLocation();
        Location targetLocation = target.getLocation();
        
        if (Utils.calculateDistance(projectileLocation, targetLocation) > 4) {
            projectile.setVelocity(Utils.getLocationsVectorDifference(projectileLocation, targetLocation));
        }
    }

    @Override
    public boolean beginTargeting() {
        // Find the Player that shot the ender pearl. Fails if a player didn't shoot it.
        ProjectileSource ps = projectile.getShooter();
        if (!(ps instanceof Player)) return false;
        Player player = (Player) ps;
        
        // Find the nearest living entity to the block where the player is looking
        // TODO: better solution via NMS
        Block targetBlock = player.getTargetBlock((Set<Material>) null, 64);
        ArrayList<Entity> entities = new ArrayList<Entity>(Arrays.asList(targetBlock.getChunk().getEntities()));
        /// Exclude the player
        Iterator<Entity> it = entities.iterator();
        while (it.hasNext()) {
            Entity e = it.next();
            if (player.equals(e)) it.remove();
        }
        target = Utils.getNearestEntityInList(targetBlock.getLocation(), entities);
        
        // Make the player follow the pearl.
        projectile.setPassenger(player);
        
        // Start (actual) targeting, report success of this task.
        return super.beginTargeting();
    }

}
