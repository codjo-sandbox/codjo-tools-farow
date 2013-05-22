package net.codjo.tools.farow.step;
import net.codjo.tools.farow.Display;
import net.codjo.tools.farow.command.CommandPlayer;
/**
 *
 */
public class Step {
    protected String name;
    private State state = State.TODO;
    protected CommandPlayer player;

    public static enum State {
        TODO,
        RUNNING,
        FAILURE,
        DONE,
        TO_BE_PUBLISHED,
        TO_BE_DELETED
    }


    protected Step(String name) {
        this(name, null);
    }


    public Step(String name, CommandPlayer player) {
        this.name = name;
        this.player = player;
    }


    public State getState() {
        return state;
    }


    @Override
    public String toString() {
        return name;
    }


    public void run(Display display) throws Exception {
        state = State.RUNNING;
        try {
            player.run(display);
        }
        finally {
            state = State.FAILURE;
        }
        state = getFinishingState();
    }

    public String getName() {
        return name;
    }


    public void setStateToRunning() {
        state = State.RUNNING;
    }

    protected State getFinishingState() {
        return State.DONE;
    }
}
