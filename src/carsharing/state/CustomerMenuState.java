package carsharing.state;

import carsharing.CompanyDao;
import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CustomerMenuState extends AbstractState {

    private final Customer customer;

    public CustomerMenuState(Scanner scanner, CompanyDao companyDao,
            List<State> stateList, Customer customer) {
        super(scanner, companyDao, stateList);
        this.customer = customer;
    }


    private void returnCar() throws SQLException {
        Car car = companyDao.getCar(customer);
        if (car == null) {
            System.out.println("You didn't rent a car!");
        } else {
            companyDao.deleteRelation(customer, car);
            System.out.println("You've returned a rented car!");
        }
    }

    private void showCar() throws SQLException {
        Car car = companyDao.getCar(customer);
        if (car == null) {
            System.out.println("You didn't rent a car!");
        } else {
            System.out.println("You rented '" + car.name() + "'");
            System.out.println("Company:");
            System.out.println(companyDao.getCompany(car).name());
        }
    }

    private void rentCar() throws SQLException {
        if (companyDao.getCar(customer) != null) {
            System.out.println("You've already rented a car!");
            return;
        }

        while (true) {
            Company company = chooseCompany();
            if (company == null) {
                return;
            }

            List<Car> carList = companyDao.getAllFreeCar(company);
            int size = carList.size();
            if (carList.isEmpty()) {
                System.out.println("No available cars in the '" + company + "' company");
                continue;
            }

            System.out.println("Choose a car:");
            for (int i = 0; i < size; ++i) {
                System.out.println((i + 1) + ". " + carList.get(i).name());
            }
            System.out.println("0. Back");
            System.out.print("> ");

            int n = getNumber(size);
            if (n != 0) {
                Car car = carList.get(n - 1);
                companyDao.addRelation(customer, car);
                System.out.println("You rented '" + car.name() + "'");
                return;
            }
        }
    }

    @Override
    public void execute() throws SQLException {
        System.out.println("1. Rent a car");
        System.out.println("2. Return a rented car");
        System.out.println("3. My rented car");
        System.out.println("0. Back");
        System.out.print("> ");

        switch (getNumber(3)) {
            case 0 -> goBack();
            case 1 -> rentCar();
            case 2 -> returnCar();
            case 3 -> showCar();
        }

        System.out.println();
    }
}
