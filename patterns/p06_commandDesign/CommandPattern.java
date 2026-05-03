package patterns.p6_commandDesign;

import patterns.p6_commandDesign.commands.FanCommand;
import patterns.p6_commandDesign.commands.LightCommand;
import patterns.p6_commandDesign.controlObjects.Fan;
import patterns.p6_commandDesign.controlObjects.Light;

public class CommandPattern {
    public static void main(String[] args) {
        Fan fan = new Fan();
        Light light = new Light();

        RemoteControl remote = new RemoteControl();

        remote.setCommand(0, new FanCommand(fan));
        remote.setCommand(1, new LightCommand(light));


        System.out.println("--- Toggling Light Button 0 ---");
        remote.pressButton(0);  // ON
        remote.pressButton(0);  // OFF

        System.out.println("--- Toggling Fan Button 1 ---");
        remote.pressButton(1);  // ON
        remote.pressButton(1);  // OFF

        System.out.println("--- Pressing Unassigned Button 2 ---");
        remote.pressButton(2);


        

    }
}
