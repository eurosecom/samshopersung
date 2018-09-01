package com.eusecom.samshopersung.roomdatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.eusecom.samshopersung.models.Transport;
import java.util.List;
import io.reactivex.Flowable;

@Dao
public interface TransportDao {

    @Query("SELECT * FROM transport ORDER BY trans DESC")
    Flowable<List<Transport>> getRxAll();

    @Query("SELECT * FROM transport")
    List<Transport> getAll();

    @Query("SELECT * FROM transport WHERE name LIKE :name LIMIT 1")
    Transport findByName(String name);

    @Insert
    void insertAll(List<Transport> transports);

    @Update
    void update(Transport transport);

    @Delete
    void delete(Transport transport);

    @Query("DELETE FROM transport WHERE trans = :trans")
    void deleteByUid(int trans);

    /**
     * Insert a transport in the database. If the transport already exists, replace it.
     * @param transport the transport to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTransport(Transport transport);

}
