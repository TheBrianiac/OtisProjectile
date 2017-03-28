package me.otisdiver.otisprojectile.targeting;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;

import me.otisdiver.otisprojectile.OtisProjectile;
import me.otisdiver.otisprojectile.Utils;

public class ArrowTargeter extends Targeter {
    
    // Range in blocks that the arrow will search for potential targets.
    private static final int searchRange = 5;
    // Time in seconds that the arrow will attempt to hit a target before giving up.
    private static final int followTime = 3;
    
    public ArrowTargeter(OtisProjectile main, Projectile projectile) {
        super(main, projectile);
    }
    
    @Override
    public void updateTarget() {
        if (isProjectileGone()) {
            cancelTargetingTask();
            return;
        }
        
        Location projectileLocation = projectile.getLocation();
        
        // Get a list of all entities within the search range.
        List<Entity> nearbyEntities = Utils.getNearbyEntitiesList(projectile, searchRange);
        // Remove from the list any entities that aren't living entities (players/mobs).
        for (int i = 0; i < nearbyEntities.size(); i++) {
            Entity e = nearbyEntities.get(i);
            if (!(e instanceof LivingEntity)) nearbyEntities.remove(i);
            if (projectile.getShooter().equals(e)) nearbyEntities.remove(i);
        }
        
        // Find the nearest entity from that list, save it.
        Entity target = Utils.getNearestEntityInList(projectileLocation, nearbyEntities);
        if (target == null) return;
        
        // Adjust velocity.
        projectile.setVelocity(projectile.getVelocity().multiply(0));
        projectile.setVelocity(Utils.getLocationsVectorDifference(projectileLocation, target.getLocation()));
    }
    
    @Override
    public boolean cancelTargetingTask() {
        if (!isTargetingTaskRunning()) return false;
        
        Bukkit.getScheduler().cancelTask(targetingTask);
        targetingTask = 0;
        return true;
    }
    
    @Override
    public boolean beginTargeting() {
        // Try to set a new targeting task.
        if (super.beginTargeting()) {
            // If successful, also schedule canceling it. Then, report success.
            Bukkit.getScheduler().scheduleSyncDelayedTask(main, super.runnableCancelTargetingTask(), followTime * 20);
            return true;
        }
        else {
            // If unsuccessful, report failure.
            return false;
        }
    }
}
