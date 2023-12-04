package utilites;

public class Logger {
    public static void log(String message) {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();

        if (stackTraceElement.length > 2) {
            StackTraceElement caller = stackTraceElement[2];
            System.out.println("[" + caller.getClassName() + "." + caller.getMethodName() + "] " + message);
        } else {
            System.out.println("[Logger] " + message);
        }
    }
}
