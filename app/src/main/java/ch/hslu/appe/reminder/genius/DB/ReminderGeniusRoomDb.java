package ch.hslu.appe.reminder.genius.DB;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;

import java.util.concurrent.Executors;

import ch.hslu.appe.reminder.genius.DB.Converter.LocalDateConverter;
import ch.hslu.appe.reminder.genius.DB.Dao.ContactDao;
import ch.hslu.appe.reminder.genius.DB.Dao.ImageDao;
import ch.hslu.appe.reminder.genius.DB.Dao.InstallationDao;
import ch.hslu.appe.reminder.genius.DB.Dao.InstallationImageDao;
import ch.hslu.appe.reminder.genius.DB.Dao.ProductCategoryDao;
import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.DB.Entity.Image;
import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.DB.Entity.InstallationImage;
import ch.hslu.appe.reminder.genius.DB.Entity.ProductCategory;

// Update the Database Version when a schema change was done.
@Database(entities = {Contact.class, ProductCategory.class, Image.class, Installation.class, InstallationImage.class}, version = 7)
@TypeConverters({LocalDateConverter.class})
public abstract class ReminderGeniusRoomDb extends RoomDatabase {

    // Abstract Getter for all Daos
    public abstract ContactDao contactDao();
    public abstract ProductCategoryDao productCategoryDao();
    public abstract ImageDao imageDao();
    public abstract InstallationDao installationDao();
    public abstract InstallationImageDao installationImageDao();

    private static Context context;

    private static volatile ReminderGeniusRoomDb INSTANCE;

    /* Implement as Singleton */
    public static ReminderGeniusRoomDb getDatabase(final Context c) {
        if (INSTANCE == null) {
            context = c;
            synchronized (ReminderGeniusRoomDb.class) {
                if (INSTANCE == null) {
                    // Create Room DB Instance
                    // fallbackToDestructiveMigration --> DB WILL BE PURGED ON SCHEMA VERSION UPDATE!
                    INSTANCE = Room.databaseBuilder(c.getApplicationContext(),
                            ReminderGeniusRoomDb.class, "remindergenius_database")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .addCallback(prePopulateProducts)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback prePopulateProducts = new RoomDatabase.Callback() {
        public void onCreate(SupportSQLiteDatabase db) {
            Executors.newSingleThreadScheduledExecutor().execute(() ->
                    getDatabase(context).productCategoryDao()
                            .insertAll(ProductCategory.prePopulateData()));
        }
    };
}
