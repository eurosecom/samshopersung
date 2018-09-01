package com.eusecom.samshopersung.roomdatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.eusecom.samshopersung.models.Orderstate;
import java.util.List;
import io.reactivex.Flowable;

@Dao
public interface OrderstateDao {

    @Query("SELECT * FROM orderstate ORDER BY ost DESC")
    Flowable<List<Orderstate>> getRxAll();

    @Query("SELECT * FROM orderstate")
    List<Orderstate> getAll();

    @Query("SELECT * FROM orderstate WHERE name LIKE :name LIMIT 1")
    Orderstate findByName(String name);

    @Insert
    void insertAll(List<Orderstate> orderstates);

    @Update
    void update(Orderstate orderstate);

    @Delete
    void delete(Orderstate orderstate);

    @Query("DELETE FROM orderstate WHERE ost = :ost")
    void deleteByUid(int ost);

    /**
     * Insert a orderstate in the database. If the orderstate already exists, replace it.
     * @param orderstate the orderstate to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrderstate(Orderstate orderstate);

}
