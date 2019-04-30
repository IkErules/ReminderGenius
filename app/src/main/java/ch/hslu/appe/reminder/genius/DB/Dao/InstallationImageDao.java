package ch.hslu.appe.reminder.genius.DB.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Entity.Image;
import ch.hslu.appe.reminder.genius.DB.Entity.InstallationImage;

@Dao
public interface InstallationImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(InstallationImage installationImage);

    @Query("SELECT * FROM image INNER JOIN installationimage ON image.imageId=installationimage.imageId WHERE installationimage.installationId=:installationId")
    public LiveData<List<Image>> getImagesForInstallation(final int installationId);

    @Update
    public void updateInstallationImageRelation(InstallationImage... installationImages);

    @Delete
    public void deleteInstallationImageRelation(InstallationImage... installationImages);

    @Query("DELETE FROM installationimage")
    void deleteAllInstallationImageRelations();

    @Query("SELECT * from installationimage ORDER BY installationId ASC")
    public LiveData<List<InstallationImage>> getAllInstallationImageRelations();
}
