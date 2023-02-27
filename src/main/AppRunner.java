package main;

/**
 * App runner class for running assignment programs.
 */
public class AppRunner {

    private enum AssignmentType {
        ASS_1_AND_2,
        ASS_3
    }

    private static final AssignmentType assType = AssignmentType.ASS_3;
    private static final boolean TEST = false;

    /**
     * Main entry point into app.
     *
     * @param args the commandline args
     */
    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir") + "\n");
        (switch (assType) {
            case ASS_1_AND_2 -> new Assignment_1_and_2(TEST);
            case ASS_3 -> new Assignment_3(TEST);
        }).run();
    }

}
