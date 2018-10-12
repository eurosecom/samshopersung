package com.eusecom.samshopersung.roomdatabase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;

import com.eusecom.samshopersung.models.EkassaRequestBackup;
import com.eusecom.samshopersung.models.Orderstate;
import com.eusecom.samshopersung.models.Payment;
import com.eusecom.samshopersung.models.Pet;
import com.eusecom.samshopersung.models.Product;
import com.eusecom.samshopersung.models.Transport;


/**
 * Created by eurosecom
 */


@Database(entities = {Product.class, Pet.class, Payment.class, Orderstate.class
        , Transport.class, EkassaRequestBackup.class }, version = 5)
@TypeConverters({DateTypeConverter.class})
public abstract class MyDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
    public abstract PetDao petDao();
    public abstract EkassaRequestBackupDao ekassaRequestBackupDao();

    public static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {


        }
    };

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {


        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `Pet` (`name` TEXT NOT NULL, PRIMARY KEY(`name`))");

        }
    };



    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE product "
                    + " ADD COLUMN price INTEGER");

        }
    };

}
