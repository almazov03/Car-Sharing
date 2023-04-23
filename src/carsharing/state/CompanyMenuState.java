package carsharing.state;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.CompanyDao;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CompanyMenuState extends AbstractState {

    private final Company company;

    public CompanyMenuState(Scanner scanner, CompanyDao companyDao, List<State> stateList,
            Company company) {
        super(scanner, companyDao, stateList);
        this.company = company;
    }

    private void createCar() {
        System.out.println("Enter the car name:");
        System.out.print("> ");

        try {
            companyDao.addCar(getName(), company);
            System.out.println("The car was added!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showCarList() {
        try {
            List<Car> carList = companyDao.getAllCars(company);
            if (carList.isEmpty()) {
                System.out.println("The car list is empty!");
            } else {
                System.out.println("Car list:");
                for (int i = 0; i < carList.size(); ++i) {
                    System.out.println((i + 1) + ". " + carList.get(i).name());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void execute() {
        System.out.println("'" + company.name() + "' company");
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
        System.out.print("> ");

        switch (getNumber(2)) {
            case 0 -> goBack();
            case 1 -> showCarList();
            case 2 -> createCar();
        }

        System.out.println();
    }
}
