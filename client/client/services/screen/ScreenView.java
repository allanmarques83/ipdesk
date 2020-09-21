package client.services.screen;

import java.awt.*;
import java.util.Map;
import java.util.HashMap;

import org.json.JSONObject;

import client.services.screen.ScreenGenerator;
import client.remote.Connection;
import client.resources.Utils;

public class ScreenView
{
	private Connection connection;
	private ScreenGenerator screen_generator;

	private Map<String, Integer> screen_resolutions;
	private Map<String, Float> screen_quality;
	
	private boolean SEND_SCREEN = false;
	private int SCREEN_RESOLUTION;
	private float SCREEN_QUALITY;

	public ScreenView(Connection connection) {
		this.connection = connection;

		screen_generator = new ScreenGenerator();

		screen_resolutions = new HashMap<>();
		screen_quality = new HashMap<>();
		setScreenResolutions();
		setScreenQuality(0.5f);
	}

	public void startSendScreen() {
		SEND_SCREEN = true;

		Thread thread = new Thread()
		{
			public void run() 
            {
				try {
					while(SEND_SCREEN && connection.getControledId() != null) {
						byte[] screen = screen_generator.getCompressBytesScreen(
							SCREEN_RESOLUTION, SCREEN_QUALITY);
						System.out.println(screen.length);

						connection.sendTraffic(new JSONObject()
							.put("destination_id", connection.getControledId())
							.put("action", "setScreenView")
							.put("original_screen_resolution", 
								screen_resolutions.get("original").toString())
							.put("optimized_screen_resolution", 
								screen_resolutions.get("optimized").toString())
							.toString().getBytes(), screen);

						Utils.loopDelay(500);

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
		Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();

		int width = screen_size.width;
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