package sbitems.skyblockitems;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import sbitems.skyblockitems.events.RegisterEvents;
import sbitems.skyblockitems.items.ItemManager;

public final class SkyblockItems extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Registers the event handlers in RegisterEvents.java
        getServer().getPluginManager().registerEvents(new RegisterEvents(), this);

        // Basically reads and adds the items --> calls the class that calls the class that creates items.
        ItemManager.init();
    }

    @Override
    public void onDisable() {
        // IDK what this does. I just added it bc it sounded cool.
        HandlerList.unregisterAll((Plugin) this);
    }
}
