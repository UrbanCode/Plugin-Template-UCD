# IBM UrbanCode Deploy Jenkins Job Manager Plug-in
---
Note: This is not the plug-in distributable! This is the source code. 

This IBM UrbanCode Deploy Jenkins Job manager plug-in provide support to magae Jenkins Job.

### License
This plug-in is protected under the [Eclipse Public 1.0 License](http://www.eclipse.org/legal/epl-v10.html)

### The Jenkins Job Manager plug-in includes the following steps:
    - Start Jenkins Job
    - Enabled Jenkins Job
    - Disable Jenkins Job

### Compatibility
	This plug-in requires version 6.0.1.0 or later of IBM UrbanCode Deploy.

### Installation
	The packaged zip is hosted on GitHub https://github.com/vikrantsnirban/Plugin-Template-UCD/releases/latest
    No special steps are required for installation. 
	See Installing plug-ins in IBM UrbanCode Deploy.
    Download this zip file if you wish to skip the manual build step.

## Usage 
	This plugin can be used in indedepent process and component Process.
	Ref Site for complete usage: http://versatile-solutions.blogspot.in/2016/10/urban-code-deploy-with-jenkins.html
	

### Build
    gradle

To compile the project locally, two additional jar files are required: jenkins-cli.jar and groovy-plugin-utils-1.0.jar. Place these jars in JenkinsJobManager-UCD project's 'libs' folder located in the root directory. 

### History
	Version 1
		Initial GitHub Release.
