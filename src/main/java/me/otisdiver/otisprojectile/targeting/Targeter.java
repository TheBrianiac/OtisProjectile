package me.otisdiver.otisprojectile.targeting;

import java.lang.ref.WeakReference;

import org.bukkit.Bukkit;
import org.bukkit.entity.Projectile;

import me.otisdiver.otisprojectile.OtisProjectile;

public abstract class Targeter {
    
    // Running instance of the JavaPlugin.
    protected OtisProjectile main = null;
    
    // The serviced projectile.
    protected Projectile projectile = null;
    
    // Currently running targeting task ID.
    private int targetingTask = 0;
    
    public Targeter(OtisProjectile main, Projectile projectile) {
        this.main = main;
        this.projectile = projectile;
    }
    
    /** Check if the projectile is gone (marked for removal or removed).
     * 
     * @return true, if the entity is gone
     */
    protected boolean isProjectileGone() {
        return (projectile.isDead() && !projectile.isValid());
    }
    
    /** Check if a targeting task has been started.
     * 
     * @return true, if no targeting task ID has been saved.
     */
    protected boolean isTargetingTaskRunning() {
        return targetingTask != 0;
    }
    
    /** Saves a new task ID if none had been set.
     * 
     * @param ID the task ID of the running task
     * @return true, if no targeting task was saved and one was started
     */
    protected boolean setTargetingTask(int ID) {
        if (isTargetingTaskRunning()) return false;
        
        targetingTask = ID;
        return true;
    }
    
    /** Cancels the task with the ID of the last saved targeting task.
     * 
     * @return true, if a targeting task was saved and cancelled
     */
    protected boolean cancelTargetingTask() {
        if (!isTargetingTaskRunning()) return false;
        
        Bukkit.getScheduler().cancelTask(targetingTask);
        targetingTask = 0;
        return true;
    }
    
    /** Adjusts the serviced projectile's course according to internal constants. */
    public abstract void updateTarget();
    
    /** Starts a repeating task to adjust the projectile's course, lasting as long as determined by internal constants. */
    public boolean beginTargeting() {
        // Cancel any existing tasks.
        cancelTargetingTask();
        
        // Start new one, pass on its report (true/false).
        return setTargetingTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(main, runnableUpdateTarget(), 1, 1));
    }
    
    /** Returns a runnable that calls the class's updateTarget method. */
    public Runnable runnableUpdateTarget() {
        // Save a reference to this object in the runnable, but not one that forces this object to exist.
        final WeakReference<Targeter> t = new WeakReference<Targeter>(this);
        return new Runnable() { public void run() {
                t.get().updateTarget();
            }
        };
    }
    
    /** Returns a runnable that calls the class's cancelTargetingTask method. */
    public Runnable runnableCancelTargetingTask() {
        // Save a reference to this object in the runnable, but not one that forces this object to exist.
        final WeakReference<Targeter> t = new WeakReference<Targeter>(this);
        return new Runnable() { public void run() {
                t.get().cancelTargetingTask();
            }
        };
    }
}
