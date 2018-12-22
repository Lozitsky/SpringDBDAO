package com.kirilo.spring.dao.interfaces;

import com.kirilo.spring.dao.objects.MP3;

import java.util.List;

public interface MP3Dao {
    void insert (MP3 mp3);
    void  insert(List<MP3> list);
    void  delete (MP3 mp3);
    boolean delete(int id);
    MP3 getMP3ById(int id);
    int getMP3Count();
    List<MP3> getMP3ListByName(String name);
    List<MP3> getMP3ListByAuthor(String name);
}
