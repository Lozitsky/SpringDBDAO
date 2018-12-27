package com.kirilo.spring.main;

import com.kirilo.spring.dao.interfaces.MP3Dao;
import com.kirilo.spring.dao.objects.Author;
import com.kirilo.spring.dao.objects.MP3;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartJoinDAO {
    public static void main(String[] args) {
        MP3 mp3 = new MP3();
        mp3.setName("Some song01");
        Author author = new Author();
        author.setName("Some author01");
        mp3.setAuthor(author);

        MP3 mp3_2 = new MP3();
        mp3_2.setName("Some song02");
        Author author2 = new Author();
        author2.setName("Some author02");
        mp3_2.setAuthor(author2);

        MP3 mp3_3 = new MP3();
        mp3_3.setName("Some song03");
        Author author3 = new Author();
        author3.setName("Some author03");
        mp3_3.setAuthor(author3);

        List<MP3> list = new ArrayList<>();
        list.addAll(Arrays.asList(mp3, mp3_2, mp3_3));

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("context.xml");
        MP3Dao jdbcJoinDAO = (MP3Dao) applicationContext.getBean("JdbcJoinDAO");

//        System.out.println(jdbcJoinDAO.insert(mp3));

        System.out.println(jdbcJoinDAO.insertList(list));


    }
}
