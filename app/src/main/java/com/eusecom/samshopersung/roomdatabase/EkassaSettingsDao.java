package com.eusecom.samshopersung.roomdatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.eusecom.samshopersung.models.EkassaRequestBackup;
import com.eusecom.samshopersung.models.EkassaSettings;

import java.util.List;
import io.reactivex.Flowable;

@Dao
public interface EkassaSettingsDao {

    @Query("SELECT * FROM ekassasettings ORDER BY id DESC")
    Flowable<List<EkassaSettings>> getRxAllSettings();

    @Query("SELECT * FROM ekassasettings WHERE id LIKE :id LIMIT 1")
    EkassaSettings findBySettingsId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEkassaSettings(EkassaSettings ekassasettings);


}
