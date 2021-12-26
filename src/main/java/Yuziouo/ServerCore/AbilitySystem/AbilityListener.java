package Yuziouo.ServerCore.AbilitySystem;

import Yuziouo.ServerCore.ServerCore;
import Yuziouo.ServerCore.ShowDamageSystem.ShowText;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityRegainHealthEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.utils.TextFormat;

import java.util.HashMap;

public class AbilityListener implements Listener {
    public static HashMap<String, Integer> speed = new HashMap<>();
    public static HashMap<String, Integer> mine = new HashMap<>();

    @EventHandler
    public void addPoint(PlayerAddPointEvent event) {
        Player player = event.getPlayer();
        player.sendPopup("經過不斷的努力你已成功獲取一點天賦點數");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            if (!(((EntityDamageByEntityEvent) event).getDamager() instanceof Player)) return;
            if (event.getEntity() instanceof Player) {
                Ability atk = ServerCore.getPlugin().getAbilityHashMap().get(((Player) ((EntityDamageByEntityEvent) event).getDamager()).getUniqueId().toString());
                Ability target = ServerCore.getPlugin().getAbilityHashMap().get(((Player) event.getEntity()).getUniqueId().toString());
                float dmg;
                if (atk.getStr() > 0) {
                    if (atk.getStr() - target.getDef() != 0)
                        dmg = (event.getDamage() * atk.getStr() / (atk.getStr() - target.getDef()));
                    else
                        dmg = event.getDamage();
                } else if (target.getDef() > 1)
                    dmg = (event.getDamage() / (target.getDef()));
                else
                    dmg = event.getDamage();
                if (dmg < 0)
                    event.setDamage(0);
                else
                    event.setDamage(dmg);
            } else {
                Ability atk = ServerCore.getPlugin().getAbilityHashMap().get(((Player) ((EntityDamageByEntityEvent) event).getDamager()).getUniqueId().toString());
                float dmg;
                dmg = event.getDamage() + atk.getStr() - (float) event.getEntity().getMaxHealth() / 10;
                if (dmg < 0)
                    event.setDamage(0);
                else event.setDamage(dmg);
            }
        }
        ShowText.send(event.getEntity(), TextFormat.RED+"-"+Float.toString(event.getDamage()));
    }
    @EventHandler
    public void onHealth(EntityRegainHealthEvent event){
        ShowText.send(event.getEntity(),TextFormat.GREEN+"+"+Float.toString(event.getAmount()));
    }
    @EventHandler
    public void onQuite(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (speed.containsKey(player.getUniqueId().toString())) {
            ServerCore.getPlugin().getServer().getScheduler().cancelTask(speed.get(player.getUniqueId().toString()));
            speed.remove(player.getUniqueId().toString());
        }
        if (mine.containsKey(player.getUniqueId().toString())) {
            ServerCore.getPlugin().getServer().getScheduler().cancelTask(mine.get(player.getUniqueId().toString()));
            mine.remove(player.getUniqueId().toString());
        }
    }
}
