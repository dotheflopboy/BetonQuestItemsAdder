package main.java.me.voten.betonquestitemsadder;

import main.java.me.voten.betonquestitemsadder.events.GiveItems;
import main.java.me.voten.betonquestitemsadder.events.PlayAnimation;
import me.voten.betonquestitemsadder.conditions.HasItemInHand;
import me.voten.betonquestitemsadder.conditions.HasItems;
import me.voten.betonquestitemsadder.conditions.IsBlock;
import me.voten.betonquestitemsadder.conditions.WearItems;
import me.voten.betonquestitemsadder.events.PlayAnimation;
import me.voten.betonquestitemsadder.events.RemoveItems;
import me.voten.betonquestitemsadder.events.SetBlockAt;
import org.betonquest.betonquest.BetonQuest;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main INSTANCE;

    @Override
    public void onLoad() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        //conditions
        BetonQuest.getInstance().registerConditions("hasitems", HasItems.class);
        BetonQuest.getInstance().registerConditions("wearitems", WearItems.class);
        BetonQuest.getInstance().registerConditions("hasiteminhand", HasItemInHand.class);
        BetonQuest.getInstance().registerConditions("isblock", IsBlock.class);
        //events
        BetonQuest.getInstance().registerEvent("removeitems", RemoveItems.class);
        BetonQuest.getInstance().registerEvents("giveitems", GiveItems.class);
        BetonQuest.getInstance().registerEvents("setblockat", SetBlockAt.class);
        BetonQuest.getInstance().registerEvents("playanimation", PlayAnimation.class);
        //objectives
        BetonQuest.getInstance().registerObjectives("craftitems", me.voten.betonquestitemsadder.objectives.CraftingItem.class);
        BetonQuest.getInstance().registerObjectives("pickupitems", me.voten.betonquestitemsadder.objectives.PickupItem.class);
        BetonQuest.getInstance().registerObjectives("blockbreak", me.voten.betonquestitemsadder.objectives.BlockBreak.class);
        BetonQuest.getInstance().registerObjectives("blockplace", me.voten.betonquestitemsadder.objectives.BlockPlace.class);
        BetonQuest.getInstance().registerObjectives("enchantitem", me.voten.betonquestitemsadder.objectives.EnchantItem.class);
        BetonQuest.getInstance().registerObjectives("smeltingitems", me.voten.betonquestitemsadder.objectives.SmeltingItem.class);
    }

    @Override
    public void onDisable() {

    }
}
