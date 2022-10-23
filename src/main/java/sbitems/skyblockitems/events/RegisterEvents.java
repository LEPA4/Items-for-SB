package sbitems.skyblockitems.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import sbitems.skyblockitems.items.ItemManager;

public class RegisterEvents implements Listener {

    @EventHandler
    public static void PJ(PlayerJoinEvent e){
        e.getPlayer().getInventory().addItem(ItemManager.CustomItems.get("Wand"));
    }

    @EventHandler
    public static void PPUI(EntityPickupItemEvent e){
        if(!(e.getEntity() instanceof Player))return;
        Player p = (Player) e.getEntity();

        if(p.getGameMode() != GameMode.SURVIVAL) return;

        ItemStack inItem = e.getItem().getItemStack();

        int inIAmt = inItem.getAmount();
        int leftAmt = inIAmt;
        ItemStack inItemRef = inItem.clone();
        inItemRef.setAmount(1);
        if(!ItemManager.CustomItemsName.containsKey(inItemRef)) return;
        Integer maxStack = ItemManager.CustomItemsStackSize.get(inItemRef);

        boolean hadOne = p.getInventory().contains(inItem);
        boolean canFit = false;

        for(ItemStack itemStack : p.getInventory().getStorageContents()){
            if(itemStack == null || itemStack.getType().equals(Material.AIR)) continue;
            if(itemStack.getAmount() < maxStack){canFit = true;}
        }

        for (ItemStack itemStack : p.getInventory().getStorageContents()){
            if(itemStack == null && hadOne) continue;

            if(!hadOne || itemStack.isSimilar(e.getItem().getItemStack())){
                e.setCancelled(true);

                boolean gotten = false;

                while(leftAmt > 0){
                    int emptySlot = p.getInventory().firstEmpty();
                    if(emptySlot == -1) break;
                    ItemStack newItem = inItem.clone();

                    if(hadOne && (itemStack.getAmount() < maxStack)){
                        int needs = maxStack- itemStack.getAmount();

                        if(inIAmt - needs >= 0) itemStack.setAmount(maxStack);
                        else itemStack.setAmount(itemStack.getAmount()+inIAmt);
                        leftAmt -= needs;
                        gotten = true;
                    }

                    if(hadOne && canFit && !gotten){
                        for(ItemStack itemStack1 : p.getInventory()){
                            if(itemStack1 == null || itemStack1.getType().equals(Material.AIR)) continue;
                            if(itemStack1.getAmount() < maxStack){
                                int needs = maxStack- itemStack1.getAmount();

                                if(inIAmt - needs >= 0) itemStack1.setAmount(maxStack);
                                else itemStack1.setAmount(itemStack1.getAmount()+inIAmt);
                                leftAmt -= needs;
                                break;
                            }
                        }
                    }

                    if(leftAmt-maxStack <= 0){
                        newItem.setAmount(leftAmt);
                    }else{
                        newItem.setAmount(leftAmt-(leftAmt-maxStack));
                    }

                    p.getInventory().setItem(emptySlot, newItem);

                    leftAmt -= maxStack;
                }

                if(leftAmt <= 0) {
                    e.getItem().remove();
                    return;
                }
                break;
            }
        }

        e.getItem().getItemStack().setAmount(leftAmt);
        e.getItem().setItemStack(e.getItem().getItemStack());
    }
}
