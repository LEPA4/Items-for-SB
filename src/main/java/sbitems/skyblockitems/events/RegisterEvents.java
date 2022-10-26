package sbitems.skyblockitems.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sbitems.skyblockitems.items.ItemManager;

import java.util.Objects;

public class RegisterEvents implements Listener {

    @EventHandler
    public static void PlayerJoin(PlayerJoinEvent e){
        e.getPlayer().getInventory().addItem(ItemManager.CustomItems.get("Bacon"));
    }

    @EventHandler
    public static void PlayerPickupEvent(EntityPickupItemEvent e){
        if(!(e.getEntity() instanceof Player))return;
        Player p = (Player) e.getEntity();

        if(p.getGameMode() == GameMode.CREATIVE) return;

        ItemStack inItem = e.getItem().getItemStack();

        int inIAmt = inItem.getAmount();
        int leftAmt = inIAmt;
        ItemStack inItemRef = inItem.clone();
        inItemRef.setAmount(1);
        if(!ItemManager.CustomItemsStackSize.containsKey(inItemRef)) return;
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


    public static int NextOpen(Inventory inv, ItemStack item, int maxStack, int omitSlot){
        for(int i = 0; i < inv.getStorageContents().length; i++){
            if(inv.getItem(i) == null) continue;
            if(Objects.requireNonNull(inv.getItem(i)).isSimilar(item)){
                if(Objects.requireNonNull(inv.getItem(i)).getAmount() < maxStack){
                    if(i != omitSlot) return i;
                }
            }
        }

        return -1;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public static void PlayerInventoryDrag (InventoryDragEvent e){
        if(e.getWhoClicked().getGameMode() == GameMode.CREATIVE) return;

        ItemStack inItem = e.getOldCursor();
        ItemStack inItemRef = inItem.clone();
        inItemRef.setAmount(1);

        if(ItemManager.CustomItemsName.containsKey(inItemRef)) e.setCancelled(true);
    }

    @EventHandler (priority = EventPriority.HIGH)
    public static void PlayerInventoryClick (InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();

        Inventory inv = e.getClickedInventory();

        if(inv == null) return;

        if(inv.getType() == InventoryType.BREWING) return;

        if(p.getGameMode() == GameMode.CREATIVE) return;

        ItemStack inItem = e.getWhoClicked().getItemOnCursor();

        int inIAmt = inItem.getAmount();

        ItemStack inItemRef = inItem.clone();
        inItemRef.setAmount(1);
        Integer maxStack = ItemManager.CustomItemsStackSize.get(inItemRef);

        ClickType type = e.getClick();

        if(maxStack == null) {
            ItemStack clickStack = p.getInventory().getItem(e.getSlot());
            if(clickStack == null) return;
            ItemStack clickStackR = clickStack.clone();
            clickStackR.setAmount(1);

            Integer clickStackE = ItemManager.CustomItemsStackSize.get(clickStackR);

            if(type == ClickType.CONTROL_DROP) return;
            if(type == ClickType.DOUBLE_CLICK && clickStackE != null) {e.setCancelled(true);return;}
            if(type.isShiftClick() && clickStackE != null) {
                e.setCancelled(true);

                int amtLeft = clickStack.getAmount();

                    int nextOpen = NextOpen(p.getInventory(), clickStackR, clickStackE, e.getSlot());
                    if (nextOpen == -1) return;

                    ItemStack targItem = p.getInventory().getItem(nextOpen);

                    if(targItem == null) return;

                    int tIA = targItem.getAmount();

                    if(tIA + amtLeft <= clickStackE){
                        clickStackR.setAmount(tIA + amtLeft);
                        inv.setItem(nextOpen, clickStackR);
                        inv.clear(e.getSlot());
                    }else if(tIA + amtLeft > clickStackE){
                        amtLeft = tIA + amtLeft;
                        clickStackR.setAmount(clickStackE);
                        amtLeft -= clickStackE;
                        inv.setItem(nextOpen, clickStackR);
                        clickStackR.setAmount(amtLeft);
                        inv.setItem(e.getSlot(), clickStackR);
                    }else return;
                return;
            }
            return;
        }

        e.setCancelled(true);

        ItemStack tarItem = inv.getItem(e.getSlot());

        if(tarItem == null || tarItem.getType() == Material.AIR){
            tarItem = inItem.clone();
            if(inIAmt > maxStack){
                tarItem.setAmount(maxStack);
                inIAmt -= maxStack;
            }else{
                tarItem.setAmount(inIAmt);
                inIAmt = 0;
            }
        }else{
            if(tarItem.getAmount() >= maxStack) return;
            else if(inIAmt + tarItem.getAmount() > maxStack){
                inIAmt = (inIAmt+tarItem.getAmount()) - maxStack;
                tarItem.setAmount(maxStack);
            }else if(inIAmt + tarItem.getAmount() <= maxStack){
                tarItem.setAmount(tarItem.getAmount()+inIAmt);
                inIAmt = 0;
            }
        }

        inv.setItem(e.getSlot(), tarItem);
        inItem.setAmount(inIAmt);
        e.getWhoClicked().setItemOnCursor(inItem);
    }

    @EventHandler
    public static void PlayerConsume (PlayerItemConsumeEvent e){
        ItemStack itemIn = e.getPlayer().getInventory().getItemInMainHand();

        ItemStack item = itemIn.clone();
        item.setAmount(1);

        if(ItemManager.FoodSaturation.containsKey(item)) {
            e.setCancelled(true);
            Player p = e.getPlayer();
            float playerSaturation = p.getSaturation();
            int playerHunger = p.getFoodLevel();
            if(playerHunger >= 20) return;

            p.setFoodLevel(Math.min(playerHunger + ItemManager.FoodBars.get(item), 20));
            p.setSaturation(playerSaturation + ItemManager.FoodSaturation.get(item));

            if(ItemManager.FoodEffect.containsKey(item))
                p.addPotionEffects(ItemManager.FoodEffect.get(item));



            itemIn.setAmount(itemIn.getAmount()-1);
        }
    }
}
