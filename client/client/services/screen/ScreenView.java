package client.services.screen;

import java.awt.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

import org.json.JSONObject;

import client.services.screen.ScreenGenerator;
import client.remote.SystemActions;
import client.resources.Utils;
import client.resources.Constants;

public class ScreenView
{
	private SystemActions system_actions;
	private ScreenGenerator screen_generator;

	private Map<String, Integer> screen_resolutions;
	private Map<String, Float> screen_quality;
	
	private boolean SEND_SCREEN = false;
	private int SCREEN_RESOLUTION;
	private float SCREEN_QUALITY;

	public ScreenView(SystemActions system_actions) {
		this.system_actions = system_actions;

		this.system_actions
			.addAction("ScreenView.startSendScreen", 
				params -> startSendScreen(params))
			.addAction("ScreenView.stopSendScreen",
				params -> stopSendScreen());

		screen_generator = new ScreenGenerator();

		screen_resolutions = new HashMap<>();
		screen_quality = new HashMap<>();
		setScreenResolutions();
		setScreenQuality(0.5f);
	}

	public void startSendScreen(Object[] params) {
		SEND_SCREEN = true;

		Thread thread = new Thread()
		{
			public void run() 
            {
				try {
					while(SEND_SCREEN && system_actions.getData("Connection")
							.getString("getControledId") != null) {
						byte[] screen = screen_generator.getCompressBytesScreen(
							(int)params[0], SCREEN_QUALITY);

						system_actions.getAction("sendScreen").accept(new Object[]{screen});

						int delay_factor = system_actions.getData("Connection")
							.getInt("getOutTrafficQueueSize")*250;

						Utils.loopDelay(delay_factor);

					}
				}
				catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		};
		thread.start();
	}

	public void stopSendScreen() {
		SEND_SCREEN = false;
	}

	private void setScreenResolutions() {

		int width = Constants.Monitor.width;
		Double percent = (25.0/100.0)*width;
		int low_width = width-percent.intValue();

		SCREEN_RESOLUTION = low_width;

		screen_resolutions.put(String.format("original", width), width);
		screen_resolutions.put(String.format("optimized", low_width), low_width);
		
	}

	public void setScreenQuality(float quality) {
		SCREEN_QUALITY = quality;
	}

	public float getScreenQuality() {
		return SCREEN_QUALITY;
	}
}