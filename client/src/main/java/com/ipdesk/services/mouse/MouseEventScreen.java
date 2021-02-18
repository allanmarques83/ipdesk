package services.mouse;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.function.Consumer;

import java.awt.Point;


public class MouseEventScreen
{
    Point _MOUSE_DRAG;

    Point _MOUSE_PRESSED;

    Consumer<Object[]> _MOUSE_EVENT;

    public MouseEventScreen(Consumer<Object[]> mouse_event) {
        _MOUSE_EVENT = mouse_event;

        _MOUSE_DRAG = null;
        _MOUSE_PRESSED = new Point(0,0);
    }

    public MouseListener getSceenMouseListner() {
        return new MouseListener() 
        {
            public void mousePressed(MouseEvent e) {
                _MOUSE_PRESSED.setLocation(e.getX(),e.getY());
            }

            public void mouseReleased(MouseEvent e) 
            {
                int mouse_released_x = e.getX();
                int mouse_released_y = e.getY();
                int BUTTON_MASK = InputEvent.getMaskForButton(
                    getButtonMask(e.getButton())
                );

                if(_MOUSE_DRAG != null) {
                    _MOUSE_EVENT.accept(new Object[]{
                        "MOUSE_DRAGGED_EVENT",
                        (int)_MOUSE_PRESSED.getX(),
                        (int)_MOUSE_PRESSED.getY(),
                        mouse_released_x,
                        mouse_released_y,
                        BUTTON_MASK,
                    });
                    _MOUSE_DRAG = null;
                }
                else {
                    _MOUSE_EVENT.accept(new Object[]{
                        "MOUSE_CLICK_EVENT",
                        0,
                        0,
                        (int)_MOUSE_PRESSED.getX(),
                        (int)_MOUSE_PRESSED.getY(),
                        BUTTON_MASK,
                    });
                }
            }
            public void mouseClicked(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
        };
    }

    public MouseMotionListener getSceenMouseMotionListner() {
        return new MouseMotionListener() 
        {
            public void mouseMoved(MouseEvent e) {
                // int BUTTON_MASK = InputEvent.getMaskForButton(
                //     getButtonMask(e.getButton())
                // );

                // _MOUSE_EVENT.accept(new Object[]{
                //     "MOUSE_MOVE_EVENT",
                //     0,
                //     0,
                //     e.getX(),
                //     e.getY(),
                //     BUTTON_MASK,
                // });
            }
            public void mouseDragged(MouseEvent e) {
                int BUTTON_MASK = InputEvent.getMaskForButton(
                    getButtonMask(e.getButton())
                );

                if(_MOUSE_DRAG == null) {
                    _MOUSE_DRAG = new Point(e.getX(),e.getY());

                    _MOUSE_EVENT.accept(new Object[]{
                        "MOUSE_DRAG_EVENT",
                        0,
                        0,
                        e.getX(),
                        e.getY(),
                        BUTTON_MASK,
                    });
                }
                else {
                    _MOUSE_DRAG.setLocation(e.getX(),e.getY());
                }
            }
        };
    }

    public MouseWheelListener getMouseWheelListener() {
        return new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
                 _MOUSE_EVENT.accept(new Object[]{
                "MOUSE_WHELL_EVENT",
                    0,
                    0,
                    0,
                    0,
                    e.getWheelRotation(),
                });
            }
        };
    }

    private int getButtonMask(int button) {
        if(button > 0) {
            return button;
        }
        return 1;
    }

}
