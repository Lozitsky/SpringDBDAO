package com.kirilo.spring.main;

import com.kirilo.spring.dao.impls.SQLiteDAO;
import com.kirilo.spring.dao.objects.MP3;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {
    public static void main(String[] args) {
        MP3 mp3 = new MP3();
        mp3.setName("Some song");
        mp3.setAuthor("Some author");

//        new SQLiteDAO().insertWithJDBC(mp3);

//        new SQLiteDAO().insertWithJDBCAnother(mp3);

        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        SQLiteDAO sqliteDAO = (SQLiteDAO) context.getBean("sqliteDAO");
        sqliteDAO.insert(mp3);
    }

}
