package me.otisdiver.otisprojectile.targeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

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
        projectile.setVelocity(target.getLocation().toVector());
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
        target = Utils.getNearestEntityInList(targetBlock.getLocation(), entities);
        
        // Start targeting, report success of this task.
        return super.beginTargeting();
    }

}
