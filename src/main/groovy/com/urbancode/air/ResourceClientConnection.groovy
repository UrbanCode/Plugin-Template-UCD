package com.urbancode.air

import com.urbancode.ud.client.ResourceClient
import org.codehaus.jettison.json.JSONObject
import org.codehaus.jettison.json.JSONArray

class ResourceClientConnection{

	ResourceClient client
	ResourceClientConnection(UCDServerConnection sc) {
		client = new ResourceClient(new URI(sc.getWebURL()), sc.getUserName(), sc.getPassword())
	}

	public JSONObject getUCDResourceByPath(String path) {

		return(client.getResourceByPath(path))
	}
}
