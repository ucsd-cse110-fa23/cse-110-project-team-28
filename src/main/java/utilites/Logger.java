package utilites;

public class Logger {
    public static void log(String message) {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();

        if (stackTraceElement.length > 2) {
            StackTraceElement caller = stackTraceElement[2];
            System.out.println("[INFO][" + caller.getClassName() + "." + caller.getMethodName() + "] " + message);
        } else {
            System.out.println("[Logger] " + message);
        }
    }

    public static void warn(String message) {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();

        if (stackTraceElement.length > 2) {
            StackTraceElement caller = stackTraceElement[2];
            System.out.println("[WARN][" + caller.getClassName() + "." + caller.getMethodName() + "] " + message);
        } else {
            System.out.println("[Logger] " + message);
        }
    }

    public static void error(String message) {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();

        if (stackTraceElement.length > 2) {
            StackTraceElement caller = stackTraceElement[2];
            System.out.println("[ERROR][" + caller.getClassName() + "." + caller.getMethodName() + "] " + message);
        } else {
            System.out.println("[Logger] " + message);
        }
    }
}
