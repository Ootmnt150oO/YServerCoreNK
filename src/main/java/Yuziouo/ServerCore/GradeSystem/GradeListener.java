package Yuziouo.ServerCore.GradeSystem;

import Yuziouo.ServerCore.AbilitySystem.PlayerAddPointEvent;
import Yuziouo.ServerCore.ParticleEffect;
import Yuziouo.ServerCore.ServerCore;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;

public class GradeListener implements Listener {
    @EventHandler
    public void addExp(PlayerAddExpEvent event){
        Player player = event.getPlayer();
        player.sendPopup("經驗+"+event.getExp());
    }
    @EventHandler
    public void addGrade(PlayerAddGradeEvent event){
        Player player = event.getPlayer();
        player.sendTitle("你的經驗已經滿到溢出","幫你自動生上一等啦");
        player.sendMessage("你現在的等級是:"+ ServerCore.getPlugin().getGradeHashMap().get(player.getUniqueId().toString()).getGrade());
        ParticleEffect.addHealth(player.getPosition());
        ParticleEffect.spawnFirework(player.getPosition());
        ServerCore.getPlugin().getServer().getPluginManager().callEvent(new PlayerAddPointEvent(player,1));
        ServerCore.getPlugin().getAbilityHashMap().get(player.getUniqueId().toString()).addPoint(1);
    }
}
