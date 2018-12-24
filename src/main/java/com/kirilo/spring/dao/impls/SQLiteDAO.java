package com.kirilo.spring.dao.impls;

import com.kirilo.spring.dao.db.DBConnector;
import com.kirilo.spring.dao.interfaces.MP3Dao;
import com.kirilo.spring.dao.objects.MP3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component("sqliteDAO")
public class SQLiteDAO implements MP3Dao {
    private JdbcTemplate jdbcTemplate;
    private final String SQL_INSERT = "insert into mp3 (author, name) values (?, ?)";
    private final String SQL_DELETE = "delete from mp3 where id=?";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int insert(MP3 mp3) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(SQL_INSERT, new Object[]{mp3.getAuthor(), mp3.getName()});
        jdbcTemplate.update(SQL_INSERT, new Object[]{mp3.getAuthor(), mp3.getName()}, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public void insert(List<MP3> list) {
        list.forEach(mp3 -> insert(mp3));
    }

    @Override
    public Map<String, Integer> getStat() {
        return null;
    }

    public void insertWithJDBCAnother(MP3 mp3) {
        DBConnector dbConnector = new DBConnector();

        try(PreparedStatement preparedStatement = dbConnector.getMyConnection().prepareStatement(SQL_INSERT)) {
            preparedStatement.setString(1, mp3.getAuthor());
            preparedStatement.setString(2, mp3.getName());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //14 strings
    public void insertWithJDBC(MP3 mp3) {
        String url = "jdbc:postgresql://localhost:5432/springdb";
        String login = "root";
        String pass = "root";

        try(Connection connection = DriverManager.getConnection(url, login, pass);
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
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
        delete(mp3.getId());
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(SQL_DELETE, new Object[]{id}) == 1;
    }

    @Override
    public MP3 getMP3ById(int id) {
        return null;
    }

    @Override
    public int getMP3Count() {
        return 0;
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
