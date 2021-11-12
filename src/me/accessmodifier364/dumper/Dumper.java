package me.accessmodifier364.dumper;

import me.accessmodifier364.dumper.transformer.Transformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://docs.oracle.com/javase/9/docs/api/java/lang/instrument/package-summary.html
 *
 * @author accessmodifier364
 * @since 11-Nov-2021
 */

public class Dumper {

    public static String directory = System.getenv("USERPROFILE") + "\\Desktop\\dump\\";

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Loading Dumper...");

        try {
            Files.createDirectories(Paths.get(directory + "classes"));
        } catch (IOException e) {
            System.out.println("Error creating the main directory: " + e.getMessage());
        }

        inst.addTransformer(new Transformer());
    }

    public static void main(String[] args) throws IOException { //Put the dumped classes into a jar file.
        System.out.println("Creating the Jar File...");
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(directory + "dump.jar"));

        for (File f : getFilesInDirectory(directory)) {
            if (!f.getName().endsWith(".class")) continue;
            String newName = f.toPath().toString().replace(directory + "classes\\", "");
            System.out.println("Adding class " + newName);
            JarEntry jarEntry = new JarEntry(newName);
            jarOutputStream.putNextEntry(jarEntry);
            jarOutputStream.write(Files.readAllBytes(f.toPath()));
            jarOutputStream.closeEntry();
        }

        jarOutputStream.close();
    }

    private static List<File> getFilesInDirectory(String dir) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(dir))) {
            return paths.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        }
    }

}

