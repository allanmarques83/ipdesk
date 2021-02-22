package services.screen.keyboard;

import java.awt.Robot;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import resources.Utils;

public class KeyboardEventApply {
    Robot _ROBOT;

    public Map<String, Consumer<Object[]>> _KEYBOARD_ACTIONS;

    public KeyboardEventApply() {
        _ROBOT = Utils.getRobotInstance();

        _KEYBOARD_ACTIONS = new HashMap<String, Consumer<Object[]>>();

        _KEYBOARD_ACTIONS.put("KEYBOARD_PRESSED", args -> keyboardPressedEvent(args));
        _KEYBOARD_ACTIONS.put("KEYBOARD_RELEASED", args -> keyboardReleasedEvent(args));
    }

    private void keyboardPressedEvent(Object[] args) {
        int key_code = (int)args[0];
        
        if(key_code == 86 && _ROBOT.isAutoWaitForIdle()) {
            _ROBOT.setAutoWaitForIdle(false);
        }
        else {
            try {
                _ROBOT.keyPress(key_code);
            }
            catch (IllegalArgumentException exception) {
                //
            }
        }

        if(key_code == 17) {
            _ROBOT.setAutoWaitForIdle(true);
        }
    }

    private void keyboardReleasedEvent(Object[] args) {
        int key_code = (int)args[0];
        try {
            _ROBOT.keyRelease(key_code);   
        }
        catch (IllegalArgumentException exception) {
            //
        }
    }
}
