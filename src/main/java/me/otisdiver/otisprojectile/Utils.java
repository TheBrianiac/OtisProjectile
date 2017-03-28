package me.otisdiver.otisprojectile;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class Utils {
    
    /** Calculates the pythagorean distance between two points.
     * 
     * @param locA the first location value
     * @param locB the second location value
     * @return the distance between the two locations
     */
    public static double calculateDistance(Location locA, Location locB) {
        double dx = locA.getX() - locB.getX();
        double dy = locA.getY() - locB.getY();
        double dz = locA.getZ() - locB.getZ();
        
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    /** Finds all entities within a cube search area centered on the entity.
     * 
     * @param alpha the entity to search around
     * @param searchRange the width of the search cube
     * @return a list of entities nearby the original entity
     */
    public static List<Entity> getNearbyEntitiesList(Entity alpha, int searchRange) {
        // Find all entities within a cube area the width of the search range that's centered on the entity.
        double radius = searchRange / 2;
        return alpha.getNearbyEntities(radius, radius, radius);
    }
    
    /** Finds the nearest entity to the given entity, within a certain range.
     * 
     * @param alpha the entity to search around
     * @param searchRange width of the cube area that will be searched around the entity
     * @return the nearest entity
     */
    public static Entity getNearestEntityInList(Location location, List<Entity> nearbyEntities) {
        
        // Make sure there are entities nearby. Stop if there aren't.
        if (nearbyEntities.isEmpty()) return null;
        
        Entity closestEntity = null;
        Double closestDistance = null;
        
        for (Entity e : nearbyEntities) {
            // Check whether the nearby entity is closer than the closest we've checked so far.
            Double distance = calculateDistance(location, e.getLocation());
            if (closestDistance == null || distance < closestDistance) {
                // If it is, then that's the new closest we've checked.
                closestEntity = e;
                closestDistance = distance;
            }
        }
        
        return closestEntity;
    }
    
    /** Find the vector difference between two locations.
     * 
     * @param alpha the current location
     * @param beta the location to adjust to
     * @return a vector that can be applied to an entity at point alpha to make it change direction to beta
     */
    public static Vector getLocationsVectorDifference(Location alpha, Location beta) {
        return beta.toVector().subtract(alpha.toVector());
    }
    
    /** Checks whether the entity is a hostile mob.
     * 
     * @param entity the entity to check
     * @return true, if the entity is hostile
     */
    public static boolean isEntityHostile(Entity entity) {
        
        // Assume that, if it isn't a LivingEntity, it isn't a mob.
        if (!(entity instanceof LivingEntity)) return false;
        
        // Return based on the entity's type.
        EntityType type = entity.getType();
        switch (type) {
            case BLAZE:
                return true;
            case CAVE_SPIDER:
                return true;
            case CREEPER:
                return true;
            case ELDER_GUARDIAN:
                return true;
            case ENDER_DRAGON:
                return true;
            case ENDERMAN:
                return true;
            case ENDERMITE:
                return true;
            case GHAST:
                return true;
            case GIANT:
                return true;
            case GUARDIAN:
                return true;
            case MAGMA_CUBE:
                return true;
            case PIG_ZOMBIE:
                return true;
            case SILVERFISH:
                return true;
            case SKELETON:
                return true;
            case SLIME:
                return true;
            case SPIDER:
                return true;
            case WITCH:
                return true;
            case WITHER:
                return true;
            case WITHER_SKELETON:
                return true;
            case ZOMBIE:
                return true;
            case ZOMBIE_VILLAGER:
                return true;
            default:
                return false;
        }
    }
}
