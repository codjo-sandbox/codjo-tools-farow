package net.codjo.tools.farow.command;
import java.util.ArrayList;
import java.util.List;
import net.codjo.tools.farow.Display;
/**
 *
 */
public class CommandPlayer {
    private List<Command> commands = new ArrayList<Command>();
    private Command lastFailedCommand;


    public boolean add(Command command) {
        return commands.add(command);
    }


    public void run(Display display) throws Exception {
        int index = 0;
        if (lastFailedCommand != null) {
            index = commands.indexOf(lastFailedCommand) + 1;
            lastFailedCommand.setBuildFailure(false);
            lastFailedCommand.reExecute(display);
            lastFailedCommand = null;
        }
        for (; index < commands.size(); index++) {
            Command command = commands.get(index);
            try {
                display.line("############################################################################");
                display.line("### ");
                display.line("##    " + command.getDisplayLabel());
                display.line("#");
                display.information(command.getDisplayLabel());
                command.execute(display);
                display.information("");
            }
            catch (Exception e) {
                lastFailedCommand = command;
                throw e;
            }
        }
    }
}
