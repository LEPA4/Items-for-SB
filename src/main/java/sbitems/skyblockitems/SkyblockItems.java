package sbitems.skyblockitems;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import sbitems.skyblockitems.events.RegisterEvents;
import sbitems.skyblockitems.items.ItemManager;
import sbitems.skyblockitems.items.LunchBox;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class SkyblockItems extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Basically reads and adds the items --> calls the class that calls the class that creates items.
        ItemManager.init();

        // Registers the event handlers
        getServer().getPluginManager().registerEvents(new RegisterEvents(), this);
        getServer().getPluginManager().registerEvents(new LunchBox(), this);

        // Loads from data file
        this.saveConfig();

        if(this.getConfig().contains("data"))
            this.load();
    }

    @Override
    public void onDisable() {
        // Saves to data file
        if(!LunchBox.PlayerInv.isEmpty())
            this.save();
        // IDK what this does. I just added it bc it sounded cool.
        HandlerList.unregisterAll((Plugin) this);
    }

    public void save(){
        for(UUID user : LunchBox.PlayerInv.keySet()){
            this.getConfig().set("data." + user, LunchBox.PlayerInv.get(user).getContents());
        }
        this.saveConfig();
    }
    public void load(){
        this.getConfig().getConfigurationSection("data").getKeys(false).forEach(key -> {
            Inventory saveinv = Bukkit.createInventory(null, 36, "Lunch Box");
            ItemStack[] items = ((List<ItemStack>) this.getConfig().get("data." + key)).toArray(new ItemStack[0]);
            int index = 0;
            for(ItemStack i : items){
                saveinv.setItem(index, i);
                index++;
            }
            LunchBox.PlayerInv.put(UUID.fromString(key), saveinv);
        });
    }
}
