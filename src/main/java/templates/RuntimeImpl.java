package templates;

import sun.management.Util;
import sun.management.VMManagement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author accessmodifier364
 * @since 9/24/2023 at 16:13
 */

public class RuntimeImpl {

    private VMManagement jvm;

    public List<String> getInputArguments() {
//        Util.checkMonitorAccess();
        try {
            final Method m = Util.class.getDeclaredMethod("checkMonitorAccess");
            m.setAccessible(true);
            m.invoke(null);
        } catch (final NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return jvm.getVmArguments()
                .stream()
                .filter(s -> !s.startsWith("-javaagent:"))
                .collect(Collectors.toList());
    }
}