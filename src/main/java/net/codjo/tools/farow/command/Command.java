package net.codjo.tools.farow.command;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.codjo.tools.farow.Display;
/**
 *
 */
public abstract class Command {
    private String displayLabel;
    protected final ArtifactType artifactType;
    protected final String name;
    protected boolean buildFailure = false;


    protected Command(String displayLabel) {
        this(displayLabel, null, null);
    }


    protected Command(String displayLabel, ArtifactType artifactType, String libName) {
        this.displayLabel = displayLabel;
        this.artifactType = artifactType;
        this.name = libName;
    }


    public abstract void execute(Display display) throws Exception;


    public void reExecute(Display display) throws Exception {
        execute(display);
    }


    public String getDisplayLabel() {
        return displayLabel;
    }


    protected void setBuildFailure(boolean buildFailure) {
        this.buildFailure = buildFailure;
    }


    protected void flushStream(InputStream is, Display display) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        //noinspection NestedAssignment
        while ((line = br.readLine()) != null) {
            display.line(line);
            processLine(line);
        }
    }


    protected void processLine(String line) {
    }


    protected String toArtifactPath() {
        return artifactType.toArtifactPath(name);
    }


    protected void processResult(Display display, Process process) throws IOException {
        flushStream(process.getInputStream(), display);
        flushStream(process.getErrorStream(), display);

        int processResult = process.exitValue();
        if (processResult != 0) {
            process.destroy();
            throw new IOException("Commande en echec ! (" + processResult + ")");
        }
    }


    protected Process createProcess(Display display, String... args) throws IOException {
        List<String> cli = new ArrayList<String>(args.length + 2);
        cli.add("cmd");
        cli.add("/c");
        cli.addAll(Arrays.asList(args));

        display.line("Running " + cli);

        return new ProcessBuilder(cli).redirectErrorStream(true).start();
    }


    protected void executeIt(Display display, String... commands) throws IOException {
        processResult(display, createProcess(display, commands));
    }


    protected void executeItInteractif(Display display, String[] commands, String answer) throws IOException {
        Process process = createProcess(display, commands);

        OutputStream outputStream = process.getOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        printStream.println(answer);
        printStream.flush();

        processResult(display, process);
    }
}
