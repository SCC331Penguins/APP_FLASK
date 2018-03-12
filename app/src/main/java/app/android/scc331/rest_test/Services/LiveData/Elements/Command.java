package app.android.scc331.rest_test.Services.LiveData.Elements;

import app.android.scc331.rest_test.Objects.Actuator;

public class Command {

    private Actuator actuator;
    private String command;

    public Command(Actuator actuator, String command){
        this.command = command;
        this.actuator = actuator;
    }

    public String getMACAddress() {
        return actuator.getId();
    }

    public String getCommand() {
        return command;
    }
}
