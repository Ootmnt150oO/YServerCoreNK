package Yuziouo.ServerCore.GradeSystem;


public class Grade {
    private int grade;
    private int exp;

    public int getGrade() {
        return grade;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getMaxExp() {
        return (getGrade() * getGrade() * 20) + 50;
    }

    public void upGrade() {
        setExp(getExp() - getMaxExp());
        setGrade(getGrade() + 1);
    }

    public boolean canUP() {
        return getExp() >= getMaxExp();
    }

    public void addExp(int exp) {
        setExp(getExp() + exp);
        if (canUP())upGrade();
    }

    public void addGrade(int grade) {
        setGrade(getGrade() + grade);
    }
}
