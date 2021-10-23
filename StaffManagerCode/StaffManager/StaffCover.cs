using System.Collections.Generic;
using System.Linq;
using ConVar;
using Oxide.Core;
using Oxide.Core.Configuration;
using Oxide.Core.Libraries.Covalence;
using Oxide.Core.Plugins;

namespace Oxide.Plugins
{
    [Info("Staff Cover", "Cas", "0.0.1")]
    [Description("Simple staff renamer")]
    class StaffCover : RustPlugin
    {
        #region Plugin References

        [PluginReference] private Plugin BetterChat;

        #endregion

        #region Data
        private StaffCoverData _data;
        private DynamicConfigFile _dataFile;

        private void LoadData()
        {
            try
            {
                _data = _dataFile.ReadObject<StaffCoverData>();
                Puts("Data File Loaded");
            }
            catch
            {
                Puts("Couldn't load staff cover e data, creating new datafile");
                _data = new StaffCoverData();
                SaveData();
            }
        }

        private void SaveData()
        {
            _dataFile.WriteObject(_data);
        }

        private class StaffCoverData
        {
            public Dictionary<string, PlayerInfo> players { get; set; } = new Dictionary<string, PlayerInfo>();
        }
        private class PlayerInfo
        {
            public string Id { get; set; }
            public string Name { get; set; }
            public string CurrentName { get; set; }
            public bool isRenamed { get; set; }
            public string fakeId { get; set; }
            public bool UsingFakeId { get; set; }
        }

        #endregion

        #region Commands

        [ChatCommand("rename")]
        private void CmdRename(BasePlayer player, string command, string[] args)
        {
            if (!player.IPlayer.HasPermission(RenamePermission))
            {
                player.IPlayer.Reply("No permission.");
                return;
            }

            if (args.Length < 1)
            {
                player.IPlayer.Reply("Invalid args. use: /rename [new name]");
                return;
            }

            Rename(player, args[0]);
        }

        [ChatCommand("fakeid")]
        private void CmdFakeId(BasePlayer player, string command, string[] args)
        {
            if (!player.IPlayer.HasPermission(FakeIdPermission))
            {
                player.IPlayer.Reply("No permission.");
                return;
            }

            if (args.Length < 1)
            {
                player.IPlayer.Reply("Invalid args. use: /rename [new steam64 id]");
                return;
            }

            FakeId(player, args[0]);
        }

        [ChatCommand("resetname")]
        private void CmdResetName(BasePlayer player, string command, string[] args)
        {
            if (!player.IPlayer.HasPermission(RenamePermission) && !player.IPlayer.HasPermission(FakeIdPermission))
            {
                player.IPlayer.Reply("No permission.");
                return;
            }

            ResetName(player);
        }

        #endregion

        #region Api

        private bool IsRenamed(BasePlayer player)
        {
            if (_data.players.ContainsKey(player.IPlayer.Id))
            {
                return _data.players[player.IPlayer.Id].isRenamed;
            }

            return false;
        }

        private bool UsingFakeId(BasePlayer player)
        {
            if (_data.players.ContainsKey(player.IPlayer.Id))
            {
                return _data.players[player.IPlayer.Id].UsingFakeId;
            }

            return false;
        }

        private void ResetName(BasePlayer player)
        {
            object result = Interface.Oxide.CallHook("OnResetName", player);
            if (result != null)
            {
                return;
            }

            if (!IsRenamed(player) && !UsingFakeId(player))
            {
                player.IPlayer.Reply("You are not renamed.");
                return;
            }

            _data.players[player.IPlayer.Id].CurrentName = _data.players[player.IPlayer.Id].Name;
            _data.players[player.IPlayer.Id].isRenamed = false;
            _data.players[player.IPlayer.Id].UsingFakeId = false;

            SaveData();

            player.IPlayer.Rename(_data.players[player.IPlayer.Id].Name);
            
            player.IPlayer.Reply("You name and id has been reset.");
        }

        private void Rename(BasePlayer player, string newName)
        {
            object result = Interface.Oxide.CallHook("OnRename", player);
            if (result != null)
            {
                return;
            }

            if (!_data.players.ContainsKey(player.IPlayer.Id))
            {
                PlayerInfo playerInfo = new PlayerInfo();
                playerInfo.Name = player.IPlayer.Name;
                playerInfo.Id = player.IPlayer.Id;
                
                _data.players.Add(player.IPlayer.Id, playerInfo);
            }

            _data.players[player.IPlayer.Id].CurrentName = newName;
            _data.players[player.IPlayer.Id].isRenamed = true;

            player.IPlayer.Rename(newName);

            player.IPlayer.Reply("Your name has been changed to: " + newName);
        }

        private void FakeId(BasePlayer player, string newSteamId)
        {
            object result = Interface.Oxide.CallHook("OnFakeId", player);
            if (result != null)
            {
                return;
            }

            if (!_data.players.ContainsKey(player.IPlayer.Id))
            {
                PlayerInfo playerInfo = new PlayerInfo();
                playerInfo.Name = player.IPlayer.Name;
                playerInfo.Id = player.IPlayer.Id;
                
                _data.players.Add(player.IPlayer.Id, playerInfo);
            }

            _data.players[player.IPlayer.Id].fakeId = newSteamId;
            _data.players[player.IPlayer.Id].UsingFakeId = true;

            player.IPlayer.Reply("Your steam id has been changed to: " + newSteamId);
        }

        #endregion

        #region Hooks
        private void Loaded()
        {
            if (BetterChat == null)
            {
                Puts("BetterChat is not loaded, get it at https://umod.org/plugins/better-chat");
            }

            _dataFile = Interface.Oxide.DataFileSystem.GetFile("StaffCover");
            LoadData();
            RegisterPermissions();
        }

        private object OnRename(BasePlayer player, string newName)
        {
            return null;
        }

        private object OnFakeId(BasePlayer player, string newId)
        {
            return null;
        }

        private object OnResetName(BasePlayer player)
        {
            return null;
        }

        private object OnUserChat(IPlayer player, string message)
        {
            if (!plugins.Exists(nameof(BetterChat)))
                return null;
            bool staffModeOn = false;
            if (_data.players.ContainsKey(player.Id))
            {
                staffModeOn = _data.players[player.Id].UsingFakeId;
            }
            return staffModeOn ? true : (object) null;
        }

        private object OnBetterChat(Dictionary<string, object> data)
        {
            object channel;
            if (!data.TryGetValue("ChatChannel", out channel) || !(channel is Chat.ChatChannel))
                return null;

            var chatChannel = (Chat.ChatChannel)channel;

            object player;
            if (!data.TryGetValue("Player", out player) || !(player is IPlayer))
                return null;

            var iPlayer = (IPlayer) player;

            object message;
            if (!data.TryGetValue("Message", out message) || message == null)
                return null;
            var formattedMessage = message.ToString();

            object username;
            if (!data.TryGetValue("UsernameSettings", out username) || !(username is Dictionary<string, object>))
                return null;
            var usernameSetting = (Dictionary<string, object>)username;

            object color;
            var usernameColor = "#55aaff";
            if (usernameSetting.TryGetValue("Color", out color) && color != null)
                usernameColor = color.ToString();

            var chatMessage = BetterChat?.Call("API_GetFormattedMessage", player, formattedMessage)?.ToString() ?? "";
            var consoleMessage = BetterChat?.Call("API_GetFormattedMessage", player, formattedMessage, true)?.ToString() ?? "";

            if (_data.players.ContainsKey(iPlayer.Id))
            {
                if (_data.players[iPlayer.Id].UsingFakeId)

                {
                    object blocked;
                    var blockedIds = new List<string>();
                    if (data.TryGetValue("BlockedReceivers", out blocked) && blocked is List<string>)
                        blockedIds = (List<string>)blocked;

                    foreach (BasePlayer p in BasePlayer.activePlayerList.Where(p => !blockedIds.Contains(p.UserIDString)))
                        p.SendConsoleCommand("chat.add", new object[] { (int)chatChannel, _data.players[iPlayer.Id].fakeId, chatMessage });

                    return true;   
                }
            }

            return null;
        }

        #endregion

        #region Permissions

        private const string RenamePermission = "staffcover.rename";
        private const string FakeIdPermission = "staffcover.fakeid";

        private void RegisterPermissions()
        {
            permission.RegisterPermission(RenamePermission, this);
            permission.RegisterPermission(FakeIdPermission, this);
        }

        #endregion
    }
}
