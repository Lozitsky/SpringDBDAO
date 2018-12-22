package com.kirilo.spring.main;

import com.kirilo.spring.dao.interfaces.MP3Dao;
import com.kirilo.spring.dao.objects.MP3;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Start {
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

//        new SQLiteDAO().insertWithJDBC(mp3);

//        new SQLiteDAO().insertWithJDBCAnother(mp3);

        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
//        SQLiteDAO sqliteDAO = (SQLiteDAO) context.getBean("sqliteDAO");
        MP3Dao sqliteDAONamed = (MP3Dao) context.getBean("sqliteDAONamed");
//        sqliteDAO.insert(mp3);
//        sqliteDAO.delete(9);
//        sqliteDAO.insert(list);
        System.out.println(sqliteDAONamed.getMP3ListByAuthor("KAZKA"));
        System.out.println(sqliteDAONamed.getMP3Count());
    }

}
