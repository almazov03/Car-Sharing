package carsharing;

import carsharing.state.MainMenuState;
import carsharing.state.State;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CarSharingImpl implements CarSharing {

    private final String fileName;
    private final List<State> stateList;

    CarSharingImpl(String fileName) {
        this.fileName = fileName;
        this.stateList = new ArrayList<>();
    }

    @Override
    public void run() {
        try (CompanyDao companyDao = new CompanyDaoImpl(fileName); Scanner scanner = new Scanner(
                System.in)) {
            stateList.add(new MainMenuState(scanner, companyDao, stateList));
            while (!stateList.isEmpty()) {
                stateList.get(stateList.size() - 1).execute();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
