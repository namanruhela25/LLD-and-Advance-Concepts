package patterns.p6_commandDesign.commands;

import patterns.p6_commandDesign.controlObjects.Fan;
import patterns.p6_commandDesign.interfaces.Command;

public class FanCommand implements Command {

    private Fan fan;

    public FanCommand(Fan fan) {
        this.fan = fan;
    }

    @Override
    public void execute() {
        fan.on();
    }

    @Override
    public void undo() {
        fan.off();
    }
    
}
