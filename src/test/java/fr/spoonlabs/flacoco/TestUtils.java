package fr.spoonlabs.flacoco;

public class TestUtils {

    public static boolean isLessThanJava11() {
        return getJavaVersion() < 11;
    }

    public static int getJavaVersion() {
        String version = System.getProperty("java.version");

        // Java 8 or lower: 1.6.0_23, 1.7.0, 1.7.0_80, 1.8.0_211
        // Java 9 or higher: 9.0.1, 11.0.4, 12, 12.0.1
        if(version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if(dot != -1) { version = version.substring(0, dot); }
        }

        return Integer.parseInt(version);
    }
}
