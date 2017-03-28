package me.otisdiver.otisprojectile.targeting;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;

import me.otisdiver.otisprojectile.OtisProjectile;
import me.otisdiver.otisprojectile.Utils;

public class EggTargeter extends Targeter {
    
    private static int searchRange = 20;
    
    // Entity the egg has "locked on" to.
    private Entity target;
    private boolean lastUpdateFailed = false;
    
    public EggTargeter(OtisProjectile main, Projectile projectile) {
        super(main, projectile);
    }
    
    private boolean identifyTarget() {
        // Get a list of all entities within the search range.
        List<Entity> nearbyEntities = Utils.getNearbyEntitiesList(projectile, searchRange);
        // Remove from the list any entities that aren't hostile mobs.
        for(int i = 0; i < nearbyEntities.size(); i++) {
            Entity e = nearbyEntities.get(i);
            if (!Utils.isEntityHostile(e)) nearbyEntities.remove(i);
            if (projectile.getShooter().equals(e)) nearbyEntities.remove(i);
        }
        
        // Find the nearest entity. If it exists, update the target and report success.
        Entity entity = Utils.getNearestEntityInList(projectile.getLocation(), nearbyEntities);
        if (entity != null) {
            target = entity;
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public void updateTarget() {
        if (isProjectileGone()) {
            cancelTargetingTask();
            return;
        }
        
        // If there's no target, try to find one. If that fails, stop.
        if (target == null || target.isDead()) {
            if (!identifyTarget()) return;
        }
        
        Location projectileLocation = projectile.getLocation();
        Location targetLocation = target.getLocation();
        
        // Continue chasing the target if it's still within the search range.
        if (Utils.calculateDistance(projectileLocation, targetLocation) < searchRange) {
            projectile.setVelocity(Utils.getLocationsVectorDifference(projectileLocation, targetLocation));
            lastUpdateFailed = false;
        }
        // If the attempt fails, and the last attempt also failed, give up (will find new target on next tick).
        else if (lastUpdateFailed) {
            target = null;
            lastUpdateFailed = false;
        }
        // If the attempt fails, but the last one didn't, mark that this one did.
        else {
            lastUpdateFailed = true;
        }
    }
    
}
