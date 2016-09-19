package com.urbancode.air

import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet

import com.urbancode.commons.httpcomponentsutil.HttpClientBuilder

import groovyx.net.http.RESTClient
import groovyx.net.http.ContentType
import static groovyx.net.http.ContentType.JSON
import groovy.json.JsonOutput
import groovyx.net.http.AuthConfig

import javax.net.ssl.SSLContext

import org.apache.http.conn.scheme.SchemeSocketFactory
import org.apache.http.conn.ssl.SSLSocketFactory

import javax.net.ssl.X509TrustManager
import javax.net.ssl.TrustManager

import org.apache.http.conn.scheme.Scheme
import org.apache.http.HttpResponse

import com.urbancode.air.UCDServerConnection

class httpRequestProcess {
	HttpClientBuilder clientBuilder
	DefaultHttpClient client
	UCDServerConnection serverConnection
	int counter
	boolean debug

	httpRequestProcess(UCDServerConnection serverConnection, boolean debug) {
		this.serverConnection = serverConnection

		clientBuilder = new HttpClientBuilder()
		clientBuilder.setUsername(serverConnection.getUserName())
		clientBuilder.setPassword(serverConnection.getPassword())
		clientBuilder.setTrustAllCerts(true)
		client = clientBuilder.buildClient()
		this.debug = debug
		counter = 0
	}

	public String httpGetResource(String resourceID) {
		String completeURL = serverConnection.getWebURL() + "/rest/resource/resource/" + resourceID + "/resources"

		return(httpGet(completeURL))

	}

	public String httpGetResourceProps(String propSheet, Integer version) {

		def urlPropSheet = propSheet.replaceAll("/", "%26")
		String completeURL = serverConnection.getWebURL() + "/property/propSheet/" + urlPropSheet + "." + version.toString()

		return(httpGet(completeURL))
	}

	private String httpGet(String url) {
		HttpGet request = new HttpGet(new URI(url))
		HttpResponse resp = client.execute(request)
		BufferedReader br = new BufferedReader (new InputStreamReader(resp.getEntity().getContent()))
		String responseText = ""

		String currentLine = new String();

		while ((currentLine = br.readLine()) != null){
			responseText += currentLine
		}
		return(responseText)

	}


/*
	public String httpwrite() {

		RESTClient restClient = new RESTClient(serverConnection.getWebURL() + "/rest/resource/resource/95be8206-b245-49ea-aa37-92ffd92166db/propertiesForRole/4bf413f9-5cea-4aa0-ada0-c6de28bc4c9e?sortType=asc", groovyx.net.http.ContentType.JSON)
		restClient.parser.'application/xml' = restClient.parser.'text/plain'
		restClient.parser.'application/xhtml+xml' = restClient.parser.'text/plain'
		restClient.parser.'application/atom+xml' = restClient.parser.'text/plain'
		restClient.parser.'application/json' = restClient.parser.'text/plain'
		restClient.parser.'text/html' = restClient.parser.'text/plain'
		restClient.parser.'application/x-www-form-urlencoded' = restClient.parser.'text/plain'

		def nullTrustManager = [
			checkClientTrusted: {chain, authType -> },
			checkServerTrusted: {chain, authType -> },
			getAcceptedIssuers: { null }
			]

		def nullHostnameVerifier = [
			verify: { hostname, session -> true }
			]

		SSLContext sslContext = SSLContext.getInstance("SSL_TLS");
		sslContext.init(null, [nullTrustManager as X509TrustManager] as TrustManager[], null);

		javax.net.ssl.SSLSocketFactory factory = sslContext.getSocketFactory();
		SchemeSocketFactory schemeSocketFactory = new SSLSocketFactory(factory, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Scheme scheme = new Scheme("https",  443, schemeSocketFactory);
		restClient.client.connectionManager.schemeRegistry.register(scheme)

		AuthConfig auth = new AuthConfig(restClient)
		auth.basic(serverConnection.getUserName(), serverConnection.getPassword())
		restClient.setAuthConfig(auth)
		def headerMap = [:]

		def body = ""

		/*{\"name\":\"" + toVersion
		body += "\",\"description\":\"" + description
		body += "\",\"copyId\":\"" + fromVersionID
		body += "\"}"

		if (debug) { println("BODY : " + body) }

		def response = restClient.put("headers":headerMap, "body":body)

		if (debug) {
			println("Status: " + response.status)
			if (response.data) {
				println("\tContent Type: " + response.contentType)
				println("\tHeaders: " + response.getAllHeaders())
				println("\tBody:\n" + JsonOutput.prettyPrint(JsonOutput.toJson(response.data)))
			}
		}
	}



	public String httpGetSnapshots(String ApplicationID) {
		String completeURL = serverConnection.getWebURL() + "/rest/deploy/application/" + ApplicationID + "/snapshots/false"

		if (debug) { println("REQ URL : " + completeURL + "   ") }

		HttpGet request = new HttpGet(new URI(completeURL))
		HttpResponse resp = client.execute(request)
		BufferedReader br = new BufferedReader (new InputStreamReader(resp.getEntity().getContent()))
		String responseText = ""

		String currentLine = new String();

		while ((currentLine = br.readLine()) != null){
			responseText += currentLine
		}
		return(responseText)
	}


	public String httpGetResources() {
		String completeURL = serverConnection.getWebURL() + "/rest/resource/resource"

		if (debug) { println("REQ URL : " + completeURL + "   ") }

		HttpGet request = new HttpGet(new URI(completeURL))
		HttpResponse resp = client.execute(request)
		BufferedReader br = new BufferedReader (new InputStreamReader(resp.getEntity().getContent()))
		String responseText = ""

		String currentLine = new String();

		while ((currentLine = br.readLine()) != null){
			responseText += currentLine
		}
		return(responseText)
	}

	public String httpGetRoleProps() {
		String completeURL = serverConnection.getWebURL() + "/rest/resource/resource/95be8206-b245-49ea-aa37-92ffd92166db/propertiesForRole/4bf413f9-5cea-4aa0-ada0-c6de28bc4c9e?sortType=asc"

		if (debug) { println("REQ URL : " + completeURL + "   ") }

		HttpGet request = new HttpGet(new URI(completeURL))
		HttpResponse resp = client.execute(request)
		BufferedReader br = new BufferedReader (new InputStreamReader(resp.getEntity().getContent()))
		String responseText = ""

		String currentLine = new String();

		while ((currentLine = br.readLine()) != null){
			responseText += currentLine
		}
		return(responseText)
	}

	public String httpGetComponentDetails(String ComponentID) {
		String completeURL = serverConnection.getWebURL() + "/rest/deploy/component/" + ComponentID

		if (debug) { println("REQ URL : " + completeURL + "   ") }

		HttpGet request = new HttpGet(new URI(completeURL))
		HttpResponse resp = client.execute(request)
		BufferedReader br = new BufferedReader (new InputStreamReader(resp.getEntity().getContent()))
		String responseText = ""

		String currentLine = new String();

		while ((currentLine = br.readLine()) != null){
			responseText += currentLine
		}
		return(responseText)
	}

	public String httpPutCreateComponentVersionPropertyDef(String propertyName, String componentID) {
		def requiredURL = serverConnection.getWebURL() + "/property/propSheetDef/components%26" + componentID + "%26versionPropSheetDef.-1/propDefs"
		if (debug) {
			println("URL for PUT : " + requiredURL)
		}
		RESTClient restClient = new RESTClient(requiredURL, groovyx.net.http.ContentType.JSON)
		// restClient.parser.'application/xml' = restClient.parser.'text/plain'
		// restClient.parser.'application/xhtml+xml' = restClient.parser.'text/plain'
		// restClient.parser.'application/atom+xml' = restClient.parser.'text/plain'
		// restClient.parser.'application/json' = restClient.parser.'text/plain'
		// restClient.parser.'text/html' = restClient.parser.'text/plain'
		// restClient.parser.'application/x-www-form-urlencoded' = restClient.parser.'text/plain'

		def nullTrustManager = [
			checkClientTrusted: {chain, authType -> },
			checkServerTrusted: {chain, authType -> },
			getAcceptedIssuers: { null }
			]

		def nullHostnameVerifier = [
			verify: { hostname, session -> true }
			]

		SSLContext sslContext = SSLContext.getInstance("SSL_TLS");
		sslContext.init(null, [nullTrustManager as X509TrustManager] as TrustManager[], null);

		javax.net.ssl.SSLSocketFactory factory = sslContext.getSocketFactory();
		SchemeSocketFactory schemeSocketFactory = new SSLSocketFactory(factory, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Scheme scheme = new Scheme("https",  443, schemeSocketFactory);
		restClient.client.connectionManager.schemeRegistry.register(scheme)

		AuthConfig auth = new AuthConfig(restClient)
		auth.basic(serverConnection.getUserName(), serverConnection.getPassword())
		restClient.setAuthConfig(auth)
		def headerMap = [:]

		def body = "{\"name\":\"" + propertyName
		body += "\",\"description\":\"\",\"label\":\"\",\"pattern\":\"\",\"required\":\"true\",\"type\":\"TEXT\",\"value\":\"\""
		body += ",\"definitionGroupId\":\"8e7b3708-9abb-4e9e-a161-3d64e7e3aa15\"}"

		if (debug) { println("BODY : " + body) }

		def response = restClient.put("headers":headerMap, "body":body)

		if (debug) {
			println("Status: " + response.status)
			if (response.data) {
				println("\tContent Type: " + response.contentType)
				println("\tHeaders: " + response.getAllHeaders())
				println("\tBody:\n" + JsonOutput.prettyPrint(JsonOutput.toJson(response.data)))
			}
		}
	}

	*/

}
