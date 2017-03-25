package me.otisdiver.otisprojectile;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import me.otisdiver.otisprojectile.targeting.ArrowTargeter;
import me.otisdiver.otisprojectile.targeting.EggTargeter;
import me.otisdiver.otisprojectile.targeting.EnderPearlTargeter;
import me.otisdiver.otisprojectile.targeting.Targeter;

public class ProjectileListener implements Listener {

    private OtisProjectile main;
    
    public ProjectileListener(OtisProjectile main) {
        this.main = main;
    }
    
    // Listener for projectile launch events.
    @EventHandler(ignoreCancelled = true)
    public void projectileLaunch(ProjectileLaunchEvent e) {
        
        Projectile projectile = e.getEntity();
        
        // Stop if the projectile was not shot by a player
        if (!(projectile.getShooter() instanceof Player)) return;
        
        // Create an appropriate targeter for the projectile type.
        EntityType type = e.getEntityType();
        Targeter targeter = null;
        switch (type) {
            case ARROW:
                targeter = new ArrowTargeter(main, projectile);
                break;
            case EGG:
                targeter = new EggTargeter(main, projectile);
                break;
            case ENDER_PEARL:
                targeter = new EnderPearlTargeter(main, projectile);
                break;
            default:
                return; // stop
        }
        
        targeter.beginTargeting();
    }
    
}
