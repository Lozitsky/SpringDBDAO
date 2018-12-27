package com.kirilo.spring.dao.impls;

import com.kirilo.spring.dao.interfaces.MP3Dao;
import com.kirilo.spring.dao.objects.Author;
import com.kirilo.spring.dao.objects.MP3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component("JdbcJoinDAO")
public class PostgresJoinDAO implements MP3Dao {
    private static final String MP3TABLE = "mp3";
    private static final String MP3VIEW = "mp3_view";
    private static final String SQL_SELECT_MP3VIEW = "select * from " + MP3VIEW + " where upper(author_name) like :author_name";
    private static final String SQL_INSERT_MP3 = "insert into mp3 (name, author_id) values (:name, :author_id)";
    private static final String SQL_SELECT_NAME = "select * from " + MP3VIEW + " where upper(mp3_name) like :mp3_name";
    private static final String SQL_SELECT_ID = "select * from " + MP3VIEW + " where mp3_id=:mp3_id";
    private static final String SQL_COUNT = "select count(id) from " + MP3TABLE;
    private static final String SQL_DELETE = "delete from " + MP3TABLE + " where id=:id";
    private static final String SQL_SELECT_TRACKS = "select author_name, count(mp3_id) as count from " + MP3VIEW + " group by author_name";
    private static final String SQL_INSERT_AUTHOR = "insert into author (name) values (:name)";


    private SimpleJdbcInsert insertMP3;
//    private SimpleJdbcInsert insertAuthor;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public PostgresJoinDAO(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertMP3 = new SimpleJdbcInsert(dataSource).withTableName("mp3").usingColumns("name", "author_id");
//        this.insertAuthor = new SimpleJdbcInsert(dataSource).withTableName("author").usingColumns("name");
    }

    @Override
    public int insert(MP3 mp3) {
        MapSqlParameterSource source = getMapSqlParameterSource(mp3);
        return insertMP3.execute(source);
    }

    @Override
    public void insert(List<MP3> list) {

    }

    @Override
    public int insertList(List<MP3> listMP3) {
//        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(listMP3.toArray());

        //or
        SqlParameterSource[] batch = new SqlParameterSource[listMP3.size()];

        for (int i = 0; i < listMP3.size(); i++) {
            batch[i] = getMapSqlParameterSource(listMP3.get(i));
        }

        int[] ints = jdbcTemplate.batchUpdate(SQL_INSERT_MP3, batch);

        return ints.length;
    }

    @Override
    public int updateList(List<MP3> listMP3) {
        return 0;
    }

    @Override
    public Map<String, Integer> getStat() {
        return jdbcTemplate.query(SQL_SELECT_TRACKS, rs -> {
            Map<String, Integer> map = new TreeMap<>();
            while (rs.next()){
                String author_name = rs.getString("author_name");
                int count = rs.getInt("count");
                map.put(author_name, count);
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
        source.addValue("mp3_id", id);
        return jdbcTemplate.queryForObject(SQL_SELECT_ID, source, new MP3RowMapper());
    }

    @Override
    public int getMP3Count() {
        return jdbcTemplate.getJdbcOperations().queryForObject(SQL_COUNT, Integer.class);
    }

    @Override
    public List<MP3> getMP3ListByName(String name) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("mp3_name", "%" + name.toUpperCase() + "%");

        return jdbcTemplate.query(SQL_SELECT_NAME, source, new MP3RowMapper());
    }

    @Override
    public List<MP3> getMP3ListByAuthor(String name) {
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("author_name", "%" + name.toUpperCase() + "%");
        return jdbcTemplate.query(SQL_SELECT_MP3VIEW, source, new MP3RowMapper());
    }

    private static final class MP3RowMapper implements RowMapper<MP3> {

        @Override
        public MP3 mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = new Author();
            author.setId(rs.getInt("author_id"));
            author.setName(rs.getString("author_name"));

            MP3 mp3 = new MP3();
            mp3.setId(rs.getInt("mp3_id"));
            mp3.setName(rs.getString("mp3_name"));
            mp3.setAuthor(author);
            return mp3;
        }

    }

    private MapSqlParameterSource getMapSqlParameterSource(MP3 mp3) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("name", mp3.getAuthor().getName());
        jdbcTemplate.update(SQL_INSERT_AUTHOR, source, keyHolder, new String[]{"id"});

        source = new MapSqlParameterSource();
        source.addValue("name", mp3.getName());
        source.addValue("author_id", keyHolder.getKey().intValue());
        return source;
    }
}
