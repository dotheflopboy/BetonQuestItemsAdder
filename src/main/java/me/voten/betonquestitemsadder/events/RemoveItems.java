package me.voten.betonquestitemsadder.events;

import dev.lone.itemsadder.api.ItemsAdder;

import main.java.me.voten.betonquestitemsadder.util.NumberUtils;
import org.betonquest.betonquest.api.profiles.Profile;
import org.bukkit.inventory.ItemStack;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.QuestEvent;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.utils.PlayerConverter;

public class RemoveItems extends QuestEvent {
    private final ItemStack item;
    private final int amount;

    public RemoveItems(Instruction instruction) throws InstructionParseException {
        super(instruction, true);
        String item = instruction.getInstruction().substring(instruction.getInstruction().indexOf(" ") + 1);
        if (item.contains(" ")) {
            this.item = ItemsAdder.getCustomItem(item.substring(0, item.indexOf(" ")));
            if (NumberUtils.isInteger(item.substring(item.indexOf(" ") + 1))) {
                this.amount = Integer.parseInt(item.substring(item.indexOf(" ") + 1));
            } else {
                this.amount = 1;
                throw new InstructionParseException("Amount must be a number");
            }
        } else {
            this.item = ItemsAdder.getCustomItem(instruction.getInstruction().substring(instruction.getInstruction().indexOf(" ") + 1));
            this.amount = 1;
        }
    }

    @Override
    protected Void execute(Profile profile) {
        if (this.item == null) {
            System.out.println("§c[BetonQuest -> ItemsAdder] Wrong item name");
            return null;
        }
        this.item.setAmount(this.amount);
        PlayerConverter.getOnlineProfiles(profile).getInventory().removeItem(this.item);
        return null;
    }
}
