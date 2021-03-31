package services.screen;

import java.util.Vector;

import org.json.JSONObject;

import remote.ServerConnection;
import resources.Utils;
import traffic_model.TrafficModel;

public class ScreenSender
{
	ServerConnection _SERVER_CONNECTION;
	private ScreenGenerator screen_generator;
	
	private boolean SEND_SCREEN = false;
	private float _SCREEN_QUALITY;
	private int _SCREEN_RESOLUTION;

	public ScreenSender(ServerConnection server_connection) {
		_SERVER_CONNECTION = server_connection;

		screen_generator = new ScreenGenerator();

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
						
						boolean hasImageToSend = hasImagesInQueue(
							_SERVER_CONNECTION.getTrafficQueue()
						);

						if(!hasImageToSend)
							_SERVER_CONNECTION._OUTCOMING_USER_ACTION.sendScreen(screen);

						Utils.loopDelay(250);
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

	public boolean hasImagesInQueue(Vector<TrafficModel> traffic_queue) {
		return traffic_queue.stream().anyMatch(
			traffic -> new JSONObject(
				new String(traffic.getMessage())
			).getString("action").equals("setScreenView") 
		);
	}
}