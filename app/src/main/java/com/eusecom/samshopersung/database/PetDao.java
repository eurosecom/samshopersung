package com.eusecom.samshopersung.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.eusecom.samshopersung.database.entity.Pet;
import java.util.List;

/**
 * Created by eurosecom on 6/25/18
 */

@Dao
public interface PetDao {

    @Query("SELECT * FROM pet")
    List<Pet> getAll();

    @Query("SELECT * FROM pet WHERE name LIKE :name LIMIT 1")
    Pet findByName(String name);

    @Insert
    void insertAll(List<Pet> pets);

    @Update
    void update(Pet pet);

    @Delete
    void delete(Pet pet);
}
