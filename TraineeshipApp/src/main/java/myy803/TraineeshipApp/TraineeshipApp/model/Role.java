package myy803.TraineeshipApp.TraineeshipApp.model;

public enum Role {
    STUDENT("Student"),
    PROFESSOR("Professor"),
    COMPANY("Company"),
    COMMITTEE("Committee");

    private final String value;

    private Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
