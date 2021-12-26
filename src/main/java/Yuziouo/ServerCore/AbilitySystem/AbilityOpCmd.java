package Yuziouo.ServerCore.AbilitySystem;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class AbilityOpCmd extends Command {
    public AbilityOpCmd() {
        super("abilityop","管理員玩家天賦設定選項");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender.isPlayer()){
            if (commandSender.isOp()){
                AbilityForm.OpMenu((Player) commandSender);
            }
        }
        return true;
    }
}
