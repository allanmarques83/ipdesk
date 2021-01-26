package main.services.screen;

import java.util.Map;
import java.util.HashMap;

import main.remote.ServerConnection;
import main.resources.Utils;
import main.resources.Constants;

public class ScreenView
{
	ServerConnection _SERVER_CONNECTION;
	private ScreenGenerator screen_generator;

	private Map<String, Integer> screen_resolutions;
	
	private boolean SEND_SCREEN = false;
	private float _SCREEN_QUALITY;

	public ScreenView(ServerConnection server_connection) {
		_SERVER_CONNECTION = server_connection;

		screen_generator = new ScreenGenerator();

		screen_resolutions = new HashMap<>();
		setScreenResolutions();
		setScreenQuality(0.4f);
	}

	public void startSendScreen(int monitor_resolution) {
		SEND_SCREEN = true;

		Thread thread = new Thread()
		{
			@Override
			public void run() 
            {
				try {
					while(SEND_SCREEN && _SERVER_CONNECTION.getControledUserId() != null) {
						byte[] screen = screen_generator.getCompressBytesScreen(
							monitor_resolution, _SCREEN_QUALITY);
						
						_SERVER_CONNECTION._OUTCOMING_USER_ACTION.sendScreen(screen);

						int delay_factor = _SERVER_CONNECTION.getOutTrafficQueueSize()*750;

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

		screen_resolutions.put(String.format("original", width), width);
		screen_resolutions.put(String.format("optimized", low_width), low_width);
	}

	public void setScreenQuality(float quality) {
		_SCREEN_QUALITY = quality;
	}

	public float getScreenQuality() {
		return _SCREEN_QUALITY;
	}
}