package sbitems.skyblockitems.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sbitems.skyblockitems.items.ItemManager;

import java.util.List;

public class RegisterEvents implements Listener {

    private static int getIfEmpty(Inventory inv){
        for(int i = 0; i < inv.toString().length(); i++){
            if(inv.getItem(i) == null)
                return i;
        }
        return -1;
    }

    @EventHandler
    public static void PJ(PlayerJoinEvent e){
        e.getPlayer().getInventory().addItem(ItemManager.CustomItems.get("Wand"));
    }

    @EventHandler
    public static void PPUI(EntityPickupItemEvent e){
        if(!(e.getEntity() instanceof Player))return;
        Player p = (Player) e.getEntity();

        for (ItemStack itemStack : p.getInventory()){
            if(itemStack == null || itemStack.getType().equals(Material.AIR)) return;

            if(itemStack.isSimilar(e.getItem().getItemStack())){
                int emptySlot = getIfEmpty(p.getInventory());
                if(emptySlot == -1) {
                    e.setCancelled(true);
                    return;
                }
                ItemStack inItem = e.getItem().getItemStack();
                int inIAmt = inItem.getAmount();
                ItemStack inItemRef = inItem.clone();
                inItemRef.setAmount(1);

                e.setCancelled(true);
                e.getItem().remove();

                Integer maxStack = ItemManager.CustomItemsStackSize.get(inItemRef);

                int leftAmt = inIAmt;

                for(int i = 0; i < Math.floor(inIAmt/maxStack); i++){
                    ItemStack newItem = inItem.clone();

                    if(leftAmt-maxStack <= 0){
                        newItem.setAmount(leftAmt);
                    }else{
                        newItem.setAmount(leftAmt-(leftAmt-maxStack));
                    }

                    p.getInventory().setItem(emptySlot, newItem);
                    leftAmt -= maxStack;

                    emptySlot = getIfEmpty(p.getInventory());
                }

                if(leftAmt <= 0) break;
            }
        }
    }
}
