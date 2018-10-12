package com.eusecom.samshopersung.roomdatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.eusecom.samshopersung.models.EkassaRequestBackup;
import java.util.List;
import io.reactivex.Flowable;

@Dao
public interface EkassaRequestBackupDao {

    @Query("SELECT * FROM ekassarequestbackup ORDER BY requestUuid DESC")
    Flowable<List<EkassaRequestBackup>> getRxAllRequest();

    @Query("SELECT * FROM ekassarequestbackup")
    List<EkassaRequestBackup> getAllRequest();

    @Query("SELECT * FROM ekassarequestbackup WHERE requestUuid LIKE :requestUuid LIMIT 1")
    EkassaRequestBackup findByRequestUuid(String requestUuid);

    @Insert
    void insertAll(List<EkassaRequestBackup> ekassarequestbackups);

    @Update
    void update(EkassaRequestBackup ekassarequestbackup);

    @Delete
    void delete(EkassaRequestBackup ekassarequestbackup);

    @Query("DELETE FROM ekassarequestbackup WHERE id = :id")
    void deleteByUid(String id);

    /**
     * Insert a ekassarequestbackup in the database. If the ekassarequestbackup already exists, replace it.
     * @param ekassarequestbackup the ekassarequestbackup to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEkassaRequestBackup(EkassaRequestBackup ekassarequestbackup);

}
