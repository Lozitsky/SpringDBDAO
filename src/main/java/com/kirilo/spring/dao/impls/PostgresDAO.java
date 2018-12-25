package com.kirilo.spring.dao.impls;

import com.kirilo.spring.dao.interfaces.MP3Dao;
import com.kirilo.spring.dao.objects.MP3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component("simpleJdbcDAO")
public class PostgresDAO implements MP3Dao {
    private SimpleJdbcInsert insertMP3;

    private NamedParameterJdbcTemplate jdbcTemplate;

    private DataSource dataSource;

    private final String SQL_INSERT = "insert into mp3 (author, name) values (:author, :name)";
    private final String SQL_UPDATE = "update mp3 set author = :author, name = :name where id = :id";

    @Autowired
    public void SetDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertMP3 = new SimpleJdbcInsert(dataSource).withTableName("mp3").usingColumns("name", "author");
        this.dataSource = dataSource;
    }

    @Override
    public int insert(MP3 mp3) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("name", mp3.getName());
        source.addValue("author", mp3.getAuthor());

        return insertMP3.execute(source);
    }

    @Override
    public void insert(List<MP3> list) {
        list.forEach(mp3 -> insert(mp3));
    }

    @Override
    public int insertList(List<MP3> listMP3) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(listMP3.toArray());

        //or
//        SqlParameterSource[] batch = new SqlParameterSource[listMP3.size()];
//
//        for (int i = 0; i < listMP3.size(); i++) {
//            MapSqlParameterSource msp = new MapSqlParameterSource();
//            msp.addValue("name", listMP3.get(i).getName());
//            msp.addValue("author", listMP3.get(i).getAuthor());
//            batch[i] = msp;
//        }

        int[] ints = jdbcTemplate.batchUpdate(SQL_INSERT, batch);

        return ints.length;
    }

    @Override
    public int updateList(List<MP3> listMP3) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(listMP3.toArray());
        int[] ints = jdbcTemplate.batchUpdate(SQL_UPDATE, batch);
        return ints.length;
    }

    @Override
    public Map<String, Integer> getStat() {
        return null;
    }

    @Override
    public void delete(MP3 mp3) {

    }

    @Override
    public boolean delete(int id) {
        return false;
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
