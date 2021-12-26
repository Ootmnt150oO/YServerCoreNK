package Yuziouo.ServerCore.GradeSystem;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;

public class PlayerAddExpEvent extends Event implements Cancellable {
    private final Player player;
    private int exp;
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    public PlayerAddExpEvent(Player player, int exp) {
        this.player = player;
        this.exp = exp;
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

    public int getExp() {
        return exp;
    }

    public Player getPlayer() {
        return player;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
