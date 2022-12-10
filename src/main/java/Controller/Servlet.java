package Controller;

import DAO.EmployeeDAO;
import Service.EmployeeService;
import model.Department;

import java.io.*;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/main")
public class Servlet extends HttpServlet {
    private EmployeeService employeeService;
    private EmployeeDAO employeeDAO;

    public void init() {
        employeeDAO = new EmployeeDAO();
        employeeService = new EmployeeService();

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {

            case "create":
                createForm(request, response);
                break;
            case "update":
                updateForm(request, response);
                break;
            case "delete":
                delete(request, response);
                break;
            default:
                displayListEmployee(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create":
                create(request, response);
                break;
            case "update":
                update(request, response);
                break;
            case "search":
                displaySearchEmployeeList(request, response);
                break;
        }
    }
    private void displayListEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("list.jsp");
        request.setAttribute("employees", employeeService.findAll(request));
        requestDispatcher.forward(request, response);
    }
    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        employeeService.delete(request);
        response.sendRedirect("http://localhost:8080/main");
    }
    private void createForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Department> departments = employeeService.findDepartment();
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("create.jsp");
        request.setAttribute("departments", departments);
        requestDispatcher.forward(request, response);
    }
    private void updateForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("update.jsp");
        request.setAttribute("employee", employeeService.findById(request));
        request.setAttribute("departments", employeeService.findDepartment());
        requestDispatcher.forward(request, response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        Double salary = Double.parseDouble(request.getParameter("salary"));
        Long departmentId = Long.parseLong(request.getParameter("department_id"));
        employeeService.save(request);
        response.sendRedirect("http://localhost:8080/main");
    }
    private void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (employeeService.save(request)) {
            HttpSession session = request.getSession();
            session.setAttribute("message", "Create successfully!");
        }
        response.sendRedirect("http://localhost:8080/main");
    }
    private void displaySearchEmployeeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("search");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("list.jsp");
        request.setAttribute("employees", employeeService.findByNameContaining(request));
        requestDispatcher.forward(request, response);
    }

}