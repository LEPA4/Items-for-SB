package sbitems.skyblockitems.items;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LunchBox implements Listener {

    public static Map<UUID, Inventory> PlayerInv = new HashMap<>();

    public static void openLunchBoxGUI(Player p){
        // Checks if player has already used the lunch bock, if so, then create a new one.
        if(PlayerInv.containsKey(p.getUniqueId()))
            p.openInventory(PlayerInv.get(p.getUniqueId()));
        else{
            Inventory inv = Bukkit.createInventory(p, 36, "Lunch Box");
            p.openInventory(inv);
            PlayerInv.put(p.getUniqueId(), inv);
        }
    }


}
