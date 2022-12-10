package Service;

import DAO.DepartmentDAO;
import DAO.EmployeeDAO;
import model.Department;
import model.Employee;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    private static ArrayList<Employee> employees;
    private static ArrayList<Department> departments;
    private static Long INDEX;
    private EmployeeDAO employeeDAO;
    private DepartmentDAO departmentDAO;

    public EmployeeService() {
        employeeDAO = new EmployeeDAO();
        departmentDAO = new DepartmentDAO();
    }

    public List<Employee> findAll(HttpServletRequest request) {
        return employeeDAO.findAll();
    }
    public List<Department> findDepartment(){
        return departmentDAO.findAll();
    }

    public boolean delete(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        return employeeDAO.deleteById(id);
    }
    public List<Employee> findByNameContaining(HttpServletRequest request) {
        String name = request.getParameter("search");
        List<Employee> employeeSearch = new ArrayList<>();
        for (Employee employee : employeeSearch) {
            if (employee.getName().toLowerCase().contains(name.toLowerCase())) {
                employeeSearch.add(employee);
            }
        }
        return employeeDAO.findAllByName(name);
    }
    public boolean save(HttpServletRequest request) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        Double salary = Double.parseDouble(request.getParameter("salary"));
        Long departmentId = Long.parseLong(request.getParameter("department_id"));
        if (id == null) {
            return employeeDAO.createEmployee(new Employee(name, email, address, phone, salary, departmentDAO.findDepartmentById(departmentId)));
        }
        return employeeDAO.updateEmployee(new Employee(Long.parseLong(id),name, email,address, phone, salary, departmentDAO.findDepartmentById(departmentId)));
    }
    public Employee findById(HttpServletRequest request) {
        Long id = Long.parseLong(request.getParameter("id"));
        return employeeDAO.findById(id);
    }

}
