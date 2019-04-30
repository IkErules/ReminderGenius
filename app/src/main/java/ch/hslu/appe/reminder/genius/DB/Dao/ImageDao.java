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

@Dao
public interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Image image);

    @Update
    public void updateImages(Image... images);

    @Delete
    public void deleteImages(Image... images);

    @Query("DELETE FROM image")
    void deleteAllImages();

    @Query("SELECT * FROM image WHERE path LIKE :search ")
    public LiveData<List<Image>> findImagesWithPath(String search);

    @Query("SELECT * from image ORDER BY imageId ASC")
    public LiveData<List<Image>> getAllImages();
}
