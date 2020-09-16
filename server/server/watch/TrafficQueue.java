package server.watch;

import javafx.collections.ObservableMap;
import java.util.Vector;

import server.resources.Utils;
import server.watch.TrafficWatch;
import traffic_model.TrafficModel;
import server.client.Client;

public class TrafficQueue
{
    private ObservableMap<String, Client> connections;
    private ObservableMap<String, String> sessions;

    private Vector<Object[]> TRAFFIC_QUEUE;

    private TrafficWatch traffic_watch;

	public TrafficQueue(ObservableMap<String, Client> connections) {
		this.connections = connections;

		traffic_watch = new TrafficWatch(connections);

        TRAFFIC_QUEUE = new Vector<Object[]>();

        this.process_queue();
	}

	public void process_queue() {
		Thread thread = new Thread()
		{
			public void run() 
            {
				try {
					while(true) {
						
						if(TRAFFIC_QUEUE.isEmpty()) {
							Utils.loopDelay(1);
							continue;
						}
						traffic_watch.process(TRAFFIC_QUEUE.get(0));
						TRAFFIC_QUEUE.remove(0);
					}
                }
                catch (Exception exception) {

                }
            }
        };
        thread.start();
	}

	public void add(Object[] params) {
        TRAFFIC_QUEUE.add(params);
    }
}