package Yuziouo.ServerCore.BodyStr;

import Yuziouo.ServerCore.ServerCore;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerLocallyInitializedEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.utils.TextFormat;

import java.util.HashMap;

public class StrengthListener implements Listener {
    private final HashMap<String,Integer> task = new HashMap<>();
    public void show(Player player){
        int total = ServerCore.getPlugin().getStrengthHashMap().get(player.getUniqueId().toString()).getMax();
        int cent = total/10;
        int mystr = ServerCore.getPlugin().getStrengthHashMap().get(player.getUniqueId().toString()).getStrength();
        int need = mystr / cent;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < need;i++){
            builder.append(TextFormat.YELLOW).append("⚡");
        }
        for (int j = need; j <=cent;j++){
            builder.append(TextFormat.RED).append("⚡");
        }
        player.sendPopup("體力值:"+builder.toString());
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        if (event.isCancelled()) return;
        if (!ServerCore.getPlugin().getStrengthHashMap().get(player.getUniqueId().toString()).hasStrength()) {
            player.sendMessage("很抱歉優 體力暫時用盡了 休息一下 再繼續挖掘把");
            event.setCancelled(true);
            return;
        }
        ServerCore.getPlugin().getStrengthHashMap().get(player.getUniqueId().toString()).delStrength(1);
        show(player);
    }
    @EventHandler
    public void onJoin(PlayerLocallyInitializedEvent event){
        task.put(event.getPlayer().getUniqueId().toString(),ServerCore.getPlugin().getServer().getScheduler().scheduleRepeatingTask(ServerCore.getPlugin(),new AddStrengthTask(event.getPlayer()),20*3).getTaskId());
    }
    @EventHandler
    public void onQuite(PlayerQuitEvent event){
        if (task.containsKey(event.getPlayer().getUniqueId().toString())){
            ServerCore.getPlugin().getServer().getScheduler().cancelTask(task.get(event.getPlayer().getUniqueId().toString()));
            task.remove(event.getPlayer().getUniqueId().toString());
        }
    }
}
