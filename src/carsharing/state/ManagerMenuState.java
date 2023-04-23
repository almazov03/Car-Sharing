package carsharing.state;

import carsharing.CompanyDao;
import carsharing.entity.Company;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public final class ManagerMenuState extends AbstractState {


    public ManagerMenuState(Scanner scanner, CompanyDao companyDao, List<State> stateList) {
        super(scanner, companyDao, stateList);
    }

    private void selectCompany() throws SQLException {
        Company company = chooseCompany();
        if (company != null) {
            stateList.add(new CompanyMenuState(scanner, companyDao, stateList, company));
        }
    }

    private void createCompany() throws SQLException {
        System.out.println("Enter the company name:");
        System.out.print("> ");
        companyDao.addCompany(getName());
        System.out.println("The company was created!");
    }

    @Override
    public void execute() throws SQLException {
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
        System.out.print("> ");

        switch (getNumber(2)) {
            case 0 -> goBack();
            case 1 -> selectCompany();
            case 2 -> createCompany();
        }

        System.out.println();
    }
}
