package Yuziouo.ServerCore.GradeSystem;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;


public class PlayerAddGradeEvent extends Event implements Cancellable {
    private final Player player;
    private int grade;
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    public PlayerAddGradeEvent(Player player, int grade) {
        this.grade = grade;
        this.player = player;
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

    public int getGrade() {
        return grade;
    }

    public Player getPlayer() {
        return player;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

}
