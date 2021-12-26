package Yuziouo.ServerCore.GradeSystem;

import Yuziouo.ServerCore.ServerCore;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class GradeCommand extends Command {
    public GradeCommand() {
        super("grade", "設定玩家等級內容");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (commandSender.isPlayer()) {
            if (commandSender.isOp()) {
                Player player = (Player) commandSender;
                GradeForm.GradeMenu(player);
            } else {
                commandSender.sendMessage("你只是一個小小的玩家別想著偷改東西");
            }
        } else {
            if (strings.length == 3) {
                Player target = ServerCore.getPlugin().getServer().getPlayer(strings[1]);
                if (target == null) return true;
                int count = Integer.parseInt(strings[2]);
                Grade grade = ServerCore.getPlugin().getGradeHashMap().get(target.getUniqueId().toString());
                switch (strings[0]) {
                    case "setexp":
                        grade.setExp(count);
                        break;
                    case "setgrade":
                        grade.setGrade(count);
                        break;
                    case "addexp":
                        grade.addExp(count);
                        ServerCore.getPlugin().getServer().getPluginManager().callEvent(new PlayerAddExpEvent(target,count));
                        break;
                    case "addgrade":
                        grade.addGrade(count);
                        ServerCore.getPlugin().getServer().getPluginManager().callEvent(new PlayerAddGradeEvent(target,count));
                        break;
                }
                commandSender.sendMessage("操作成功!");
            } else {
                commandSender.sendMessage("grade 操作方式 玩家名稱 數量");
                commandSender.sendMessage("操作方式: setexp 設定玩家經驗值");
                commandSender.sendMessage("操作方式: setgrade 設定玩家等級");
                commandSender.sendMessage("操作方式: addexp 設定玩家exp");
                commandSender.sendMessage("操作方式: addgrade 設定玩家exp");
            }
        }

        return true;
    }
}
