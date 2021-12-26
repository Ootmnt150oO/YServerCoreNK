package Yuziouo.ServerCore.AbilitySystem;

public class Ability {

    private int point, str, def, high, speed, mine, health;

    public int getPoint() {
        return point;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDef() {
        return def;
    }

    public int getHigh() {
        return high;
    }

    public int getMine() {
        return mine;
    }

    public int getStr() {
        return str;
    }

    public int getHealth() {
        return health;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public void setMine(int mine) {
        this.mine = mine;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void addPoint(int point) {
        setPoint(getPoint() + point);
    }
    public void delPoint(int point){
        setPoint(getPoint()-point);
    }
}
