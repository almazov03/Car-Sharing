package carsharing.state;

import carsharing.CompanyDao;
import carsharing.entity.Customer;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public final class MainMenuState extends AbstractState {


    public MainMenuState(Scanner scanner, CompanyDao companyDao, List<State> stateList) {
        super(scanner, companyDao, stateList);
    }

    private void selectCustomer() throws SQLException {
        List<Customer> customerList = companyDao.getAllCustomers();
        int size = customerList.size();

        if (customerList.isEmpty()) {
            System.out.println("The customer list is empty!");
            return;
        }

        System.out.println("Choose a customer:");
        for (int i = 0; i < size; ++i) {
            System.out.println((i + 1) + ". " + customerList.get(i).name());
        }
        System.out.println("0. Back");
        System.out.print("> ");

        int n = getNumber(size);
        if (n != 0) {
            stateList.add(new CustomerMenuState(scanner, companyDao, stateList,
                    customerList.get(n - 1)));
        }
    }

    private void createCustomer() throws SQLException {
        System.out.println("Enter the customer name:");
        System.out.print("> ");
        companyDao.addCustomer(getName());
        System.out.println("The customer was added!");
    }

    private void goToManagerMenu() {
        stateList.add(new ManagerMenuState(scanner, companyDao, stateList));
    }

    @Override
    public void execute() throws SQLException {
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");
        System.out.print("> ");

        switch (getNumber(3)) {
            case 0 -> goBack();
            case 1 -> goToManagerMenu();
            case 2 -> selectCustomer();
            case 3 -> createCustomer();
        }

        System.out.println();
    }
}
