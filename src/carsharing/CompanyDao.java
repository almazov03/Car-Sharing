package carsharing;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;
import java.sql.SQLException;
import java.util.List;

public interface CompanyDao extends AutoCloseable {

    void addCompany(String companyName) throws SQLException;

    void addCar(String carName, Company company) throws SQLException;

    void addCustomer(String customerName) throws SQLException;

    void addRelation(Customer customer, Car car) throws SQLException;

    List<Company> getAllCompanies() throws SQLException;

    List<Car> getAllCars(Company company) throws SQLException;

    List<Customer> getAllCustomers() throws SQLException;

    List<Car> getAllFreeCar(Company company) throws SQLException;

    Car getCar(int id) throws SQLException;

    Car getCar(Customer customer) throws SQLException;

    Company getCompany(Car car) throws SQLException;

    Company getCompany(int id) throws SQLException;

    void deleteRelation(Customer customer, Car car) throws SQLException;

}
