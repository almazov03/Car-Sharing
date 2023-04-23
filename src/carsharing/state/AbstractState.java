package carsharing.state;

import carsharing.CompanyDao;
import carsharing.entity.Company;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public abstract class AbstractState implements State {

    protected final Scanner scanner;
    protected final CompanyDao companyDao;
    protected final List<State> stateList;

    AbstractState(Scanner scanner, CompanyDao companyDao, List<State> stateList) {
        this.scanner = scanner;
        this.companyDao = companyDao;
        this.stateList = stateList;
    }


    protected Company chooseCompany() throws SQLException {
        List<Company> companyList = companyDao.getAllCompanies();
        int size = companyList.size();

        if (companyList.isEmpty()) {
            System.out.println("The company list is empty!");
            return null;
        }

        System.out.println("Choose a company:");
        for (int i = 0; i < size; ++i) {
            System.out.println((i + 1) + ". " + companyList.get(i).name());
        }
        System.out.println("0. Back");
        System.out.print("> ");

        int n = getNumber(size);
        if (n != 0) {
            return companyList.get(n - 1);
        }

        return null;
    }

    protected int getNumber(int size) {
        while (true) {
            try {
                int n = scanner.nextInt();
                Objects.checkIndex(n, size + 1);
                return n;
            } catch (Exception e) {
                System.out.println("Enter number from 0 to " + size);
                System.out.print("> ");
            }
        }
    }

    protected String getName() {
        while (true) {
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                return name;
            }
        }
    }

    protected void goBack() {
        assert (!stateList.isEmpty());
        stateList.remove(stateList.size() - 1);
    }
}
