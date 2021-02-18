package services.screen;

import java.util.Map;
import java.util.HashMap;

import remote.ServerConnection;
import resources.Utils;
import resources.Constants;

public class ScreenCapture
{
	ServerConnection _SERVER_CONNECTION;
	private ScreenGenerator screen_generator;

	private Map<String, Integer> screen_resolutions;
	
	private boolean SEND_SCREEN = false;
	private float _SCREEN_QUALITY;
	private int _SCREEN_RESOLUTION;

	public ScreenCapture(ServerConnection server_connection) {
		_SERVER_CONNECTION = server_connection;

		screen_generator = new ScreenGenerator();

		screen_resolutions = new HashMap<>();
		setScreenResolutions();
		setScreenQuality(0.4f);
	}

	public void startSendScreen(int monitor_resolution) {
		SEND_SCREEN = true;
		_SCREEN_RESOLUTION = monitor_resolution;

		Thread thread = new Thread()
		{
			@Override
			public void run() 
            {
				try {
					while(SEND_SCREEN && _SERVER_CONNECTION.getControledUserId() != null) {
						byte[] screen = screen_generator.getCompressBytesScreen(
							_SCREEN_RESOLUTION, _SCREEN_QUALITY);
						
						_SERVER_CONNECTION._OUTCOMING_USER_ACTION.sendScreen(screen);

						int delay_factor = _SERVER_CONNECTION.getOutTrafficQueueSize()*400;

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
	
	public void setScreenResolution(int resolution) {
		_SCREEN_RESOLUTION = resolution;
	}

	public float getScreenQuality() {
		return _SCREEN_QUALITY;
	}
	
	public int getScreenResolution() {
		return _SCREEN_RESOLUTION;
	}
}