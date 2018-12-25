package com.kirilo.spring.main;

import com.kirilo.spring.dao.interfaces.MP3Dao;
import com.kirilo.spring.dao.objects.MP3;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartSimpleJDBCInsert {
    public static void main(String[] args) {
        MP3 mp3 = new MP3();
        mp3.setName("Some song");
        mp3.setAuthor("Some author");

        MP3 mp3_2 = new MP3();
        mp3_2.setName("Some song2");
        mp3_2.setAuthor("Some author2");

        MP3 mp3_3 = new MP3();
        mp3_3.setName("Some song3");
        mp3_3.setAuthor("Some author3");

        List<MP3> list = new ArrayList<>();
        list.addAll(Arrays.asList(mp3, mp3_2, mp3_3));

        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        MP3Dao simpleJdbcDAO = (MP3Dao) context.getBean("simpleJdbcDAO");

//        System.out.println(simpleJdbcDAO.insert(mp3));
//        System.out.println(simpleJdbcDAO.insertList(list));
        mp3.setId(1);
        mp3_2.setId(2);
        mp3_3.setId(3);
        System.out.println(simpleJdbcDAO.updateList(list));
    }
}
