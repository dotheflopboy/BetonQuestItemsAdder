package main.java.me.voten.betonquestitemsadder.conditions;

import dev.lone.itemsadder.api.CustomStack;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Condition;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WearItems extends Condition {
    private final String item;

    public WearItems(Instruction instruction) {
        super(instruction, true);
        this.item = instruction.getInstruction().substring(instruction.getInstruction().indexOf(" ") + 1);
    }

    @Override
    protected Boolean execute(Profile profile) {
        Player player = (Player) PlayerConverter.getID(profile.getPlayer());
        ItemStack[] inventoryItems = player.getInventory().getArmorContents();

        for (ItemStack it : inventoryItems) {
            if (CustomStack.byItemStack(it).matchNamespacedID(CustomStack.getInstance(item))) {
                return true;
            }
        }
        return false;
    }
}
