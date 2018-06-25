package com.eusecom.samshopersung.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import com.eusecom.samshopersung.SamshopperApp;
import com.eusecom.samshopersung.database.entity.Pet;
import com.eusecom.samshopersung.database.entity.Product;


/**
 * Created by gonzalo on 7/14/17
 */

@Database(entities = {Product.class, Pet.class}, version = 3)
@TypeConverters({DateTypeConverter.class})
public abstract class MyDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract PetDao petDao();

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `Pet` (`name` TEXT NOT NULL, PRIMARY KEY(`name`))");

            // enable flag to force update products
            SamshopperApp.get().setForceUpdate(true);
        }
    };



    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE product "
                    + " ADD COLUMN price INTEGER");

            // enable flag to force update products
            SamshopperApp.get().setForceUpdate(true);
        }
    };

}
