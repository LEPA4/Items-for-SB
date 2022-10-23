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

public class RegisterEvents implements Listener {

    private static int getIfEmpty(Inventory inv){
        for(int i = 0; i < inv.getStorageContents().length; i++){
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

        ItemStack inItem = e.getItem().getItemStack();
        int inIAmt = inItem.getAmount();
        int leftAmt = inIAmt;
        ItemStack inItemRef = inItem.clone();
        inItemRef.setAmount(1);
        Integer maxStack = ItemManager.CustomItemsStackSize.get(inItemRef);

        boolean hadOne = false;
        boolean canFit = false;

        for(ItemStack itemStack : p.getInventory()){
            if(itemStack == null || itemStack.getType().equals(Material.AIR)) continue;
            if(itemStack.isSimilar(e.getItem().getItemStack())){hadOne = true;}
            if(itemStack.getAmount() < maxStack){canFit = true;}
        }

        for (ItemStack itemStack : p.getInventory()){
            if((itemStack == null || itemStack.getType().equals(Material.AIR)) && hadOne) continue;

            if(!hadOne || itemStack.isSimilar(e.getItem().getItemStack())){
                int emptySlot = getIfEmpty(p.getInventory());
                if(emptySlot == -1) {
                    e.setCancelled(true);
                    return;
                }

                e.setCancelled(true);

                boolean gotten = false;

                for(int i = 0; i <= inIAmt+1; i++){
                    if(emptySlot == -1){
                        break;
                    }
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

                    if(leftAmt <= 0) break;
                    if(leftAmt-maxStack <= 0){
                        newItem.setAmount(leftAmt);
                    }else{
                        newItem.setAmount(leftAmt-(leftAmt-maxStack));
                    }

                    p.getInventory().setItem(emptySlot, newItem);
                    leftAmt -= maxStack;
                    if(leftAmt <= 0) break;

                    emptySlot = getIfEmpty(p.getInventory());
                }
                if(leftAmt <= 0) {
                    e.getItem().remove();
                    return;
                }
                if(emptySlot == -1){
                    break;
                }
            }
        }

        e.getItem().getItemStack().setAmount(leftAmt);
        e.getItem().setItemStack(e.getItem().getItemStack());
    }
}
