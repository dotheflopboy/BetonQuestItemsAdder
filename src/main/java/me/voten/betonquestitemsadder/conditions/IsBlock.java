package main.java.me.voten.betonquestitemsadder.conditions;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import main.java.me.voten.betonquestitemsadder.util.NumberUtils;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Condition;
import org.betonquest.betonquest.api.profiles.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class IsBlock extends Condition {

    private final String item;

    public IsBlock(Instruction instruction) {
        super(instruction, true);
        this.item = instruction.getInstruction().substring(instruction.getInstruction().indexOf(" ") + 1);
    }

    @Override
    protected Boolean execute(Profile profile) {
        String name;
        String Location;
        World world;
        int x = 0, y = 0, z = 0;
        if (item.contains(" ")) {
            name = item.substring(0, item.indexOf(" "));
            Location = item.substring(item.indexOf(" ") + 1);
            for (int i = 0; i < 3; i++) {
                if (Location.contains(";")) {
                    if (NumberUtils.isInteger(Location.substring(0, Location.indexOf(";")))) {
                        switch (i) {
                            case 0 -> x = Integer.parseInt(Location.substring(0, Location.indexOf(";")));
                            case 1 -> y = Integer.parseInt(Location.substring(0, Location.indexOf(";")));
                            case 2 -> z = Integer.parseInt(Location.substring(0, Location.indexOf(";")));
                        }
                    }
                    Location = Location.substring(Location.indexOf(";") + 1);
                }
            }
            world = Bukkit.getWorld(Location);
            if (world == null) {
                System.out.println("WORLD cannot be null");
                return null;
            }
            Location loc = new Location(world, x, y, z);

            if (CustomBlock.byAlreadyPlaced(loc.getBlock()).isBlock()) {
                ItemStack it = CustomBlock.byItemStack(loc.getBlock());
                return CustomStack.byItemStack(it).matchNamespacedID(CustomStack.getInstance(name));
            }
        }
        return false;
    }
}
