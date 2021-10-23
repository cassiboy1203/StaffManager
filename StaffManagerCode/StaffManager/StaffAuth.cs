using System.Collections.Generic;
using Oxide.Core.Plugins;

namespace Oxide.Plugins
{

    [Info("Staff auth", "Cas", "0.0.1")]
    [Description("Simple staff authentication, using Staff manager")]
    class StaffAuth : RustPlugin
    {

        private const string staffPermission = "staffauth.staff";

        private PluginConfig config;

        #region Plugin References

        [PluginReference] private Plugin StaffManagerCore;
        [PluginReference] private Plugin StaffCover;
        [PluginReference] private Plugin Vanish;
        [PluginReference] private Plugin AdminRadar;
        [PluginReference] private Plugin GodMode;
        [PluginReference] private Plugin RemoverTool;

        #endregion

        #region Configuration

        private class PluginConfig
        {
            public bool HideStaffName;
            public bool HideStaffId;
            public bool changeAuthLevel;
        }

        #endregion

        #region Data

        private class AuthData
        {
            public Dictionary<string, PlayerInfo> players { get; set; } = new Dictionary<string, PlayerInfo>();
        }

        private class PlayerInfo
        {
            public string steamId;
            public string steamName;
            public bool isAuth;
            public byte AuthLevel;
            public List<string> Groups;
        }

        #endregion

        #region Commands

        [Command("staff")]
        private void AuthCmd(BasePlayer player, string command, string[] args)
        {
            if (!player.IPlayer.HasPermission(staffPermission))
            {
                player.IPlayer.Reply("No permission");
                return;
            }

            if (args.Length < 1)
            {
                player.IPlayer.Reply("Invalid args. use: /staff [authKey]");
                return;
            }

            Auth(player, args[0]);
        }

        #endregion

        #region Api

        private void Auth(BasePlayer player, string authKey)
        {
            //TODO: check auth

            if (config.HideStaffId)
            {
                //TODO: get fake staff id
                var fakeStaffId = "76561198163626210";
                StaffCover.Call("FakeId", player, fakeStaffId);
            }

            if (config.HideStaffName)
            {
                //TODO: get fake staff name
                var fakeStaffName = "staff";
                StaffCover.Call("Rename", player, fakeStaffName);
            }

            if (config.changeAuthLevel)
            {
                
            }
        }

        private void RemovePerms(BasePlayer player)
        {

        }

        #endregion

        #region Hooks

        

        #endregion
    }
}
