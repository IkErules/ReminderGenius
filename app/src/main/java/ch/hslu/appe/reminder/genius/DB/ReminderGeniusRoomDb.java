package ch.hslu.appe.reminder.genius.DB;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

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
@Database(entities = {Contact.class, ProductCategory.class, Image.class, Installation.class, InstallationImage.class}, version = 4)
public abstract class ReminderGeniusRoomDb extends RoomDatabase {

    // Abstract Getter for all Daos
    public abstract ContactDao contactDao();
    public abstract ProductCategoryDao productCategoryDao();
    public abstract ImageDao imageDao();
    public abstract InstallationDao installationDao();
    public abstract InstallationImageDao installationImageDao();

    private static volatile ReminderGeniusRoomDb INSTANCE;

    /* Implement as Singleton */
    public static ReminderGeniusRoomDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ReminderGeniusRoomDb.class) {
                if (INSTANCE == null) {
                    // Create Room DB Instance
                    // fallbackToDestructiveMigration --> DB WILL BE PURGED ON SCHEMA VERSION UPDATE!
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ReminderGeniusRoomDb.class, "remindergenius_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
