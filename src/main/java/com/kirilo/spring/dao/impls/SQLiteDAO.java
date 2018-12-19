package com.kirilo.spring.dao.impls;

import com.kirilo.spring.dao.db.DBConnector;
import com.kirilo.spring.dao.interfaces.MP3Dao;
import com.kirilo.spring.dao.objects.MP3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component("sqliteDAO")
public class SQLiteDAO implements MP3Dao {
    private JdbcTemplate jdbcTemplate;
    private final String SQL = "insert into mp3 (author, name) values (?, ?)";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(MP3 mp3) {
        jdbcTemplate.update(SQL, new Object[]{mp3.getAuthor(), mp3.getName()});
    }

    public void insertWithJDBCAnother(MP3 mp3) {
        DBConnector dbConnector = new DBConnector();

        try(PreparedStatement preparedStatement = dbConnector.getMyConnection().prepareStatement(SQL)) {
            preparedStatement.setString(1, mp3.getAuthor());
            preparedStatement.setString(2, mp3.getName());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertWithJDBC(MP3 mp3) {
        String url = "jdbc:postgresql://localhost:5432/springdb";
        String login = "root";
        String pass = "root";

        try(Connection connection = DriverManager.getConnection(url, login, pass);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            Class.forName("org.postgresql.Driver");
            preparedStatement.setString(1, mp3.getAuthor());
            preparedStatement.setString(2, mp3.getName());
            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(MP3 mp3) {

    }

    @Override
    public MP3 getMP3ById(int id) {
        return null;
    }

    @Override
    public List<MP3> getMP3ListByName(String name) {
        return null;
    }

    @Override
    public List<MP3> getMP3ListByAuthor(String name) {
        return null;
    }
}
