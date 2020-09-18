package server.watch;

import java.util.function.Consumer;
import java.util.Vector;

import server.resources.Utils;
import server.watch.TrafficWatch;
import traffic_model.TrafficModel;
import server.client.Client;

public class TrafficQueue
{
    private Vector<Object[]> TRAFFIC_QUEUE;
    private Consumer<Object[]> process_traffic;

	public TrafficQueue(Consumer<Object[]> process_traffic) {
		this.process_traffic = process_traffic;

        TRAFFIC_QUEUE = new Vector<Object[]>();

        this.thread_queue();
	}

	public void thread_queue() {
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
						process_traffic.accept(TRAFFIC_QUEUE.get(0));
						TRAFFIC_QUEUE.remove(0);
					}
                }
                catch (Exception exception) {
                	exception.printStackTrace();
					TRAFFIC_QUEUE.remove(0);
                	thread_queue();
                }
            }
        };
        thread.start();
	}

	public void add(Object[] params) {
        TRAFFIC_QUEUE.add(params);
    }
}