package com.urbancode.air

import com.urbancode.ud.client.ResourceClient

class UCDServerConnection {
    String webURL
    String userName
	String password

	UCDServerConnection(String weburl, String userName, String password) {
        this.webURL = weburl
		this.userName = userName
		this.password = password

    }
	String getWebURL() { return webURL }
	String getUserName() { return userName }
	String getPassword() { return password }
}
