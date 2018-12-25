package com.kirilo.spring.dao.impls;

import com.kirilo.spring.dao.interfaces.MP3Dao;
import com.kirilo.spring.dao.objects.MP3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component("postgresDAONamed")
public class SQLiteDAONamedParameter implements MP3Dao {

    private NamedParameterJdbcTemplate jdbcTemplate;

    private final String SQL_INSERT = "insert into mp3 (author, name) values (:author, :name)";
    private final String SQL_DELETE = "delete from mp3 where id=:id";
    private final String SQL_SELECT = "select * from mp3 where id=:id";
    private final String SQL_SELECT_NAME = "select * from mp3 where upper(name) like :name";
    private final String SQL_SELECT_AUTHOR = "select * from mp3 where upper(author) like :author";
    private final String SQL_COUNT = "select count(*) from mp3";
    private final String SQL_COUNT_TRACKS = "select author, count(id) as count from mp3 group by author";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public int insert(MP3 mp3) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("name", mp3.getName());
        source.addValue("author", mp3.getAuthor());

//        jdbcTemplate.update(SQL_INSERT, source);
        jdbcTemplate.update(SQL_INSERT, source, keyHolder, new String[]{"id"});
        return keyHolder.getKey().intValue();
    }

    @Override
    public void insert(List<MP3> list) {
        list.forEach(mp3 -> insert(mp3));
    }

    @Override
    public int insertList(List<MP3> listMP3) {
        return 0;
    }

    @Override
    public int updateList(List<MP3> listMP3) {
        return 0;
    }

    @Override
    public Map<String, Integer> getStat() {

        return jdbcTemplate.query(SQL_COUNT_TRACKS, (ResultSetExtractor<Map<String, Integer>>) rs -> {
            TreeMap<String, Integer> map = new TreeMap<>();
            while (rs.next()) {
                String author = rs.getString("author");
                int count = rs.getInt("count");
                map.put(author, count);
            }
            return map;
        });
    }

    @Override
    public void delete(MP3 mp3) {
        delete(mp3.getId());
    }

    @Override
    public boolean delete(int id) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", id);
        return jdbcTemplate.update(SQL_DELETE, source) == 1;
    }

    @Override
    public MP3 getMP3ById(int id) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", id);
        return jdbcTemplate.queryForObject(SQL_SELECT, source, new MP3RowMapper());
    }

    @Override
    public int getMP3Count() {
        return jdbcTemplate.getJdbcOperations().queryForObject(SQL_COUNT, Integer.class);
    }

    @Override
    public List<MP3> getMP3ListByName(String name) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("name", "%" + name.toUpperCase() + "%");

        return jdbcTemplate.query(SQL_SELECT_NAME, source, new MP3RowMapper());
    }

    @Override
    public List<MP3> getMP3ListByAuthor(String author) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("author", "%" + author.toUpperCase() + "%");
        return jdbcTemplate.query(SQL_SELECT_AUTHOR, source, new MP3RowMapper());
    }

    public static final class MP3RowMapper implements RowMapper<MP3> {

        @Override
        public MP3 mapRow(ResultSet resultSet, int i) throws SQLException {
            MP3 mp3 = new MP3();
            mp3.setId(resultSet.getInt("id"));
            mp3.setAuthor(resultSet.getString("author"));
            mp3.setName(resultSet.getString("name"));
            return mp3;
        }
    }
}
