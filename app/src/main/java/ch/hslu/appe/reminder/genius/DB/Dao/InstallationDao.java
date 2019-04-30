package ch.hslu.appe.reminder.genius.DB.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Entity.Installation;

@Dao
public interface InstallationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Installation contact);

    @Update
    public void updateInstallations(Installation... contacts);

    @Delete
    public void deleteInstallations(Installation... contacts);

    @Query("DELETE FROM Installation")
    void deleteAllInstallations();

    // TODO: Add method to search by any component
    @Query("SELECT * FROM Installation WHERE productDetails LIKE :search ")
    public LiveData<List<Installation>> findInstallationsWithProductDetails(String search);

    @Query("SELECT * from Installation ORDER BY installationId ASC")
    public LiveData<List<Installation>> getAllInstallations();
}
