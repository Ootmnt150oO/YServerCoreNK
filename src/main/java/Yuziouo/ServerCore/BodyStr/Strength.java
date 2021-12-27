package Yuziouo.ServerCore.BodyStr;

public class Strength {
    private int strength;
    private int max;

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void addStrength(int strength) {
        setStrength(getMax() + strength);
    }

    public void delStrength(int strength) {
        setStrength(getStrength() - strength);
    }

    public boolean hasStrength() {
        return getStrength() > 0;
    }
    public boolean hasMaxStr(){
        return getStrength() == getMax();
    }
}
