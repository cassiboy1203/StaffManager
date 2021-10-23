using Oxide.Core.Plugins;

namespace Oxide.Plugins
{
    [Info("Staff manager core", "Cas", "0.0.1")]
    [Description("Simple staff management system, using a website")]
    public class StaffManagerCore : RustPlugin
    {
        #region Plugin References
        
        [PluginReference] private Plugin BetterChat;
        [PluginReference] private Plugin Vanish;
        [PluginReference] private Plugin AdminRadar;
        [PluginReference] private Plugin GodMode;
        [PluginReference] private Plugin RemoverTool;

        #endregion

        #region Configuration



        #endregion

        #region Data

        

        #endregion

        #region Commands

        #endregion

        #region Hooks

        #endregion
    }
}
