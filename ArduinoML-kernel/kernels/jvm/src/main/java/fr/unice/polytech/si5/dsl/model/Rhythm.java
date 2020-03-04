package fr.unice.polytech.si5.dsl.model;

import java.util.HashMap;
import java.util.Map;

/**
 * This should be an enumeration but we can't put special characters as identifier so this is a normal class.
 * It is used to get the velocity and duration associated to a given rhythm name.
 */
public class Rhythm {
    private static Map<String, Map<String, Object>> rhythms = new HashMap<>();

    public static int getVelocity(String name) {
        if (rhythms.isEmpty()) {
            init();
        }
        return (int) rhythms.get(name).get("velocity");
    }

    public static float getDuration(String name) {
        if (rhythms.isEmpty()) {
            init();
        }
        return (float) rhythms.get(name).get("duration");
    }

    private static void init() {
        // Played rhythms
        rhythms.put("w", createVariables(100, 4));
        rhythms.put("h", createVariables(100, 2));
        rhythms.put("q", createVariables(100, 1));
        rhythms.put("e", createVariables(100, (float) 1 / 2));
        rhythms.put("t", createVariables(100, (float) 1 / 3));
        rhythms.put("s", createVariables(100, (float) 1 / 4));

        // Rests (velocity = 0)
        rhythms.put(".", createVariables(0, (float) 1 / 4));
        rhythms.put("-", createVariables(0, (float) 1 / 2));
        rhythms.put("_", createVariables(0, 1));
    }

    private static Map<String, Object> createVariables(int velocity, float duration) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("velocity", velocity);
        variables.put("duration", duration);

        return variables;
    }
}
