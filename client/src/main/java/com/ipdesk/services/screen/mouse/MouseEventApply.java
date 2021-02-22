package services.screen.mouse;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import resources.Constants;
import resources.Utils;

public class MouseEventApply 
{
    Robot _ROBOT;

    public Map<String, Consumer<Object[]>> _MOUSE_ACTIONS;

    int _MONITOR_RESOLUTION_W;
    int _MONITOR_RESOLUTION_H;
    
    public MouseEventApply() {
        _ROBOT = Utils.getRobotInstance();

        _MOUSE_ACTIONS = new HashMap<String, Consumer<Object[]>>();

        _MONITOR_RESOLUTION_W = Constants.Monitor.width;
        _MONITOR_RESOLUTION_H = Constants.Monitor.height;

        _MOUSE_ACTIONS.put("MOUSE_CLICK_EVENT", args -> mouseClickEvent(args));
        _MOUSE_ACTIONS.put("MOUSE_MOVE_EVENT", args -> mouseMoveEvent(args));
        _MOUSE_ACTIONS.put("MOUSE_DRAGGED_EVENT", args -> mouseDraggedEvent(args));
        _MOUSE_ACTIONS.put("MOUSE_DRAG_EVENT", args -> mouseDragEvent(args));
        _MOUSE_ACTIONS.put("MOUSE_WHELL_EVENT", args -> mouseWhellEvent(args));
    }

    public void mouseClickEvent(Object[] mouse_args) 
    {
        int to_x = (int)mouse_args[2];
        int to_y = (int)mouse_args[3];
        int screen_view_width = (int)mouse_args[5];
        int screen_view_height = (int)mouse_args[6];
        int button_mask = (int)mouse_args[4];
        
        int click_position_x = Utils.getClickMousePosition(
            (double)to_x, (double)screen_view_width, _MONITOR_RESOLUTION_W
        );
        int click_position_y = Utils.getClickMousePosition(
            (double)to_y, (double)screen_view_height, _MONITOR_RESOLUTION_H
        );

        _ROBOT.mouseMove(click_position_x, click_position_y);
        _ROBOT.mousePress( button_mask );
        _ROBOT.delay(25);
        _ROBOT.mouseRelease( button_mask );
        _ROBOT.mouseRelease( InputEvent.BUTTON1_DOWN_MASK );
    }

    public void mouseDraggedEvent(Object[] mouse_args) 
    {
        int from_x = (int)mouse_args[0];
        int from_y = (int)mouse_args[1];
        int to_x = (int)mouse_args[2];
        int to_y = (int)mouse_args[3];
        int screen_view_width = (int)mouse_args[5];
        int screen_view_height = (int)mouse_args[6];
        int button_mask = (int)mouse_args[4];

        
        int from_drag_x = Utils.getClickMousePosition(
            (double)from_x, (double)screen_view_width, _MONITOR_RESOLUTION_W
        );
        int from_drag_y = Utils.getClickMousePosition(
            (double)from_y, (double)screen_view_height, _MONITOR_RESOLUTION_H
        );
        int to_drag_x = Utils.getClickMousePosition(
            (double)to_x, (double)screen_view_width, _MONITOR_RESOLUTION_W
        );
        int to_drag_y = Utils.getClickMousePosition(
            (double)to_y, (double)screen_view_height, _MONITOR_RESOLUTION_H
        );

        _ROBOT.delay(25);
        _ROBOT.mouseMove(from_drag_x,from_drag_y);
        _ROBOT.mousePress( button_mask );
        _ROBOT.delay(100);
        _ROBOT.mouseMove(to_drag_x,to_drag_y);
        _ROBOT.delay(250);
        _ROBOT.mouseRelease( button_mask );
        _ROBOT.mouseRelease( InputEvent.BUTTON1_DOWN_MASK );
    }

    public void mouseDragEvent(Object[] mouse_args) 
    {
        int button_mask = (int)mouse_args[4];

        _ROBOT.mousePress(button_mask);
    }
    public void mouseMoveEvent(Object[] mouse_args) 
    {
        int to_x = (int)mouse_args[2];
        int to_y = (int)mouse_args[3];
        int screen_view_width = (int)mouse_args[5];
        int screen_view_height = (int)mouse_args[6];
        
        int click_position_x = Utils.getClickMousePosition(
            (double)to_x, (double)screen_view_width, _MONITOR_RESOLUTION_W
        );
        int click_position_y = Utils.getClickMousePosition(
            (double)to_y, (double)screen_view_height, _MONITOR_RESOLUTION_H
        );

        _ROBOT.mouseMove(click_position_x, click_position_y);
        // _ROBOT.delay(25);
    }

    public void mouseWhellEvent(Object[] mouse_args) 
    {
        int scroll = (int)mouse_args[4];
		_ROBOT.mouseWheel((scroll*2));
    }
}
