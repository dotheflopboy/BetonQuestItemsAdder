package main.java.me.voten.betonquestitemsadder.conditions;

import dev.lone.itemsadder.api.CustomStack;
import main.java.me.voten.betonquestitemsadder.util.NumberUtils;
import org.betonquest.betonquest.api.profiles.Profile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Condition;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.utils.PlayerConverter;

public class HasItems extends Condition {

    private final String item;
    private final int amount;

    public HasItems(Instruction instruction) throws InstructionParseException {
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
        ItemStack[] inventoryItems = player.getInventory().getContents();
        int am = 0;



        for (ItemStack it : inventoryItems) {
            if (CustomStack.byItemStack(it).matchNamespacedID(CustomStack.getInstance(this.item))) {
                if (it.getAmount() >= this.amount) {
                    return true;
                } else {
                    am += it.getAmount();
                }
            }
            if (am >= this.amount) {
                return true;
            }
        }
        return false;
    }
}
