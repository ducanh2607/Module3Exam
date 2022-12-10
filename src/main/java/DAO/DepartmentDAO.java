package DAO;

import connection.MyConnection;
import model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    private Connection connection;
    private final String SELECT_All = "select * from department;";
    private final String SELECT_BY_ID = "select * from department where id = ?;";

    public DepartmentDAO() {
        this.connection = MyConnection.getConnection();
    }
    public List<Department> findAll() {
        List<Department> categories = new ArrayList<>();
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(SELECT_All)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                categories.add(new Department(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public Department findDepartmentById(Long id) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(SELECT_BY_ID)){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long idDb = resultSet.getLong("id");
                String name = resultSet.getString("name");
                return new Department(idDb, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
