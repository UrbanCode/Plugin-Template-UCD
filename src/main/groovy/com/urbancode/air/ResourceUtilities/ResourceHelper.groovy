/*
 * Licensed Materials - Property of IBM Corp.
 * IBM UrbanCode Deploy
 * (c) Copyright IBM Corporation 2011, 2014. All Rights Reserved.
 *
 * U.S. Government Users Restricted Rights - Use, duplication or disclosure restricted by
 * GSA ADP Schedule Contract with IBM Corp.
 */
 package com.urbancode.air.ResourceUtilities
 
 import java.util.UUID
 import java.util.List
 import java.util.Map
 import java.util.HashMap
 import java.util.regex.Pattern
 import java.util.Properties
 
 import org.codehaus.jettison.json.JSONObject
 import org.codehaus.jettison.json.JSONArray
 
 import com.urbancode.air.AirPluginTool
 import com.urbancode.ud.client.AgentClient
 import com.urbancode.ud.client.ResourceClient
 import com.urbancode.ud.client.EnvironmentClient
 import com.urbancode.commons.util.FileFilterToRegex
 
 import com.urbancode.air.Resources
 import com.urbancode.air.UCDServerConnection
 import com.urbancode.air.UCDResource
 import com.urbancode.air.ResourceClientConnection
 import com.urbancode.air.httpRequestProcess

 import org.apache.http.HttpResponse
 import org.apache.http.client.methods.HttpGet
 import org.apache.http.impl.client.DefaultHttpClient
 
 import com.urbancode.commons.httpcomponentsutil.HttpClientBuilder
 
 public class ResourceHelper {
	 def apTool
	 def props = []
	 def udUser
	 def udPass
	 def weburl
	 UCDServerConnection serverConnection
 
	 public ResourceHelper(def apToolIn) {
		 apTool = apToolIn
		 props = apTool.getStepProperties()
		 udUser = apTool.getAuthTokenUsername()
		 udPass = apTool.getAuthToken()
		 weburl = System.getenv("AH_WEB_URL")
		 com.urbancode.air.XTrustProvider.install()
	
		 // serverConnection = new UCDServerConnection(weburl, udUser, udPass)
		 serverConnection = new UCDServerConnection("https://rational-srv-01:8443", "admin", "admin")
	 }
 
	 private static List<Pattern> getGlobPatternsFromMultiline(String multiline) {
		 return multiline.split("\n")
				 .findAll({ it.trim().length() > 0 })
				 .collect({ FileFilterToRegex.convert(it) })
	 }
	 
	 def listResources() {
		 def rootPath = props['resourceRoot']
		 def nameFilter = props['nameFilter']
		 def pathType = props['pathType']
		 def outputContent_Properties = props['outputContentProperties']
		 def outputContent_Security = props['outputContentSecurity']
		 def outputContent_FurtherDetails = props['outputContentFurtherDetails']
		 def outputFileName = props['outputFileName']
		 def outputResultToWindow = props['outputResultToWindow']
 
		 Resources resources = new Resources(serverConnection)
		 def httpProcess = new httpRequestProcess(serverConnection, true)
		 
		 resources.getResources(rootPath, httpProcess, nameFilter)
		 
		 if ((outputFileName == "") && (outputResultToWindow == "false")) {
			 println("ERROR : No output option specified")
			 println("Please enter a filename to hold the output, or select the option to display the result in the output window or in an output property.")
		 } else {	 
			 println("=======================================================")
			 resources.setRequiredPropertiesAndPathType(outputContent_Properties, outputContent_Security, outputContent_FurtherDetails, pathType)
			 def outputData = resources.ListResources(rootPath)
			 
			 resources.getNumberOfResources()
			 
			 def numberResources = resources.getNumberOfResources()
			 
			 if (outputFileName != "") {
				 
				 def filename = outputFileName
				 def file = new File(filename)
				 def w = file.newWriter()
	
				 w << outputData
				 w.close()
				 apTool.setOutputProperty("OutputFile", outputFileName)
			 }
			 if (outputResultToWindow == "true") {
				 println("Number resources found : " + numberResources)
				 print(outputData)
			 }
			 
			 apTool.setOutputProperty("NumberResources", numberResources.toString())
			  
			 apTool.setOutputProperties()
		 }
	 }
	 def findEmptyProperties() {
		 def rootPath = props['resourceRoot']
		 def nameFilter = props['nameFilter']
		 def pathType = props['pathType']
		 def emptyResourceProperties = props['findEmptyResourceProperties']
		 def emptyResourceRoleProperties = props['findEmptyResourceRoleProperties']
		 def outputFileName = props['outputFileName']
		 def outputResultToWindow = props['outputResultToWindow']
		 
		 def missingScope = ""
		 
		 Resources resources = new Resources(serverConnection)
		 def httpProcess = new httpRequestProcess(serverConnection, true)
		 
		 resources.getResources(rootPath, httpProcess, nameFilter)
		 
		 if ((outputFileName == "") && (outputResultToWindow == "false")) {
			 println("ERROR : No output option specified")
			 println("Please enter a filename to hold the output, or select the option to display the result in the output window or in an output property.")
		 } else {
			 println("=======================================================")
			 resources.setRequiredPropertiesAndPathType("false", "false", "false", pathType)	 
			  
			 if ((emptyResourceProperties == "true") && (emptyResourceRoleProperties == "true")) {
				 missingScope = "all"
			 } else if ((emptyResourceProperties == "true") && (emptyResourceRoleProperties == "false")) {
			 	missingScope = "resource"
			 } else if ((emptyResourceProperties == "false") && (emptyResourceRoleProperties == "true")) {
			 	missingScope = "role"
			 } 
			 		 
			 def outputData = resources.findEmptyProperties(rootPath, missingScope)
			 
			 def emptyResProps = resources.getNumberEmptyResourceProperties()
			 def emptyResRoleProps = resources.getNumberEmptyResourceRoleProperties()
			 
			 if (outputResultToWindow == "true") {
				 println("Empty resource properties      : " + emptyResProps)
				 println("Empty resource role properties : " + emptyResRoleProps)
				 print(outputData)
			 }
			 
			 apTool.setOutputProperty("EmptyResProps", emptyResProps.toString())
			 apTool.setOutputProperty("EmptyResRoleProps", emptyResRoleProps.toString())
			 
			 if (outputFileName != "") {
				 
				 def filename = outputFileName
				 def file = new File(filename)
				 def w = file.newWriter()
	
				 w << outputData
				 w.close()
				 apTool.setOutputProperty("OutputFile", outputFileName)
			 }
			  
			 apTool.setOutputProperties()
		 }
	 }
	 def searchProperties() {
		 def rootPath = props['resourceRoot']
		 def nameFilter = props['nameFilter']
		 def pathType = props['pathType']
		 def searchTerm = props['searchTerm']
		 def outputFileName = props['outputFileName']
		 def outputResultToWindow = props['outputResultToWindow']
		 		 
		 Resources resources = new Resources(serverConnection)
		 def httpProcess = new httpRequestProcess(serverConnection, true)
		 
		 resources.getResources(rootPath, httpProcess, nameFilter)
		 
		 if ((outputFileName == "") && (outputResultToWindow == "false")) {
			 println("ERROR : No output option specified")
			 println("Please enter a filename to hold the output, or select the option to display the result in the output window or in an output property.")
		 } else {
			 println("=======================================================")
			 resources.setRequiredPropertiesAndPathType("false", "false", "false", pathType)
			  				  
			 def outputData = resources.searchForProperty(rootPath, searchTerm)
			 
			 def foundResProps = resources.getNumberFoundResourceProperties()
			 def foundResRoleProps = resources.getNumberFoundResourceRoleProperties()
			 
			 if (outputResultToWindow == "true") {
			 
				 println("Found resource properties      : " + foundResProps)
				 println("Found resource role properties : " + foundResRoleProps)
				 print(outputData)
			 }
			 	 
			 apTool.setOutputProperty("FoundResProps", foundResProps.toString())
			 apTool.setOutputProperty("FoundResRoleProps", foundResRoleProps.toString())
			 
			 if (outputFileName != "") {
				 
				 def filename = outputFileName
				 def file = new File(filename)
				 def w = file.newWriter()
	
				 w << outputData
				 w.close()
				 apTool.setOutputProperty("OutputFile", outputFileName)
			 }
			 apTool.setOutputProperties()
		 }		 
	 }
 } 
