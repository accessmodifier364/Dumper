package me.accessmodifier364.dumper;

import me.accessmodifier364.dumper.transformer.AntiAntiDebug;
import me.accessmodifier364.dumper.transformer.Dumper;
import me.accessmodifier364.dumper.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/package-summary.html
 *
 * @author accessmodifier364
 * @since 11-Nov-2021
 */

public final class Main {

    public static Path directory = Paths.get(System.getProperty("user.home") + File.separator +
            "Desktop" + File.separator + "dump");

    public static void premain(final String args, final Instrumentation instrumentation) {
        System.out.println("Loading Dumper...");

        try {
            Files.createDirectories(Paths.get(directory + File.separator + "classes"));
        } catch (final IOException e) {
            System.err.println("Error creating the main directory: " + e.getMessage());
        }

        if (args != null && args.equals("bypass")) {
            System.out.println("Enabled anti-detection");
            instrumentation.addTransformer(new AntiAntiDebug());
        }
        instrumentation.addTransformer(new Dumper());
    }

    public static void main(final String[] args) throws IOException {
        System.out.println("Creating the Jar File...");
        FileUtil.zipFolder(directory, Paths.get(directory + File.separator + "dump.jar"));
    }
}

