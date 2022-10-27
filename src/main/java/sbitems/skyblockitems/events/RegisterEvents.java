package sbitems.skyblockitems.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sbitems.skyblockitems.items.ItemManager;
import sbitems.skyblockitems.items.LunchBox;

import java.util.Objects;

public class RegisterEvents implements Listener {

    @EventHandler
    public static void PlayerPickupEvent(EntityPickupItemEvent e){
        // Makes sure the entity picking smth up is a player and creates a variable for player.
        if(!(e.getEntity() instanceof Player))return;
        Player p = (Player) e.getEntity();

        // Checks if game mode is creative bc everything breaks when it is
        if(p.getGameMode() == GameMode.CREATIVE) return;

        ItemStack inItem = e.getItem().getItemStack();

        // Sets the items and the amount. inItemRef is just a way to check if it exists in the maps.
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
            // If there is an available slot for the item to add to, it lets the programme know
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
                        int needs = maxStack- itemStack.getAmount(); // How much it needs to make it max stack

                        if(inIAmt - needs >= 0) itemStack.setAmount(maxStack); // Checks if there is the "Perfect" amount going in, if so, sets it to max stack.
                        else itemStack.setAmount(itemStack.getAmount()+inIAmt); // If it is not perfect, add them
                        leftAmt -= needs; // Remove what it took.
                        gotten = true;
                    }

                    if(hadOne && canFit && !gotten){ // Does the same thing as above but for a different item stack
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

                    // Pretty self-explanatory, sets the amount of the in item to see what they should be.
                    if(leftAmt-maxStack <= 0){
                        newItem.setAmount(leftAmt);
                    }else{
                        newItem.setAmount(leftAmt-(leftAmt-maxStack));
                    }

                    p.getInventory().setItem(emptySlot, newItem);

                    leftAmt -= maxStack;
                }

                if(leftAmt <= 0) {
                    // If there is no more of the item, remove it from the world.
                    e.getItem().remove();
                    return;
                }
                break;
            }
        }

        // Sets the item stacks.
        e.getItem().getItemStack().setAmount(leftAmt);
        e.getItem().setItemStack(e.getItem().getItemStack());
    }


    public static int NextOpen(Inventory inv, ItemStack item, int maxStack, int omitSlot){
        // Finds the next open slot
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
        // Cancels the event because I'm too lazy to write the code for it at the moment.
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

        // Stuff breaks in the following two scenarios
        if(inv.getType() == InventoryType.BREWING) return;

        if(p.getGameMode() == GameMode.CREATIVE) return;

        ItemStack inItem = e.getWhoClicked().getItemOnCursor();

        int inIAmt = inItem.getAmount();

        ItemStack inItemRef = inItem.clone();
        inItemRef.setAmount(1);
        Integer maxStack = ItemManager.CustomItemsStackSize.get(inItemRef);

        ClickType type = e.getClick();

        // Checks if the item has a max stack size defined by plugin - this is for shift clicking because it kept making errors
        if(maxStack == null) {
            ItemStack clickStack = p.getInventory().getItem(e.getSlot());
            if(clickStack == null) return;
            ItemStack clickStackR = clickStack.clone();
            clickStackR.setAmount(1);

            Integer clickStackE = ItemManager.CustomItemsStackSize.get(clickStackR);

            if(type == ClickType.CONTROL_DROP) return;
            // Stops double click because I'm too lazy to do that as well
            if(type == ClickType.DOUBLE_CLICK && clickStackE != null) {e.setCancelled(true);return;}
            if(type.isShiftClick() && clickStackE != null) {
                e.setCancelled(true);

                int amtLeft = clickStack.getAmount();

                // Gets the next available slot for it to go in -- not including itself
                int nextOpen = NextOpen(p.getInventory(), clickStackR, clickStackE, e.getSlot());
                if (nextOpen == -1) return;

                ItemStack targItem = p.getInventory().getItem(nextOpen);

                if(targItem == null) return;

                int tIA = targItem.getAmount();

                // Adds itself to the next available slot (targItem) and set remainder if there is one --- too lazy to finish as well
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

        // Adds itself to the clicked area, sets what's in the cursor to what's left over.
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

        // If the consumed item is a custom item then...
        if(ItemManager.FoodSaturation.containsKey(item)) {
            e.setCancelled(true);
            Player p = e.getPlayer();
            float playerSaturation = p.getSaturation();
            int playerHunger = p.getFoodLevel();
            // Makes sure the player doesn't eat too much.
            if(playerHunger >= 20) return;

            // Add food to player and saturation
            p.setFoodLevel(Math.min(playerHunger + ItemManager.FoodBars.get(item), 20));
            p.setSaturation(playerSaturation + ItemManager.FoodSaturation.get(item));

            // If the consumed item grants an effect, grant it
            if(ItemManager.FoodEffect.containsKey(item))
                p.addPotionEffects(ItemManager.FoodEffect.get(item));

            // Subtraction
            itemIn.setAmount(itemIn.getAmount()-1);
        }
    }

    @EventHandler
    public static void PlayerClick (PlayerInteractEvent e){
        if(e.getAction() == Action.LEFT_CLICK_AIR && e.getItem() != null)
            if(e.getItem().isSimilar(ItemManager.CustomItems.get("Bacon")))
                e.getPlayer().getInventory().addItem(ItemManager.CustomItems.get("Lunch Box"));

        if((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.getItem() != null)
            if(e.getItem().isSimilar(ItemManager.CustomItems.get("Lunch Box"))) {
                e.setCancelled(true);
                LunchBox.openLunchBoxGUI(e.getPlayer());
            }
    }
}
