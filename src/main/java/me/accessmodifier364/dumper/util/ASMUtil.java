package me.accessmodifier364.dumper.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.InputStream;

/**
 * @author accessmodifier364
 * @since 9/24/2023 at 16:30
 */

public final class ASMUtil {

    private static InputStream getClassInputStream(final Class<?> c) {

        return c.getClassLoader()
                .getResourceAsStream(c.getName().replace('.', '/') + ".class");
    }

    public static ClassNode getClassNode(final Class<?> c) {

        try (final InputStream is = getClassInputStream(c)) {

            final ClassReader cr = new ClassReader(is);
            final ClassNode cn = new ClassNode();
            cr.accept(cn, 0);

            return cn;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}