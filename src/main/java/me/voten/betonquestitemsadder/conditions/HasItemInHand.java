package main.java.me.voten.betonquestitemsadder.conditions;

import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
import main.java.me.voten.betonquestitemsadder.util.NumberUtils;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Condition;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HasItemInHand extends Condition {

    private final String item;
    private final int amount;

    public HasItemInHand(Instruction instruction) throws InstructionParseException {
        super(instruction, true);
        String item = instruction.getInstruction().substring(instruction.getInstruction().indexOf(" ") + 1);
        if (item.contains(" ")) {
            this.item = item.substring(0, item.indexOf(" "));
            if (NumberUtils.isInteger(item.substring(item.indexOf(" ") + 1))) {
                this.amount = Integer.parseInt(item.substring(item.indexOf(" ") + 1));
            } else {
                this.amount = 1;
                throw new InstructionParseException("Amount must be a number");
            }
        } else {
            this.item = instruction.getInstruction().substring(instruction.getInstruction().indexOf(" ") + 1);
            this.amount = 1;
        }
    }

    @Override
    protected Boolean execute(Profile profile) {
        Player player = (Player) PlayerConverter.getID(profile.getPlayer());
        ItemStack HandItem = player.getInventory().getItemInMainHand();


        if (CustomStack.byItemStack(HandItem).matchNamespacedID(CustomStack.getInstance(this.item))) {
            return HandItem.getAmount() >= this.amount;
        }
        return false;
    }
}
