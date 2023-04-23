package carsharing;

public class Main {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jjdbc:h2:/home/lol/IdeaProjects/Car-Sharing/src/carsharing/db/";


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