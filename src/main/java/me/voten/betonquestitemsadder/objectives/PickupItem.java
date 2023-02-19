package main.java.me.voten.betonquestitemsadder.objectives;

import dev.lone.itemsadder.api.ItemsAdder;
import main.java.me.voten.betonquestitemsadder.util.NumberUtils;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Objective;
import org.betonquest.betonquest.api.profiles.OnlineProfile;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.config.Config;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.logging.Level;

public class PickupItem extends Objective implements Listener {

    private final ItemStack item;
    private final int amount;
    private final boolean notify;

    public PickupItem(Instruction instruction) throws InstructionParseException {
        super(instruction);
        this.template = PickupData.class;
        this.notify = instruction.hasArgument("notify");
        String item = instruction.getInstruction().substring(instruction.getInstruction().indexOf(" ") + 1);
        if (item.contains(" ")) {
            String am = item.substring(item.indexOf(" ") + 1);
            this.item = ItemsAdder.getCustomItem(item.substring(0, item.indexOf(" ")));
            if (am.contains(" ")) {
                am = am.substring(0, am.indexOf(" "));
            }
            if (NumberUtils.isInteger(am)) {
                this.amount = Integer.parseInt(am);
            } else {
                this.amount = 1;
                throw new InstructionParseException("Amount must be a number");
            }
        } else {
            this.item = ItemsAdder.getCustomItem(instruction.getInstruction().substring(instruction.getInstruction().indexOf(" ") + 1));
            this.amount = 1;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        if (!isInvalidItem(e.getItem().getItemStack())) return;
        Profile profile = PlayerConverter.getID(player);
        if (!containsPlayer(profile) || !checkConditions(profile)) {
            return;
        }
        PickupData playerData = getPickupData(profile);
        ItemStack pickupItem = e.getItem().getItemStack();
        playerData.pickup(pickupItem.getAmount());
        if (playerData.isFinished()) {
            completeObjective(profile);
            return;
        }
        if (this.notify)
            try {
                Config.sendNotify(this.instruction.getPackage().getQuestPath(), (OnlineProfile) profile, "items_to_pickup", new String[]{Integer.toString(playerData.getAmount())}, "items_to_pickup,info");
            } catch (QuestRuntimeException exception) {
                try {
                    BetonQuest.getInstance().getLogger().log(Level.WARNING, "The notify system was unable to play a sound for the 'items_to_pickup' category in '" + this.instruction.getObjective().getFullID() + "'. Error was: '" + exception.getMessage() + "'");
                } catch (InstructionParseException ignored) {

                }
            }
    }

    private boolean isInvalidItem(ItemStack it) {
        String name = ItemsAdder.getCustomItemName(this.item);
        if (ItemsAdder.matchCustomItemName(it, name)) {
            return true;
        }
        return false;
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
        return switch (name.toLowerCase(Locale.ROOT)) {
            case "left" -> Integer.toString(getPickupData(profile).getAmount());
            case "amount" -> Integer.toString(this.amount);
            default -> "";
        };
    }

    private PickupData getPickupData(Profile profile) {
        return (PickupData) this.dataMap.get(profile);
    }

    public static class PickupData extends Objective.ObjectiveData {
        private int amount;

        public PickupData(String instruction, Profile profile, String objID) {
            super(instruction, profile, objID);
            this.amount = Integer.parseInt(instruction);
        }

        private void pickup(int pickupAmount) {
            this.amount -= pickupAmount;
            update();
        }

        private boolean isFinished() {
            return (this.amount <= 0);
        }

        private int getAmount() {
            return this.amount;
        }

        public String toString() {
            return Integer.toString(this.amount);
        }
    }
}
