package client.remote;

import org.json.JSONObject;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;

public class SystemActions
{
	private Map<String, Consumer<Object[]>> actions;
	private Map<String, Supplier<JSONObject>> data;
	
	public SystemActions() {
		this.actions = new HashMap<>();
		this.data = new HashMap<>();
	}

	public SystemActions addAction(String action_name, Consumer<Object[]> action) {
		actions.put(action_name, action);
		return this;
	}

	public Consumer<Object[]> getAction(String action_name) {
		return actions.get(action_name);
	}

	public SystemActions addData(String data_name, Supplier<JSONObject> data) {
		this.data.put(data_name, data);
		return this;
	}

	public JSONObject getData(String data_name) {
		return data.get(data_name).get();
	}
}