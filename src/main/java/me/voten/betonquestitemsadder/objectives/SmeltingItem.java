package main.java.me.voten.betonquestitemsadder.objectives;

import dev.lone.itemsadder.api.ItemsAdder;
import main.java.me.voten.betonquestitemsadder.util.NumberUtils;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Objective;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class SmeltingItem extends Objective implements Listener {

    private final ItemStack item;
    private final int amount;

    public SmeltingItem(Instruction instruction) throws InstructionParseException {
        super(instruction);
        this.template = SmeltData.class;
        this.item = ItemsAdder.getCustomItem(instruction.next());
        if (this.item == null) {
            throw new InstructionParseException("Wrong item name");
        }
        String amount = instruction.next();
        if (NumberUtils.isInteger(amount)) {
            this.amount = Integer.parseInt(amount);
            if (this.amount <= 0) {
                throw new InstructionParseException("Amount cannot be less than 1");
            }
        } else {
            throw new InstructionParseException("Wrong amount number");
        }
    }

    @EventHandler
    public void onItemGet(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        if (e.getInventory().getType().equals(InventoryType.FURNACE) || e.getInventory().getType().equals(InventoryType.BLAST_FURNACE)) {
            if (e.getRawSlot() == 2) {
                Profile profile = PlayerConverter.getID(player);
                if (containsPlayer(profile)) {
                    if (ItemsAdder.matchCustomItemName(e.getCurrentItem(), ItemsAdder.getCustomItemName(this.item))) {
                        if (checkConditions(profile)) {
                            SmeltData playerData = (SmeltData) this.dataMap.get(profile);
                            playerData.subtract(e.getCurrentItem().getAmount());
                            if (playerData.isZero())
                                completeObjective(profile);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onShiftSmelting(InventoryClickEvent event) {
        if (event.getInventory().getType().equals(InventoryType.FURNACE) && event.getRawSlot() == 2 && event.getClick().equals(ClickType.SHIFT_LEFT) && (event.getWhoClicked() instanceof Player player)) {
            Profile profile = PlayerConverter.getID(player);
            if (containsPlayer(profile))
                event.setCancelled(true);
        }
    }

    @Override
    public void start() {
        Bukkit.getPluginManager().registerEvents(this, BetonQuest.getInstance());
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public String getDefaultDataInstruction() {
        return Integer.toString(this.amount);
    }

    @Override
    public String getProperty(String name, Profile profile) {
        if ("left".equalsIgnoreCase(name))
            return Integer.toString(this.amount - ((SmeltData) this.dataMap.get(profile)).getAmount());
        if ("amount".equalsIgnoreCase(name))
            return Integer.toString(((SmeltData) this.dataMap.get(profile)).getAmount());
        return "";
    }

    public static class SmeltData extends Objective.ObjectiveData {
        private int amount;

        public SmeltData(String instruction, Profile profile, String objID) {
            super(instruction, profile, objID);
            this.amount = Integer.parseInt(instruction);
        }

        private int getAmount() {
            return this.amount;
        }

        private void subtract(int amount) {
            this.amount -= amount;
            update();
        }

        private boolean isZero() {
            return (this.amount <= 0);
        }

        public String toString() {
            return Integer.toString(this.amount);
        }
    }
}
