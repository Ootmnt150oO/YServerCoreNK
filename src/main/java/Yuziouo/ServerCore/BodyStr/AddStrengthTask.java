package Yuziouo.ServerCore.BodyStr;

import Yuziouo.ServerCore.ServerCore;
import cn.nukkit.Player;
import cn.nukkit.scheduler.Task;

public class AddStrengthTask extends Task {
    Player player;
    public AddStrengthTask(Player player){
        this.player = player;
    }
    @Override
    public void onRun(int i) {
        if (!ServerCore.getPlugin().getStrengthHashMap().get(player.getUniqueId().toString()).hasMaxStr())
        ServerCore.getPlugin().getStrengthHashMap().get(player.getUniqueId().toString()).addStrength(1);
    }
}
