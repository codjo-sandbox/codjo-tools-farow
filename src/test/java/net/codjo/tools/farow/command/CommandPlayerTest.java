package net.codjo.tools.farow.command;
import java.io.IOException;
import junit.framework.TestCase;
/**
 *
 */
public class CommandPlayerTest extends TestCase {
    public void test_run() throws Exception {
        CommandPlayer player = new CommandPlayer();
        player.add(new CommandMock());
        player.run(new DisplayMock());
    }


    public void test_run_twice() throws Exception {
        CommandPlayer player = new CommandPlayer();
        player.add(new CommandMock());
        player.run(new DisplayMock());
        player.run(new DisplayMock());
    }


    public void test_run_withFailure() throws Exception {
        CommandPlayer player = new CommandPlayer();
        CommandMock command = new CommandMock();
        command.setShouldFaild(true);
        player.add(command);
        try {
            player.run(new DisplayMock());
            fail();
        }
        catch (Exception ex) {
            ; // Ok
        }
    }


    public void test_run_withFailure_twice() throws Exception {
        CommandPlayer player = new CommandPlayer();
        CommandMock command = new CommandMock();
        command.setShouldFaild(true);
        player.add(command);
        try {
            player.run(new DisplayMock());
            fail();
        }
        catch (Exception ex) {
            ; // Ok
        }

        command.setShouldFaild(false);
        player.run(new DisplayMock());
    }


    public static class DisplayMock implements Display {
        public void line(String line) {
        }


        public void information(String line) {
        }
    }

    public static class CommandMock extends Command {
        private boolean shouldFaild;


        protected CommandMock() {
            super("command mock", ArtifactType.LIB, "lib");
        }


        public void setShouldFaild(boolean shouldFaild) {
            this.shouldFaild = shouldFaild;
        }


        @Override
        public void execute(Display display) throws Exception {
            if (shouldFaild) {
                setBuildFailure(true);
                throw new IOException("en erreur ");
            }
        }
    }
}
