package me.accessmodifier364.dumper.transformer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;

import static me.accessmodifier364.dumper.Dumper.directory;

/**
 * https://stackoverflow.com/questions/27115371/overriding-the-classloader-to-get-every-loaded-class-bytes-and-name
 *
 * @author accessmodifier364
 * @since 11-Nov-2021
 */

public class Transformer implements ClassFileTransformer {

    @SuppressWarnings("all")
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        if (shouldDump(className)) {

            System.out.println("Dumping: " + className);

            String newName = className + ".class";

            if (newName.contains("/")) {
                try {
                    Files.createDirectories(Paths.get(directory + "\\" + newName.substring(0, newName.lastIndexOf('/'))));
                } catch (IOException e) {
                    System.out.println("Error creating subdirectory: " + e.getMessage());
                }
            }

            try {
                FileOutputStream fos = new FileOutputStream(directory + "\\" + newName);
                fos.write(classfileBuffer);
                fos.close();
            } catch (IOException e) {
                System.out.println("Error writing class: " + e.getMessage());
            }
        }
        return classfileBuffer;
    }

    private boolean shouldDump(String className) {

        if (className == null) return false;

        for (String e : exclusions) {
            if (className.replace('/', '.').startsWith(e)) {
                return false;
            }
        }

        return true;
    }

    private final List<String> exclusions = Arrays.asList(
            "java", "sun", "javax", "jdk", "net.minecraft",
            "com.sun", "org.spongepowered"
    );

}
