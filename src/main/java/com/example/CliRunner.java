package com.example;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.commands.ExitCommand;
import com.example.commands.RootCommand;
import com.example.factories.SpringFactory;

import picocli.CommandLine;
import picocli.shell.jline3.PicocliJLineCompleter;

@Component
public class CliRunner implements CommandLineRunner {
    private final RootCommand rootCommand;
    private final SpringFactory springFactory;

    public CliRunner(RootCommand rootCommand, SpringFactory springFactory) {
        this.rootCommand = rootCommand;
        this.springFactory = springFactory;
    }

    @Override
    public void run(String... args) throws Exception {
        CommandLine cmd = new CommandLine(rootCommand, springFactory);

        // If arguments are provided just execute them and exit
        if (args.length > 0) {
            cmd.execute(args);
            return;
        }

        // Custom JLine terminal to allow interactive shell with tab completion
        Terminal terminal = TerminalBuilder.builder().system(true).build();

        PicocliJLineCompleter picoliJLineCompleter = new PicocliJLineCompleter(cmd.getCommandSpec());

        // Custom line reader to allow tab completion
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(picoliJLineCompleter)
                .parser(new DefaultParser())
                .build();

        System.out.println("Type 'exit' to terminate the application.");
        while (true) {
            String line;

            try {
                line = lineReader.readLine("> ");
            } catch (UserInterruptException | EndOfFileException e) {
                break;
            }

            if (line.trim().isEmpty())
                continue;

            String[] tokens = line.split("\\s+");
            cmd.execute(tokens);

            if (ExitCommand.exitRequested) {
                break;
            }
        }

        System.out.println("Goodbye!");
    }
}
