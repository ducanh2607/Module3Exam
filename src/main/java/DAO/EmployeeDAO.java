package DAO;

import connection.MyConnection;
import model.Department;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmployeeDAO {
    private Connection connection;
    private DepartmentDAO departmentDAO;
    private final String SELECT_All = "select * from employee;";
    private final String SELECT_BY_ID = "select * from employee where id = ?;";
    private final String INSERT_EMPLOYEE = "insert into employee(name, email, address, phone, salary, department_id) values (?,?,?,?,?,?);";
    private final String DELETE_BY_ID = "delete from employee where id = ?;";
    private final String UPDATE = "update employee set name = ?, email = ?, address = ?, phone = ?, salary = ?, department_id = ? where id = ?;";
    private final String SEARCH = "select * from employee where name like ?;";


    public EmployeeDAO() {
        this.connection = MyConnection.getConnection();
        this.departmentDAO = new DepartmentDAO();
    }
    private void getListEmployee(List<Employee> employees, PreparedStatement preparedStatement) throws SQLException{
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            String id  = resultSet.getString("id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            String address = resultSet.getString("address");
            String phone = resultSet.getString("phone");
            String salary = resultSet.getString("salary");
            Long departmentId =resultSet.getLong("department_id");
            Department department = departmentDAO.findDepartmentById(departmentId);
            employees.add(new Employee(Long.parseLong(id), name, email, address, phone, Double.parseDouble(salary),department));

        }
    }

    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_All)){
            getListEmployee(employees, preparedStatement);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return  employees;
    }
    public boolean deleteById(Long id){
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID)){
            preparedStatement.setLong(1,id);
            return preparedStatement.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateEmployee(Employee employee){
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)){
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getEmail());
            preparedStatement.setString(3, employee.getAddress());
            preparedStatement.setString(4, employee.getPhone());
            preparedStatement.setDouble(5, employee.getSalary());
            preparedStatement.setLong(6, employee.getDepartment().getId());
            preparedStatement.setLong(7, employee.getId());
            return  preparedStatement.executeUpdate() > 0;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public List<Employee> findAllByName(String search) {
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(SEARCH)) {
            preparedStatement.setString(1, "%" + search + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                Double salary = resultSet.getDouble("salary");
                Long categoryId = resultSet.getLong("department_id");
                Department department = departmentDAO.findDepartmentById(categoryId);
                employees.add(new Employee(id, name, email, address,phone, salary, department));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
    public boolean createEmployee(Employee employee) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(INSERT_EMPLOYEE)) {
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getEmail());
            preparedStatement.setString(3, employee.getAddress());
            preparedStatement.setString(4, employee.getPhone());
            preparedStatement.setDouble(5, employee.getSalary());
            preparedStatement.setLong(6, employee.getDepartment().getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Employee findById(Long id) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                Double salary = resultSet.getDouble("salary");
                Long departmentId = resultSet.getLong("department_id");
                Department department = departmentDAO.findDepartmentById(departmentId);
                return new Employee(id, name, email, address,phone,salary,department);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
