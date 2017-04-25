/* This is an example step groovy to show the proper use of APTool
 * In order to use import these utilities, you have to use the 'groovy-plugin-utils-1.1.jar'
 * that is pulled down from Maven Central. 
 */

import com.urbancode.air.AirPluginTool;
import com.urbancode.air.CommandHelper;

/* This gets us the plugin tool helper. 
 * This assumes that args[0] is input props file and args[1] is output props file.
 * By default, this is true. If your plugin.xml looks like the example. 
 * Any arguments you wish to pass from the plugin.xml to this script that you don't want to 
 * pass through the step properties can be accessed using this argument syntax
 */
final airTool = new AirPluginTool(args[0], args[1])

/* Here we call getStepProperties() to get a Properties object that contains the step properties
 * provided by the user. 
 */
final def props = airTool.getStepProperties()

/* This is how you retrieve properties from the object. You provide the "name" attribute of the 
 * <property> element. In this example, if property "name" is null or empty, it will default
 * to the the String "World"
 */
final def name = props['name'] ?: "World"

// Create a list of strings which make up a command for the command line
def command = ['echo']
def message = "Hello ${name}!"
command << message          // command == ["echo", "Hello World!"]

//Example of CommandHelper which helps construct and run a specified command in the Agent's terminal.
CommandHelper cmdHelper = new CommandHelper(new File("."))
cmdHelper.runCommand("Running echo", command)

//Set an output property
airTool.setOutputProperty("outPropName", "outPropValue");
airTool.storeOutputProperties();//write the output properties to the file