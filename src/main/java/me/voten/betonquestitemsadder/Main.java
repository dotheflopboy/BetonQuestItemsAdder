package main.java.me.voten.betonquestitemsadder;

import main.java.me.voten.betonquestitemsadder.conditions.HasItemInHand;
import main.java.me.voten.betonquestitemsadder.conditions.HasItems;
import main.java.me.voten.betonquestitemsadder.conditions.IsBlock;
import main.java.me.voten.betonquestitemsadder.conditions.WearItems;
import main.java.me.voten.betonquestitemsadder.events.GiveItems;
import main.java.me.voten.betonquestitemsadder.events.PlayAnimation;
import main.java.me.voten.betonquestitemsadder.events.RemoveItems;
import main.java.me.voten.betonquestitemsadder.events.SetBlockAt;
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
        BetonQuest.getInstance().registerEvents("removeitems", RemoveItems.class);
        BetonQuest.getInstance().registerEvents("giveitems", GiveItems.class);
        BetonQuest.getInstance().registerEvents("setblockat", SetBlockAt.class);
        BetonQuest.getInstance().registerEvents("playanimation", PlayAnimation.class);
        //objectives
        BetonQuest.getInstance().registerObjectives("craftitems", main.java.me.voten.betonquestitemsadder.objectives.CraftingItem.class);
        BetonQuest.getInstance().registerObjectives("pickupitems", main.java.me.voten.betonquestitemsadder.objectives.PickupItem.class);
        BetonQuest.getInstance().registerObjectives("blockbreak", main.java.me.voten.betonquestitemsadder.objectives.BlockBreak.class);
        BetonQuest.getInstance().registerObjectives("blockplace", main.java.me.voten.betonquestitemsadder.objectives.BlockPlace.class);
        BetonQuest.getInstance().registerObjectives("enchantitem", main.java.me.voten.betonquestitemsadder.objectives.EnchantItem.class);
        BetonQuest.getInstance().registerObjectives("smeltingitems", main.java.me.voten.betonquestitemsadder.objectives.SmeltingItem.class);
    }

    @Override
    public void onDisable() {

    }
}
