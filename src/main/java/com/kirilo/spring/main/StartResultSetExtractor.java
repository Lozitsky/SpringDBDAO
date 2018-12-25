package com.kirilo.spring.main;

import com.kirilo.spring.dao.interfaces.MP3Dao;
import com.kirilo.spring.dao.objects.MP3;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartResultSetExtractor {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        MP3Dao postgresDAONamed = (MP3Dao) context.getBean("postgresDAONamed");

        System.out.println(postgresDAONamed.getStat());

        MP3 mp3 = new MP3();
        mp3.setName("Some song");
        mp3.setAuthor("Some author");

        System.out.println(postgresDAONamed.insert(mp3));
        //gets Exception: "org.springframework.dao.EmptyResultDataAccessException: Incorrect result size: expected 1, actual 0"
        System.out.println(postgresDAONamed.getMP3ById(1000));
    }
}
