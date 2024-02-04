package AutoPause;

import arc.Events;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.mod.Plugin;
import mindustry.game.EventType;

public class AutoPausePlugin extends Plugin {
    private final boolean enabled = true;
    private int player_count = 0;

    @Override
    public void init() {
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
    }
}