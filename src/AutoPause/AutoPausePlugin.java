package AutoPause;

import arc.Events;
import arc.util.CommandHandler;
import arc.util.Log;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.gen.Player;
import mindustry.mod.Plugin;
import mindustry.game.EventType;

public class AutoPausePlugin extends Plugin {
    private boolean enabled = true;
    private int player_count = 0;

    @Override
    public void init() {
        Log.info("[I][AutoPause] AutoPause initialization started!!");
        Vars.state.set(GameState.State.paused);

        Events.on(EventType.WorldLoadEndEvent.class, event -> {
            if (enabled) {
                Vars.state.set(GameState.State.paused);
            }
        });
        Events.on(EventType.PlayerJoin.class, event -> {
            if (enabled) {
                player_count += 1;
                if (Vars.state.isPaused()) {
                    Vars.state.set(GameState.State.playing);
                }
            }
        });
        Events.on(EventType.PlayerLeave.class, event -> {
            if (enabled) {
                player_count -= 1;
                if (player_count == 0) {
                    Vars.state.set(GameState.State.paused);
                }
            }
        });

        Log.info("[I][AutoPause] AutoPause initialization completed!!");
    }

    @Override
    public void registerServerCommands(CommandHandler handler) {
        handler.register(
                "autopause",
                "<on/off>",
                "Enabled/disable autopause", arg ->{
            if (arg.length == 0) {
                Log.info("[AutoPause] Error: Second parameter must be either 'on' or 'off'");
            }

            if (!(arg[0].equals("on") || arg[0].equals("off"))) {
                Log.info("[AutoPause] Error: Second parameter must be either 'on' or 'off'");

            }

            if (arg[0].equals("off")) {
                enabled = false;
                Log.info("[I][AutoPause] AutoPause off");
            }

            if (arg[0].equals("on")) {
                enabled = true;
                Log.info("[I][AutoPause] AutoPause on");
            }
        });
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        handler.<Player>register(
                "autopause",
                "<on/off>",
                "Enabled/disable autopause", (arg, player) -> {
                    if (arg.length == 0) {
                        player.sendMessage("Error: Second parameter must be either 'on' or 'off'");
                    }

                    if (!(arg[0].equals("on") || arg[0].equals("off"))) {
                        player.sendMessage("Error: Second parameter must be either 'on' or 'off'");

                    }

                    if (arg[0].equals("off")) {
                        enabled = false;
                        Log.info("[I][AutoPause] AutoPause off");
                        player.sendMessage("AutoPause off");
                    }

                    if (arg[0].equals("on")) {
                        enabled = true;
                        Log.info("[I][AutoPause] AutoPause on");
                        player.sendMessage("AutoPause on");
                    }
                });
    }
}