package com.eusecom.samshopersung.roomdatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.eusecom.samshopersung.models.Product;

import java.util.List;
import io.reactivex.Flowable;

/**
 * Created by gonzalo on 7/14/17
 */

@Dao
public interface ProductDao {

    @Query("SELECT * FROM product ORDER BY uid DESC")
    Flowable<List<Product>> getRxAll();

    @Query("SELECT * FROM product")
    List<Product> getAll();

    @Query("SELECT * FROM product WHERE name LIKE :name LIMIT 1")
    Product findByName(String name);

    @Insert
    void insertAll(List<Product> products);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("DELETE FROM product WHERE uid = :uid")
    void deleteByUid(int uid);

    /**
     * Insert a product in the database. If the product already exists, replace it.
     * @param product the product to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(Product product);

}
