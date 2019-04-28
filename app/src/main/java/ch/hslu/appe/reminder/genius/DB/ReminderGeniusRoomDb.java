package ch.hslu.appe.reminder.genius.DB;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import ch.hslu.appe.reminder.genius.DB.Dao.CustomerDao;
import ch.hslu.appe.reminder.genius.DB.Entity.Customer;

@Database(entities = {Customer.class}, version = 1)
public abstract class ReminderGeniusRoomDb extends RoomDatabase {

    // Abstract Getter for all Daos
    public abstract CustomerDao customerDao();

    private static volatile ReminderGeniusRoomDb INSTANCE;

    /* Implement as Singleton */
    public static ReminderGeniusRoomDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ReminderGeniusRoomDb.class) {
                if (INSTANCE == null) {
                    // Create Room DB Instance
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ReminderGeniusRoomDb.class, "remindergenius_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
