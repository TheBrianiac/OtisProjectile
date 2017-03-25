package me.otisdiver.otisprojectile;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/** Main class. Extends JavaPlugin.
 * 
 * @author Brian Schultz */
public class OtisProjectile extends JavaPlugin {

    @Override
    // Runs when plugin is loaded.
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new ProjectileListener(this), this);
    }
    
    @Override
    // Runs when plugin us unloaded.
    public void onDisable() {
        
    }
    
}
