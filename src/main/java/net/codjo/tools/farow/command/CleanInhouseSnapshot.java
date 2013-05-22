package net.codjo.tools.farow.command;

import net.codjo.tools.farow.Display;
public class CleanInhouseSnapshot extends Command {
    public CleanInhouseSnapshot() {
        super("Nettoyage du repo snapshot");
    }


    @Override
    public void execute(Display display) throws Exception {
        executeIt(display, "Z:\\maven\\bin\\clean-inhouse-snapshot.bat");
    }
}