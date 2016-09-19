import com.urbancode.air.AirPluginTool
import com.urbancode.air.ResourceUtilities.ResourceHelper

ResourceHelper helper = new ResourceHelper(new AirPluginTool(this.args[0], this.args[1]))

helper.listResources()