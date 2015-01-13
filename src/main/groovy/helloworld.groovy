import com.urbancode.air.*

CommandHelper cmdHelper = new CommandHelper(new File("."))
def command = ['echo', 'Hello  World']
cmdHelper.runCommand("Running echo", command)
