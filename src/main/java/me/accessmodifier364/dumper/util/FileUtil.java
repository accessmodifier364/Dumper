package me.accessmodifier364.dumper.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author accessmodifier364
 * @since 9/24/2023 at 16:06
 */

public final class FileUtil {

    public static List<File> getFilesInDirectory(final Path directory) throws IOException {

        try (final Stream<Path> paths = Files.walk(directory)) {

            return paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        }
    }

    public static void zipFolder(final Path inputFolder, final Path outputZip) throws IOException {

        try (final JarOutputStream jos = new JarOutputStream(Files.newOutputStream(outputZip))) {

            FileUtil.getFilesInDirectory(inputFolder)
                    .stream()
                    .filter(f -> f.getName().endsWith(".class"))
                    .forEach(f -> {
                        final String newName = f.toPath()
                                .toString()
                                .replace(inputFolder + "classes" + File.separator, "");

                        final JarEntry entry = new JarEntry(newName);
                        try {
                            jos.putNextEntry(entry);
                            jos.write(Files.readAllBytes(f.toPath()));
                            jos.closeEntry();
                            System.out.println("Adding class " + newName);
                        } catch (final IOException ignored) {
                        }
                    });
        }
    }
}