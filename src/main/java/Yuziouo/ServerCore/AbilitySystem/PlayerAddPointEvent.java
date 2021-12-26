package Yuziouo.ServerCore.AbilitySystem;

import cn.nukkit.Player;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.Listener;

public class PlayerAddPointEvent extends Event implements Listener {
    private final Player player;
    private int point;
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    public PlayerAddPointEvent(Player player, int point) {
        this.player = player;
        this.point = point;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
