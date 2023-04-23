package carsharing;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompanyDaoImpl implements CompanyDao {

    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./src/carsharing/db/";

    private Connection connection;
    private Statement statement;


    private void createCompanyTable() throws SQLException {
        System.out.println("Creating Company table in given database...");
        statement = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS COMPANY ( " +
                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(255) UNIQUE NOT NULL )";

        statement.executeUpdate(sql);
        System.out.println("Created Company table in given database");
    }

    private void createCarTable() throws SQLException {
        System.out.println("Creating Car table in given database...");
        statement = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS CAR ( " +
                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                "COMPANY_ID INT NOT NULL, " +
                "CONSTRAINT fk_company FOREIGN KEY (COMPANY_ID) " +
                "REFERENCES COMPANY(ID))";

        statement.executeUpdate(sql);
        System.out.println("Created Car table in given database");
    }

    private void createCustomerTable() throws SQLException {
        System.out.println("Creating Customer table in given database...");
        statement = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS CUSTOMER ( " +
                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                "RENTED_CAR_ID INT UNIQUE, " +
                "CONSTRAINT fk_car FOREIGN KEY (RENTED_CAR_ID) " +
                "REFERENCES CAR(ID))";

        statement.executeUpdate(sql);
        System.out.println("Created Customer table in given database");
    }

    private void openConnection(String fileName) throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);


        System.out.println("Connecting to database...");
        connection = DriverManager.getConnection(DB_URL + fileName);
        connection.setAutoCommit(true);
    }

    CompanyDaoImpl(String fileName) throws SQLException, ClassNotFoundException {
        openConnection(fileName);
        createCompanyTable();
        createCarTable();
        createCustomerTable();
        System.out.println();
    }

    @Override
    public void addCompany(String companyName) throws SQLException {
        String sql = "INSERT INTO COMPANY(NAME) VALUES ('" + companyName + "')";
        statement.executeUpdate(sql);
    }

    @Override
    public void addCar(String name, Company company) throws SQLException {
        String sql = "INSERT INTO CAR(NAME, COMPANY_ID)" +
                "VALUES ('" + name + "', " + company.id() + ")";
        statement.executeUpdate(sql);
    }

    @Override
    public void addCustomer(String customerName) throws SQLException {
        String sql = "INSERT INTO CUSTOMER(NAME) VALUES ('" + customerName + "')";
        statement.executeUpdate(sql);
    }

    @Override
    public void addRelation(Customer customer, Car car) throws SQLException {
        String sql = "UPDATE CUSTOMER " +
                "SET RENTED_CAR_ID = '" + car.id() + "' " +
                "WHERE ID = " + customer.id();
        statement.executeUpdate(sql);
    }


    @Override
    public List<Company> getAllCompanies() throws SQLException {
        String sql = "SELECT * FROM COMPANY ORDER BY ID";
        ResultSet resultSet = statement.executeQuery(sql);

        List<Company> companyList = new ArrayList<>();
        while (resultSet.next()) {
            String name = resultSet.getString("NAME");
            int id = resultSet.getInt("ID");
            companyList.add(new Company(id, name));
        }

        return companyList;
    }

    private List<Car> getCarList(ResultSet resultSet) throws SQLException {
        List<Car> carList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String name = resultSet.getString("NAME");
            int companyId = resultSet.getInt("COMPANY_ID");
            carList.add(new Car(id, name, companyId));
        }

        return carList;
    }

    private List<Customer> getCustomerList(ResultSet resultSet) throws SQLException {
        List<Customer> customerList = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String name = resultSet.getString("NAME");
            int rentedCarId = resultSet.getInt("RENTED_CAR_ID");
            customerList.add(new Customer(id, name, rentedCarId));
        }

        return customerList;
    }

    @Override
    public List<Car> getAllCars(Company company) throws SQLException {
        String sql = "SELECT * FROM CAR WHERE COMPANY_ID = " + company.id();
        ResultSet resultSet = statement.executeQuery(sql);
        return getCarList(resultSet);
    }

    @Override
    public List<Customer> getAllCustomers() throws SQLException {
        String sql = "SELECT * FROM CUSTOMER ORDER BY ID";
        ResultSet resultSet = statement.executeQuery(sql);
        return getCustomerList(resultSet);
    }

    @Override
    public List<Car> getAllFreeCar(Company company) throws SQLException {
        List<Integer> idList = new ArrayList<>();
        {
            String sql = "SELECT * FROM CUSTOMER ";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int carId = resultSet.getInt("RENTED_CAR_ID");
                if (carId != 0){
                    idList.add(carId);
                }
            }
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM CAR WHERE COMPANY_ID = " + company.id());
        for (Integer id : idList) {
            sql.append(" AND ID != ").append(id);
        }
        ResultSet resultSet = statement.executeQuery(sql.toString());


        return getCarList(resultSet);
    }

    @Override
    public Car getCar(int carId) throws SQLException {
        String sql = "SELECT * FROM CAR WHERE ID = " + carId;
        ResultSet resultSet = statement.executeQuery(sql);
        if (!resultSet.next()) {
            return null;
        }

        int id = resultSet.getInt("ID");
        String name = resultSet.getString("NAME");
        int companyId = resultSet.getInt("COMPANY_ID");
        return new Car(id, name, companyId);
    }

    @Override
    public Car getCar(Customer customer) throws SQLException {
        String sql = "SELECT * FROM CUSTOMER WHERE ID = " + customer.id();
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            return getCar(resultSet.getInt("RENTED_CAR_ID"));
        }

        return null;
    }

    @Override
    public Company getCompany(Car car) throws SQLException {
        return getCompany(car.companyId());
    }

    @Override
    public Company getCompany(int id) throws SQLException {
        String sql = "SELECT * FROM COMPANY WHERE ID = " + id;
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            String name = resultSet.getString("NAME");
            return new Company(id, name);
        }

        return null;
    }

    @Override
    public void deleteRelation(Customer customer, Car car) throws SQLException {
        String sql = "UPDATE CUSTOMER " +
                "SET RENTED_CAR_ID = NULL " +
                "WHERE ID = " + customer.id();
        statement.executeUpdate(sql);
    }


    @Override
    public void close() throws SQLException {
        Objects.requireNonNull(connection);
        Objects.requireNonNull(statement);

        connection.close();
        statement.close();

        System.out.println("Goodbye!");
    }
}
