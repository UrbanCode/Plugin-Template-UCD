package com.urbancode.air

import org.codehaus.jettison.json.JSONArray
import org.codehaus.jettison.json.JSONObject

class UCDResource {
	String id
	JSONObject RawJSON

    UCDResource(String id, JSONObject rawJSON) {
        this.id = id
		this.RawJSON = rawJSON
    }
	public JSONObject getJSON() { return RawJSON }
}
