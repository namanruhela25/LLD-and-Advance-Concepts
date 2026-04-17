package patterns.p6_commandDesign;

import patterns.p6_commandDesign.interfaces.Command;

public class RemoteControl {
    private int numofButtons = 4;
    private Command[] buttons;
    private boolean[] buttonPressed;

    public RemoteControl() {
        buttons = new Command[numofButtons];
        buttonPressed = new boolean[numofButtons];

        for (int i = 0; i < numofButtons; i++) {
            buttons[i] = null;
            buttonPressed[i] = false;
        }
    }

    public void setCommand(int buttonIndex, Command command) {
        if (buttonIndex >= 0 && buttonIndex < numofButtons) {
            buttons[buttonIndex] = command;
        } else {
            System.out.println("Invalid button index");
        }
    }

    public void pressButton(int buttonIndex) {
        if (buttonIndex >= 0 && buttonIndex < numofButtons) {
            if (!buttonPressed[buttonIndex]) {
                buttons[buttonIndex].execute();
                buttonPressed[buttonIndex] = true;
            } else {
                buttons[buttonIndex].undo();
                buttonPressed[buttonIndex] = false;
            }
        } else {
            System.out.println("No Command assign to this button");
        }
    }

}
