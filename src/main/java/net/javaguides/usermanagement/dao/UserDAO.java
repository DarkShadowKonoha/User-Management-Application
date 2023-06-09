package net.javaguides.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import net.javaguides.usermanagement.model.User;

public class UserDAO {
	
	private String jdbcURL = "jdbc:mysql://localhost/demo?useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "Girish@1";
	
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + " (name, email, country) VALUES "
			+ " (?, ?, ?); ";
	
	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id=?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
	private static final String UPDATE_USERS_SQL = "update users set name = ?, email= ?, country =? where id =?;";

	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return connection;
	}
	
	// Create or insert user
	public void insertUser(User user) throws SQLException{
		try(Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	// update user
	public boolean updateUser(User user) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getCountry());
			statement.setInt(4, user.getId());
			
			rowUpdated = statement.executeUpdate() > 0;
			
		} 
		return rowUpdated;
	}
	
	// select user by id
	public User selectUser(int id) {
		User user = null;
		// s1 - establishing a connection
		try (Connection connection = getConnection();
				// s2 - create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			// s3 - Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();
			
			// s4 - Process the ResultSet object
			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id, name, email, country);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return user;
	}
	
	// select users
	public List<User> selectAllUsers() {
		List<User> users = new ArrayList<>();
		// s1 - establishing a connection
		try (Connection connection = getConnection();
				// s2 - create a statement using connection object
			PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
			System.out.println(preparedStatement);
			// s3 - Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();
			
			// s4 - Process the ResultSet object
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new User(id, name, email, country));
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return users;
	}
	
	// delete user
	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}
}
