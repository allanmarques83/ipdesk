package services.screen.keyboard;

import java.awt.event.KeyListener;
import java.util.function.Consumer;
import java.awt.event.KeyEvent;

public class KeyboardEventScreen implements KeyListener 
{
    Consumer<Object[]> _keyboardEvent;

    public KeyboardEventScreen(Consumer<Object[]> keyboard_event) {
        _keyboardEvent = keyboard_event;
    }

    public void keyPressed( KeyEvent e ) 
	{
        _keyboardEvent.accept(
            new Object[] { "KEYBOARD_PRESSED", e.getKeyCode() }
        );
    }
    public void keyReleased( KeyEvent e ) 
	{
        _keyboardEvent.accept(
            new Object[] { "KEYBOARD_RELEASED", e.getKeyCode() }
        );
    }
    public void keyTyped( KeyEvent e ) 
	{
        //null
    }
}
