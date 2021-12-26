package Yuziouo.ServerCore.AbilitySystem;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class AbilityCmd extends Command {
    public AbilityCmd() {
        super("ability","開啟天賦點選系統");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender.isPlayer())
            AbilityForm.PlayerMenu((Player) commandSender);
        return true;
    }
}
