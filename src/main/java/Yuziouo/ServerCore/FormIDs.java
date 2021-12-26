package Yuziouo.ServerCore;

public enum FormIDs {
    AbilityUI(-12),
    AbilityOPUI(-11),
    GradeUI(-10);
    private final int id;
    FormIDs(int i) {
        this.id = i;
    }

    public int getId() {
        return id;
    }
}
