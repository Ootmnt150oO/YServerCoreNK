package Yuziouo.ServerCore;

import Yuziouo.ServerCore.AbilitySystem.*;
import Yuziouo.ServerCore.BodyStr.Strength;
import Yuziouo.ServerCore.BodyStr.StrengthListener;
import Yuziouo.ServerCore.GradeSystem.Grade;
import Yuziouo.ServerCore.GradeSystem.GradeCommand;
import Yuziouo.ServerCore.GradeSystem.GradeForm;
import Yuziouo.ServerCore.GradeSystem.GradeListener;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLocallyInitializedEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.AsyncTask;

import java.sql.SQLException;
import java.util.HashMap;

public class ServerCore extends PluginBase implements Listener {
    private final HashMap<String, Grade> gradeHashMap = new HashMap<>();
    private final HashMap<String, Ability> abilityHashMap = new HashMap<>();
    private final HashMap<String, Strength> strengthHashMap = new HashMap<>();
    private static ServerCore plugin;
    MySQL sql;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        plugin = this;
        sql = new MySQL();
        try {
            sql.connect();
            sql.createTable();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new GradeForm(),this);
        getServer().getPluginManager().registerEvents(new GradeListener(),this);
        getServer().getPluginManager().registerEvents(new AbilityForm(),this);
        getServer().getPluginManager().registerEvents(new AbilityListener(),this);
        getServer().getPluginManager().registerEvents(new StrengthListener(),this);
        getServer().getCommandMap().register("grade",new GradeCommand());
        getServer().getCommandMap().register("ability",new AbilityCmd());
        getServer().getCommandMap().register("abilityop",new AbilityOpCmd());
        super.onEnable();
    }

    public static ServerCore getPlugin() {
        return plugin;
    }

    public HashMap<String, Grade> getGradeHashMap() {
        return gradeHashMap;
    }

    public HashMap<String, Ability> getAbilityHashMap() {
        return abilityHashMap;
    }

    public HashMap<String, Strength> getStrengthHashMap() {
        return strengthHashMap;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void createDB(PlayerLocallyInitializedEvent event){
        Player player = event.getPlayer();
        sql.createAccount(player.getUniqueId());
    }
    @EventHandler(priority = EventPriority.LOW)
    public void loadPlayer(PlayerLocallyInitializedEvent event){
        Player player = event.getPlayer();
        getServer().getScheduler().scheduleAsyncTask(this, new AsyncTask() {
            @Override
            public void onRun() {
                sql.loadPlayer(player.getUniqueId(),new Grade(),new Ability(),new Strength());
            }
        });
        getServer().getScheduler().scheduleDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                reloadPlayer(player);
            }
        },20);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void savePlayer(PlayerQuitEvent event){
        Player player = event.getPlayer();
        sql.savePlayer(player.getUniqueId(),getGradeHashMap().get(player.getUniqueId().toString()), getAbilityHashMap().get(player.getUniqueId().toString()));
    }
    public static void reloadPlayer(Player player) {
        Ability ability = ServerCore.getPlugin().getAbilityHashMap().get(player.getUniqueId().toString());
        player.setMaxHealth(20 + ability.getHealth());
        player.getEffects().clear();
        ServerCore.getPlugin().getStrengthHashMap().get(player.getUniqueId().toString()).setMax(100);
        ServerCore.getPlugin().getStrengthHashMap().get(player.getUniqueId().toString()).setStrength(ServerCore.getPlugin().getStrengthHashMap().get(player.getUniqueId().toString()).getMax());
        float sc = 1.0f + ((float) ability.getHigh() / 10f);
        player.setScale(sc);
        if (AbilityListener.speed.containsKey(player.getUniqueId().toString())) {
            ServerCore.getPlugin().getServer().getScheduler().cancelTask(AbilityListener.speed.get(player.getUniqueId().toString()));
            AbilityListener.speed.remove(player.getUniqueId().toString());
        }
        if (AbilityListener.mine.containsKey(player.getUniqueId().toString())) {
            ServerCore.getPlugin().getServer().getScheduler().cancelTask(AbilityListener.mine.get(player.getUniqueId().toString()));
            AbilityListener.mine.remove(player.getUniqueId().toString());
        }
        if (ability.getSpeed() > 0) {
            AbilityListener.speed.put(player.getUniqueId().toString(), ServerCore.getPlugin().getServer().getScheduler().scheduleRepeatingTask(ServerCore.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    player.addEffect(Effect.getEffect(Effect.SPEED).setAmplifier(ability.getSpeed()).setDuration(20 * 10));
                }
            }, 20 * 5).getTaskId());
        }
        if (ability.getMine() > 0) {
            AbilityListener.mine.put(player.getUniqueId().toString(), ServerCore.getPlugin().getServer().getScheduler().scheduleRepeatingTask(ServerCore.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    player.addEffect(Effect.getEffect(Effect.HASTE).setAmplifier(ability.getMine()).setDuration(20 * 10));
                }
            }, 20 * 5).getTaskId());
        }
    }
}
