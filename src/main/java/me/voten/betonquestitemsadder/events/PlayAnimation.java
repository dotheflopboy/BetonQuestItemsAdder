package main.java.me.voten.betonquestitemsadder.events;

import dev.lone.itemsadder.api.ItemsAdder;

import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.QuestEvent;
import org.betonquest.betonquest.api.profiles.Profile;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.entity.Player;

public class PlayAnimation extends QuestEvent {

    private final String name;

    public PlayAnimation(Instruction instruction) throws InstructionParseException {
        super(instruction, true);
        this.name = instruction.next();
    }

    @Override
    protected Void execute(Profile profile) {
        ItemsAdder.playTotemAnimation((Player) PlayerConverter.getID(profile.getPlayer()), this.name);
        return null;
    }

}
