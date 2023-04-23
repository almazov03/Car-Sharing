package carsharing;

public class Main {

    private static String getFileName(String[] args) {
        if (args.length == 2 && args[0].equals("-databaseFileName")) {
            return args[1];
        }

        return "data";
    }

    public static void main(String[] args) {
        CarSharing carSharing = new CarSharingImpl(getFileName(args));
        carSharing.run();
    }
}