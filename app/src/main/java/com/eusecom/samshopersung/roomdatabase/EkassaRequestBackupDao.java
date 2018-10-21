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

    @Query("SELECT * FROM ekassarequestbackup ORDER BY id DESC")
    Flowable<List<EkassaRequestBackup>> getRxAllRequest();

    @Query("SELECT * FROM ekassarequestbackup")
    List<EkassaRequestBackup> getAllRequest();

    @Query("SELECT * FROM ekassarequestbackup WHERE requestUuid LIKE :requestUuid LIMIT 1")
    EkassaRequestBackup findByRequestUuid(String requestUuid);

    @Query("SELECT * FROM ekassarequestbackup WHERE receiptNumber LIKE :dok LIMIT 1")
    Flowable<EkassaRequestBackup> findRequestByDok(String dok);

    @Insert
    void insertAll(List<EkassaRequestBackup> ekassarequestbackups);

    @Update
    void update(EkassaRequestBackup ekassarequestbackup);

    @Delete
    void delete(EkassaRequestBackup ekassarequestbackup);

    @Query("DELETE FROM ekassarequestbackup WHERE id = :id")
    void deleteByUid(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEkassaRequestBackup(EkassaRequestBackup ekassarequestbackup);

    @Query("UPDATE ekassarequestbackup SET responseUuid = :resUuid, " +
            " processDate = :procDate, receiptDataId = :recid WHERE requestUuid = :reqUuid ")
    void updateEkassaRequestBackup(String reqUuid, String resUuid, String procDate, String recid);

    @Query("UPDATE ekassarequestbackup SET responseUuid = :resUuid, " +
            " processDate = :procDate, receiptDataId = :recid WHERE responseUuid = '' AND id = (SELECT max(id) FROM ekassarequestbackup) ")
    void updateMaxIdEkassaRequestBackup(String resUuid, String procDate, String recid);

}
