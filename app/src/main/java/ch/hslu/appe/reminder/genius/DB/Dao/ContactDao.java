package ch.hslu.appe.reminder.genius.DB.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import androidx.lifecycle.LiveData;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Entity.Contact;

@Dao
public interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contact contact);

    @Update
    public void updateContacts(Contact... contacts);

    @Delete
    public void deleteContacts(Contact... contacts);

    @Query("DELETE FROM Contact")
    void deleteAllContacts();

    @Query("SELECT * FROM Contact WHERE firstName LIKE :search " +
            "OR lastName LIKE :search")
    public LiveData<List<Contact>> findContactsWithName(String search);

    @Query("SELECT * from Contact ORDER BY contactId ASC")
    public LiveData<List<Contact>> getAllContacts();
}
