package main.java.me.voten.betonquestitemsadder.objectives;

import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent;
import dev.lone.itemsadder.api.ItemsAdder;
import main.java.me.voten.betonquestitemsadder.util.NumberUtils;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Objective;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.config.Config;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class BlockPlace extends Objective implements Listener {

    private final ItemStack item;
    private final int amount;
    private final boolean notify;
    private final int notifyInterval;

    public BlockPlace(Instruction instruction) throws InstructionParseException {
        super(instruction);
        this.template = BlockData.class;
        this.notifyInterval = instruction.getInt(instruction.getOptional("notify"), 1);
        this.notify = (instruction.hasArgument("notify") || this.notifyInterval > 1);
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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlaceBlock(CustomBlockPlaceEvent e) {
        Profile profile = PlayerConverter.getID(e.getPlayer());
        if (containsPlayer(profile)) {
            if (ItemsAdder.matchCustomItemName(e.getCustomBlockItem(), ItemsAdder.getCustomItemName(this.item))) {
                if (checkConditions(profile)) {
                    BlockData playerData = (BlockData) this.dataMap.get(profile);
                    playerData.add();
                    if (playerData.getAmount() == this.amount) {
                        completeObjective(profile);
                    } else if (this.notify && playerData.getAmount() % this.notifyInterval == 0) {
                        if (playerData.getAmount() > this.amount) {
                            try {
                                Config.sendNotify(this.instruction.getPackage().getName(), profile, "blocks_to_break", new String[]{String.valueOf(playerData.getAmount() - this.amount)}, "blocks_to_break,info");
                            } catch (QuestRuntimeException exception) {
                                try {
                                    LogUtils.getLogger().log(Level.WARNING, "The notify system was unable to play a sound for the 'blocks_to_break' category in '" + this.instruction.getObjective().getFullID() + "'. Error was: '" + exception.getMessage() + "'");
                                } catch (InstructionParseException exep) {
                                    LogUtils.logThrowableReport(exep);
                                }
                            }
                        }
                    } else {
                        try {
                            Config.sendNotify(this.instruction.getPackage().getName(), profile, "blocks_to_place", new String[]{String.valueOf(this.amount - playerData.getAmount())}, "blocks_to_place,info");
                        } catch (QuestRuntimeException exception) {
                            try {
                                LogUtils.getLogger().log(Level.WARNING, "The notify system was unable to play a sound for the 'blocks_to_place' category in '" + this.instruction.getObjective().getFullID() + "'. Error was: '" + exception.getMessage() + "'");
                            } catch (InstructionParseException exep) {
                                LogUtils.logThrowableReport(exep);
                            }
                        }
                    }
                }
            }
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
        return "0";
    }

    @Override
    public String getProperty(String name, Profile profile) {
        if ("left".equalsIgnoreCase(name))
            return Integer.toString(this.amount - ((BlockData) this.dataMap.get(profile)).getAmount());
        if ("amount".equalsIgnoreCase(name))
            return Integer.toString(((BlockData) this.dataMap.get(profile)).getAmount());
        return "";
    }

    public static class BlockData extends Objective.ObjectiveData {
        private int amount;

        public BlockData(String instruction, Profile profile, String objID) {
            super(instruction, profile, objID);
            this.amount = Integer.parseInt(instruction);
        }

        private void add() {
            this.amount++;
            update();
        }

        private int getAmount() {
            return this.amount;
        }

        public String toString() {
            return String.valueOf(this.amount);
        }
    }
}
