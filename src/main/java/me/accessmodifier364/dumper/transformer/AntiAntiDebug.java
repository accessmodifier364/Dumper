package me.accessmodifier364.dumper.transformer;

import me.accessmodifier364.dumper.util.ASMUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import templates.RuntimeImpl;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Arrays;

/**
 * @author accessmodifier364
 * @since 9/24/2023 at 16:15
 */

public final class AntiAntiDebug implements ClassFileTransformer {

    @Override
    public byte[] transform(
            final ClassLoader loader,
            final String className,
            final Class<?> classBeingRedefined,
            final ProtectionDomain protectionDomain,
            final byte[] classfileBuffer
    ) {
        if (!className.equals("sun/management/RuntimeImpl")) {
            return classfileBuffer;
        }

        final ClassNode cn = new ClassNode();
        final ClassReader cr = new ClassReader(classfileBuffer);
        cr.accept(cn, ClassReader.SKIP_FRAMES);

        final ClassNode template = ASMUtil.getClassNode(RuntimeImpl.class);

        template.methods.forEach(m -> Arrays.stream(m.instructions.toArray())
                .forEach(i -> {
                    if (i instanceof FieldInsnNode) {
                        final FieldInsnNode i2 = (FieldInsnNode) i;

                        if (i2.owner.equals(template.name)) {
                            i2.owner = className;
                        }
                    } else if (i instanceof InvokeDynamicInsnNode) {
                        final InvokeDynamicInsnNode i2 = (InvokeDynamicInsnNode) i;

                        if (i2.bsmArgs[1] instanceof Handle) {
                            final Handle h = (Handle) i2.bsmArgs[1];

                            if (h.getOwner().equals(template.name)) {
                                i2.bsmArgs[1] = new Handle(Opcodes.H_INVOKESTATIC, className, h.getName(), h.getDesc(), h.isInterface());
                            }
                        }
                    }
                }));

        template.methods.stream()
                .filter(m -> m.name.equals("lambda$getInputArguments$0"))
                .findAny()
                .ifPresent(m -> cn.methods.add(m));

        cn.methods.stream()
                .filter(m -> m.name.equals("getInputArguments"))
                .forEach(m -> template.methods
                        .stream()
                        .filter(m2 -> m2.name.equals(m.name))
                        .findAny()
                        .ifPresent(m2 -> {
                            m.instructions = m2.instructions;
                            m.tryCatchBlocks = m2.tryCatchBlocks;
                        }));

        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cn.accept(cw);

        return cw.toByteArray();
    }
}