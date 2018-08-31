package com.eusecom.samshopersung.roomdatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.eusecom.samshopersung.models.Payment;
import java.util.List;
import io.reactivex.Flowable;

@Dao
public interface PaymentDao {

    @Query("SELECT * FROM payment ORDER BY pay DESC")
    Flowable<List<Payment>> getRxAll();

    @Query("SELECT * FROM payment")
    List<Payment> getAll();

    @Query("SELECT * FROM payment WHERE name LIKE :name LIMIT 1")
    Payment findByName(String name);

    @Insert
    void insertAll(List<Payment> payments);

    @Update
    void update(Payment payment);

    @Delete
    void delete(Payment payment);

    @Query("DELETE FROM payment WHERE pay = :pay")
    void deleteByUid(int pay);

    /**
     * Insert a payment in the database. If the payment already exists, replace it.
     * @param payment the payment to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPayment(Payment payment);

}
