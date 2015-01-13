import com.urbancode.air.*

final airTool = new AirPluginTool(args[0], args[1])
final def props = airTool.getStepProperties()

final def name = props['name'] ?: "World"

CommandHelper cmdHelper = new CommandHelper(new File("."))
def command = ['echo']
def message = "Hello ${name}!"
command << message

cmdHelper.runCommand("Running echo", command)
