package com.urbancode.air

import java.util.Map

import com.urbancode.air.UCDServerConnection
import com.urbancode.air.ResourceClientConnection
import com.urbancode.air.UCDResource
import com.urbancode.air.httpRequestProcess

import org.codehaus.jettison.json.JSONObject
import org.codehaus.jettison.json.JSONArray
import groovy.json.JsonSlurper

import com.urbancode.ud.client.ResourceClient

public class Resources {
	def Resources = []
	def requiredProperties = [:]
	def requiredPathType = [:]
	Integer displayDepth = 1
	Integer displayKeyValuePadding = 21
	String[] displayOrder = ["name", "path", "id", "description", "active", "type", "status", "hasAgent", "securityResourceId", "inheritTeam", "discoveryFailed", "prototype", "impersonationPassword", "impersonationUseSudo", "impersonationForce"]

	UCDServerConnection serverConnection
	Integer emptyResourceProperties
	Integer emptyResourceRoleProperties
	Integer foundResourceProperties
	Integer foundResourceRoleProperties

	Resources(UCDServerConnection serverConnection) {
		this.serverConnection = serverConnection

		Resources = []
	
		requiredProperties.put("name", (boolean) "false".toBoolean())
		requiredProperties.put("path", (boolean) "false".toBoolean())
		requiredProperties.put("roleProperties", (boolean) "false".toBoolean())			
		requiredProperties.put("resourceProperties", (boolean) "false".toBoolean())
		requiredProperties.put("id", (boolean) "false".toBoolean())
		requiredProperties.put("active", (boolean) "false".toBoolean())
		requiredProperties.put("hasAgent", (boolean) "false".toBoolean())
		requiredProperties.put("status", (boolean) "false".toBoolean())
		requiredProperties.put("type", (boolean) "false".toBoolean())
		requiredProperties.put("propSheet", (boolean) "false".toBoolean())
		requiredProperties.put("role", (boolean) "false".toBoolean())
		requiredProperties.put("tags", (boolean) "false".toBoolean())
		requiredProperties.put("discoveryFailed", (boolean) "false".toBoolean())
		requiredProperties.put("prototype", (boolean) "false".toBoolean())
		requiredProperties.put("description", (boolean) "false".toBoolean())
		requiredProperties.put("parent", (boolean) "false".toBoolean())
		requiredProperties.put("parentRole", (boolean) "false".toBoolean())
		requiredProperties.put("impersonationForce", (boolean) "false".toBoolean())
		requiredProperties.put("impersonationPassword", (boolean) "false".toBoolean())
		requiredProperties.put("impersonationUseSudo", (boolean) "false".toBoolean())
		requiredProperties.put("impersonationUser", (boolean) "false".toBoolean())
		requiredProperties.put("impersonationGroup", (boolean) "false".toBoolean())
		requiredProperties.put("inheritTeam", (boolean) "false".toBoolean())
		requiredProperties.put("securityResourceId", (boolean) "false".toBoolean())
		requiredProperties.put("extendedSecurity", (boolean) "false".toBoolean())
		requiredProperties.put("security", (boolean) "false".toBoolean())
		
	}
	
	public void getConfigFileDetails(String configFileName) {
		def fileHandle = new File(configFileName)
		def fileContent = fileHandle.readLines()

		fileContent.each {
			def mapName = it.substring(0, it.indexOf('_'))
			def mapItem = it.substring(it.indexOf('_') + 1, it.indexOf(':'))
			def mapValue = it.substring(it.indexOf(':') + 1, it.length())
			if (mapName == "requiredProperties") {
				requiredProperties.put(mapItem, (boolean) mapValue.toBoolean())
			} else {
				requiredPathType.put(mapItem, (boolean) mapValue.toBoolean())
			}
		}
	}
	
	public void setRequiredPropertiesAndPathType(String outputContent_Properties, String outputContent_Security, String outputContent_FurtherDetails, String pathType) {
		
		
		// Properties that are always on :
		
		requiredProperties.put("name", (boolean) "true".toBoolean())
		requiredProperties.put("path", (boolean) "true".toBoolean())
		
		// Properties relating to resource properties and resource role properties :
		
		if (outputContent_Properties == "true") {
			requiredProperties.put("roleProperties", (boolean) "true".toBoolean())
			requiredProperties.put("resourceProperties", (boolean) "true".toBoolean())
		}
		
		// Other properties grouped into 'further details' :
		
		if (outputContent_FurtherDetails == "true") {
			requiredProperties.put("id", (boolean) "true".toBoolean())
			requiredProperties.put("active", (boolean) "true".toBoolean())
			requiredProperties.put("hasAgent", (boolean) "true".toBoolean())
			requiredProperties.put("status", (boolean) "true".toBoolean())
			requiredProperties.put("type", (boolean) "true".toBoolean())
			requiredProperties.put("propSheet", (boolean) "true".toBoolean())
			requiredProperties.put("role", (boolean) "true".toBoolean())
			requiredProperties.put("tags", (boolean) "true".toBoolean())
			requiredProperties.put("discoveryFailed", (boolean) "true".toBoolean())
			requiredProperties.put("prototype", (boolean) "true".toBoolean())
			requiredProperties.put("description", (boolean) "true".toBoolean())
			requiredProperties.put("parent", (boolean) "true".toBoolean())
			requiredProperties.put("parentRole", (boolean) "true".toBoolean())
		}
		
		// Properties relating to security and impersonation :
		
		if (outputContent_Security == "true") {
			requiredProperties.put("impersonationForce", (boolean) "true".toBoolean())
			requiredProperties.put("impersonationPassword", (boolean) "true".toBoolean())
			requiredProperties.put("impersonationUseSudo", (boolean) "true".toBoolean())
			requiredProperties.put("impersonationUser", (boolean) "true".toBoolean())
			requiredProperties.put("impersonationGroup", (boolean) "true".toBoolean())
			requiredProperties.put("inheritTeam", (boolean) "true".toBoolean())
			requiredProperties.put("securityResourceId", (boolean) "true".toBoolean())
			requiredProperties.put("extendedSecurity", (boolean) "true".toBoolean())
			requiredProperties.put("security", (boolean) "true".toBoolean())
		}
		// Path type :
		
		if (pathType == "relative") {
			requiredPathType.put("full", (boolean) "false".toBoolean())
			requiredPathType.put("relative", (boolean) "true".toBoolean())
		} else {
		requiredPathType.put("full", (boolean) "true".toBoolean())
		requiredPathType.put("relative", (boolean) "false".toBoolean())
		}
	}
	
	public printRequiredProps() {
		
		if(requiredProperties.get("roleProperties")) {
			println("role properties required")
		} else {
			println("role properties NOT required")
		}
		
		println(requiredProperties.get("name"))
		println(requiredProperties.get("path"))
		println(requiredProperties.get("roleProperties"))
		println(requiredProperties.get("resourceProperties"))
		println(requiredProperties.get("id"))
		println(requiredProperties.get("active"))
		println(requiredProperties.get("hasAgent"))
		println(requiredProperties.get("status"))
		println(requiredProperties.get("type"))
		println(requiredProperties.get("propSheet"))
		println(requiredProperties.get("role"))
		println(requiredProperties.get("tags"))
		println(requiredProperties.get("discoveryFailed"))
		println(requiredProperties.get("prototype"))
		println(requiredProperties.get("description"))
		println(requiredProperties.get("parent"))
		println(requiredProperties.get("parentRole"))
		println(requiredProperties.get("impersonationForce"))
		println(requiredProperties.get("impersonationPassword"))
		println(requiredProperties.get("impersonationUseSudo"))
		println(requiredProperties.get("impersonationUser"))
		println(requiredProperties.get("impersonationGroup"))
		println(requiredProperties.get("inheritTeam"))
		println(requiredProperties.get("securityResourceId"))
		println(requiredProperties.get("extendedSecurity"))
		println(requiredProperties.get("security"))

		
	}

	public void getResources(String rootPath, httpRequestProcess httpCon, String nameFilter) {

		ResourceClientConnection RCC = new ResourceClientConnection(serverConnection)
		JSONObject resourceJSON = RCC.getUCDResourceByPath(rootPath)

		if (resourceJSON != null) {
			JSONObject resourceDetailsJSON = getResourceDetails(RCC, resourceJSON.getString("id"), httpCon)

			UCDResource newResource = new UCDResource(resourceJSON.getString("id"), resourceDetailsJSON)
			Resources[0] = newResource

			def resourcesFromChild = []
			resourcesFromChild = getChildResources(resourceJSON.getString("id"), RCC, httpCon, rootPath, nameFilter)
			if((resourcesFromChild != null) && (resourcesFromChild.size() > 0)) {

				resourcesFromChild.each { item ->
					Resources[Resources.size()] = item
				}
			}
		}
	}

	private UCDResource[] getChildResources(String parent, ResourceClientConnection RCC, httpRequestProcess httpProcess, String rootPath, String nameFilter) {
		def childResources = []

		String processResult = httpProcess.httpGetResource(parent)

		if ((processResult.contains("[]") && processResult.length() < 5)) {
			return(null)
		} else {
			JSONArray childResourcesJSON = new JSONArray(processResult)
			for (int JSONObjectCounter = 0; JSONObjectCounter < childResourcesJSON.length(); JSONObjectCounter++) {
				JSONObject resourceJSON = childResourcesJSON.getJSONObject(JSONObjectCounter);

				JSONObject childResourceDetailsJSON = getResourceDetails(RCC, resourceJSON.getString("id"), httpProcess)
				UCDResource newResource = new UCDResource(childResourceDetailsJSON.getString("id"), childResourceDetailsJSON)

				if (childResourceDetailsJSON.getString("name").contains(nameFilter)) {
					childResources += newResource
				}
				def resourcesFromChild = getChildResources(childResourceDetailsJSON.getString("id"), RCC, httpProcess, rootPath, nameFilter)
				if((resourcesFromChild != null) && (resourcesFromChild.size() > 0)) {
					resourcesFromChild.each { item ->
						childResources += item
					}
				}
			}
			return(childResources)
		}
	}
	private JSONObject getResourceDetails(ResourceClientConnection RCC, String id, httpRequestProcess httpProcess) {
		JSONObject resourceDetailsJSON = RCC.getUCDResourceByPath(id)

		def detailsSlurper = new JsonSlurper()
		def detailsJSONObject = detailsSlurper.parseText(resourceDetailsJSON.toString())

		if (detailsJSONObject.propSheet.path != null) {

			String resourceProps = httpProcess.httpGetResourceProps(detailsJSONObject.propSheet.path, detailsJSONObject.propSheet.version)

			def resourcePropsSlurper = new JsonSlurper()
			def resourcePropsJSONObject = resourcePropsSlurper.parseText(resourceProps.toString())

			JSONObject resourceProperties = new JSONObject()

			JSONArray properties = new JSONArray()

			if (resourcePropsJSONObject.properties.size() > 0) {
				for (r in resourcePropsJSONObject.properties) {
					JSONObject newProps = new JSONObject()
					newProps.put("name", r.name)
					newProps.put("value", r.value)
					newProps.put("id", r.id)
					newProps.put("secure", r.secure)
					newProps.put("description", r.description)
					properties.put(newProps)
				}
			}
			for (r in resourcePropsJSONObject) {
				if (r.key != "properties") {
					resourceProperties.put(r.key, r.value.toString())

				}
			}

			if ((resourcePropsJSONObject.properties) && (resourcePropsJSONObject.properties.size() > 0)) {
				resourceProperties.put("properties", properties)
				resourceDetailsJSON.put("resourceProperties", resourceProperties)

			}
		}

		return(resourceDetailsJSON)
	}

	private boolean checkRequiredProperties(String prop) {
		
		return (requiredProperties.get(prop))
	}

	private int getMaxKeyLength(map) {
		def maxKeyLength = 0
		for (i in map) {
			def keyLength = "${i.key}".length()
			if (keyLength > maxKeyLength) { maxKeyLength = keyLength }
		}
		return(maxKeyLength)
	}
	public String ListResources(String rootPath) {
		def outputString = ""
		Resources.each { resourceItem ->
			def detailsSlurper = new JsonSlurper()
			def detailsJSONObject = resourceItem.getJSON()

			def keysFound = []

			Iterator<?> keys = detailsJSONObject.keys()
			while( keys.hasNext() ) {
				String key = (String) keys.next()
				keysFound[keysFound.size()] = key
			}

			displayOrder.each { item ->
				if (keysFound.contains(item)) {
					if (checkRequiredProperties(item)) {
						if (item == "path") {
							// Display the full path

							if (requiredPathType.get("full")) {
								outputString += sprintf("%-${displayDepth}s-- %-${displayKeyValuePadding}s -> %s\n", "", item, detailsJSONObject.get(item))
							}

							// Display the relative path

							if (requiredPathType.get("relative")) {
								def relativePath = ""
								if (detailsJSONObject.get(item).length() > rootPath.length()) {
									relativePath = detailsJSONObject.get(item).substring(rootPath.length())
								} else {
									relativePath = ". <root path object> "
								}
								outputString += sprintf("%-${displayDepth}s-- %-${displayKeyValuePadding}s -> %s\n", "", "relative path", relativePath)
							}
						} else {
							outputString += sprintf("%-${displayDepth}s-- %-${displayKeyValuePadding}s -> %s\n", "", item, detailsJSONObject.get(item))
						}
					}
				}
			}

			outputString += "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n"

			keys = detailsJSONObject.keys()

			while( keys.hasNext() ) {
			    String key = (String) keys.next()
			    if (detailsJSONObject.get(key) instanceof JSONObject ) {
					if(checkRequiredProperties(key)) {
						outputString += sprintf("%-${displayDepth}s-- %-${displayKeyValuePadding}s\n", "", key)
						outputString += displayJSONObject(detailsJSONObject.get(key), displayDepth)
					}
			    } else if (detailsJSONObject.get(key) instanceof JSONArray ) {
					if(checkRequiredProperties(key)) {
						outputString += sprintf("%-${displayDepth}s-- %-${displayKeyValuePadding}s\n", "", key)
						outputString += displayJSONArray(detailsJSONObject.get(key), displayDepth)
					}
			    } else if (key == "resourceProperties") {
					if(checkRequiredProperties(key)) {
						outputString += sprintf("%-${displayDepth}s-- %-${displayKeyValuePadding}s\n", "", "resourceProperties")
						def resourcePropertiesSlurper = new JsonSlurper()
						JSONObject resourcePropertiesJSONObject = resourcePropertiesSlurper.parseText(detailsJSONObject.get(key).toString())

						outputString += displayJSONObject(resourcePropertiesJSONObject, displayDepth)
					}
				} else if (!displayOrder.contains(key)) {
					if (checkRequiredProperties(key)) {
						outputString += sprintf("%-${displayDepth}s-- %-${displayKeyValuePadding}s -> %s\n", "", key, detailsJSONObject.get(key))
					}
				}
			}
			outputString += "================================================================================\n"
		}
		return(outputString)
	}

	private String displayJSONObject(JSONObject object, Integer displayDepth) {
		def outputText = ""
		displayDepth += 2

		Iterator<?> keys = object.keys()

		if (keys.hasNext()) {
			while( keys.hasNext() ) {
				String key = (String) keys.next();
				if ( object.get(key) instanceof JSONObject ) {
					outputText += sprintf("%-${displayDepth}s-- %-${displayKeyValuePadding}s\n", "", key)
					outputText += displayJSONObject(object.get(key), displayDepth)
				} else if (object.get(key) instanceof JSONArray ) {
					outputText += sprintf("%-${displayDepth}s-- %-${displayKeyValuePadding}s\n", "", key)
					outputText += displayJSONArray(object.get(key), displayDepth)
				} else {
					outputText += sprintf("%-${displayDepth}s-- %-${displayKeyValuePadding}s -> %s\n", "", key, object.get(key))
				}
			}
			def padDepth = 80 - displayDepth
			outputText += sprintf("%-${displayDepth}s%${padDepth}.${padDepth}s\n", "", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
		}
		displayDepth -= 2
		return(outputText)
	}

	private String displayJSONArray(JSONArray array, Integer displayDepth) {
		def outputText = ""
		displayDepth += 2

		for (int i = 0; i < array.length(); i++) {
  			JSONObject arrayObject = array.getJSONObject(i)

			if (arrayObject instanceof JSONObject ) {
				outputText += displayJSONObject(arrayObject, displayDepth)
			} else if (arrayObject instanceof JSONArray ) {
				outputText += displayJSONArray(arrayObject, displayDepth)
			} else {
				outputText += "What's this ... ?\n"
			}
		}
		displayDepth -= 2
		return(outputText)
	}

	public int getNumberOfResources() { return Resources.size() }
	
	public int getNumberEmptyResourceProperties() { return emptyResourceProperties }
	public int getNumberEmptyResourceRoleProperties() { return emptyResourceRoleProperties }
	
	public String findEmptyProperties(String rootPath, String scope) {
		emptyResourceProperties = 0
		emptyResourceRoleProperties = 0
		def outputString = ""
		
		Resources.each { resourceItem ->
			def detailsSlurper = new JsonSlurper()
			def detailsJSONObject = resourceItem.getJSON()

			def missingRolePropValue = false
			def missingResourcePropValue = false
			def rolePropertiesFound = false
			def resourcePropertiesFound = false
			JSONObject roleProps = null
			JSONObject resourceProps = null

			Iterator<?> keys = detailsJSONObject.keys()
			while( keys.hasNext() ) {
				String key = (String) keys.next()
				if (key == "roleProperties") {
					rolePropertiesFound = true
				}
				if (key == "resourceProperties") {
					resourcePropertiesFound = true
				}
			}

			if ((scope == "role") || (scope == "all")) {
				if (rolePropertiesFound) {
					roleProps = detailsJSONObject.get("roleProperties")

					if (roleProps != null) {
						missingRolePropValue = false

						keys = roleProps.keys()

						if (keys.hasNext()) {
							while( keys.hasNext() ) {
								String key = (String) keys.next();

								if (roleProps.get(key).length() == 0) {
					    			missingRolePropValue = true
								}
							}
						}
					}
				}
			}
			if ((scope == "resource") || (scope == "all")) {
				if (resourcePropertiesFound) {
					resourceProps = detailsJSONObject.get("resourceProperties")

					if (resourceProps != null) {
						missingResourcePropValue = false

						keys = resourceProps.keys()

						if (keys.hasNext()) {
							while( keys.hasNext() ) {
								String key = (String) keys.next();
								if (key == "properties") {
									JSONArray resourcePropertiesArray = resourceProps.get(key)

									for (def resourcePropCounter = 0; resourcePropCounter < resourcePropertiesArray.length(); resourcePropCounter++) {

										JSONObject resourceProperties = resourcePropertiesArray.getJSONObject(resourcePropCounter)

										if (resourceProperties.get("value").length() == 0) {
											missingResourcePropValue = true
										}
									}
								}
							}
						}
					}
				}
			}
			if ((missingRolePropValue == true) || (missingResourcePropValue == true)) {
				outputString += "Resource name : " + detailsJSONObject.name + "\n"
				if (requiredPathType.get("full")) {
					outputString += "Resource path : " + detailsJSONObject.path + "\n"
				}
				if (requiredPathType.get("relative")) {
					if (detailsJSONObject.path.length() > rootPath.length()) {
						def relativePath = detailsJSONObject.path.substring(rootPath.length())
						outputString += "Resource path : " + relativePath + "\n"
					}
				}
			}
			if ((scope == "role") || (scope == "all")) {
				if (missingRolePropValue == true) {
					outputString += "Resource role properties with missing values .....\n" 

					keys = roleProps.keys()

					if (keys.hasNext()) {
						while( keys.hasNext() ) {
							String key = (String) keys.next();

							if (roleProps.get(key).length() == 0) {
								outputString += sprintf(" -- %s\n", key)
								emptyResourceRoleProperties++
							}
						}
					}
				}
			}
			if ((scope == "resource") || (scope == "all")) {
				if (missingResourcePropValue == true) {
					outputString += "Resource properties with missing values .....\n"

					resourceProps = detailsJSONObject.get("resourceProperties")

					if (resourceProps != null) {
						keys = resourceProps.keys()

						if (keys.hasNext()) {
							while( keys.hasNext() ) {
								String key = (String) keys.next();
								if (key == "properties") {
									JSONArray resourcePropertiesArray = resourceProps.get(key)

									for (def resourcePropCounter = 0; resourcePropCounter < resourcePropertiesArray.length(); resourcePropCounter++) {

										JSONObject resourceProperties = resourcePropertiesArray.getJSONObject(resourcePropCounter)

										if (resourceProperties.get("value").length() == 0) {
											outputString += sprintf(" -- %s\n", resourceProperties.get("name"))
											emptyResourceProperties++
										}
									}
								}
							}
						}
					}
				}
			}
			if ((missingRolePropValue == true) || (missingResourcePropValue == true)) {
				outputString += "-------------------------------------------------------------------------------\n"
			}
		}
		return(outputString)
	}


	public int getNumberFoundResourceProperties() { return foundResourceProperties }
	public int getNumberFoundResourceRoleProperties() { return foundResourceRoleProperties }
	

	public String searchForProperty(String rootPath, String propertyName) {
		foundResourceProperties = 0
		foundResourceRoleProperties = 0
		def outputString = ""
		
		Resources.each { resourceItem ->
			def detailsSlurper = new JsonSlurper()
			def detailsJSONObject = resourceItem.getJSON()

			def rolePropertiesFound = false
			def resourcePropertiesFound = false
			def foundRoleProp = false
			def foundResourceProp = false
			JSONObject roleProps = null
			JSONObject resourceProps = null

			Iterator<?> keys = detailsJSONObject.keys()
			while( keys.hasNext() ) {
				String key = (String) keys.next()
				if (key == "roleProperties") {
					rolePropertiesFound = true
				}
				if (key == "resourceProperties") {
					resourcePropertiesFound = true
				}
			}

			if (rolePropertiesFound) {
				roleProps = detailsJSONObject.get("roleProperties")

				if (roleProps != null) {
					foundRoleProp = false

					keys = roleProps.keys()

					if (keys.hasNext()) {
						while( keys.hasNext() ) {
							String key = (String) keys.next();

							if (key.contains(propertyName)) {
								foundRoleProp = true
							}
						}
					}
				}
			}

			if (resourcePropertiesFound) {
				resourceProps = detailsJSONObject.get("resourceProperties")

				if (resourceProps != null) {
					foundResourceProp = false

					keys = resourceProps.keys()

					if (keys.hasNext()) {
						while( keys.hasNext() ) {
							String key = (String) keys.next();
							if (key == "properties") {
								JSONArray resourcePropertiesArray = resourceProps.get(key)

								for (def resourcePropCounter = 0; resourcePropCounter < resourcePropertiesArray.length(); resourcePropCounter++) {

									JSONObject resourceProperties = resourcePropertiesArray.getJSONObject(resourcePropCounter)

									if (resourceProperties.get("name").contains(propertyName)) {
										foundResourceProp = true
									}
								}
							}
						}
					}
				}
			}

			if ((foundRoleProp == true) || (foundResourceProp == true)) {
				outputString += "Resource name : " + detailsJSONObject.name + "\n"
				if (requiredPathType.get("full")) {
					outputString += "Resource path : " + detailsJSONObject.path + "\n"
				}
				if (requiredPathType.get("relative")) {
					if (detailsJSONObject.path.length() > rootPath.length()) {
						def relativePath = detailsJSONObject.path.substring(rootPath.length())
						outputString += "Resource path : " + relativePath + "\n"
					}
				}
			}
			if (foundRoleProp == true) {
				outputString += "Resource role properties .....\n"
				keys = roleProps.keys()

				if (keys.hasNext()) {
					while( keys.hasNext() ) {
						String key = (String) keys.next();

						if (key.contains(propertyName)) {
							outputString += sprintf(" -- %-${displayKeyValuePadding}s -> %s\n", key, roleProps.get(key))
							foundResourceRoleProperties++
						}
					}
				}

			}
			if (foundResourceProp == true) {
				outputString += "Resource properties .....\n"
				resourceProps = detailsJSONObject.get("resourceProperties")

				if (resourceProps != null) {
					keys = resourceProps.keys()

					if (keys.hasNext()) {
						while( keys.hasNext() ) {
							String key = (String) keys.next();
							if (key == "properties") {
								JSONArray resourcePropertiesArray = resourceProps.get(key)

								for (def resourcePropCounter = 0; resourcePropCounter < resourcePropertiesArray.length(); resourcePropCounter++) {

									JSONObject resourceProperties = resourcePropertiesArray.getJSONObject(resourcePropCounter)

									if (resourceProperties.get("name").contains(propertyName)) {
										outputString += sprintf(" -- %-${displayKeyValuePadding}s -> %s\n", resourceProperties.get("name"), resourceProperties.get("value"))
										foundResourceProperties++
									}
								}
							}
						}
					}
				}
			}
			if ((foundRoleProp == true) || (foundResourceProp == true)) {
				outputString += "-------------------------------------------------------------------------------\n"
			}
		}
		return(outputString)
	}
}
