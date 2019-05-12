package ch.hslu.appe.reminder.genius.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Entity.Image;
import ch.hslu.appe.reminder.genius.Repository.ImageRepository;

/*
The ViewModel does provide data to the UI while surviving configuration changes.
It acts as a communication center between the Repository and the UI.

SRP: Activity draws UI, ViewModel holds the data.
 */
public class ImageViewModel extends AndroidViewModel {

    private ImageRepository repository;
    private LiveData<List<Image>> allImages;

    public ImageViewModel(Application application) {
        super(application);
        this.repository = new ImageRepository(application);
        this.allImages = this.repository.getAllImages();
    }

    public LiveData<List<Image>> getAllImages() { return this.allImages; }

    public void insert(Image... images) { this.repository.insert(images); }

    public int insert(Image image) { return this.repository.insert(image); }

    public LiveData<Image> getImageWithPath(String path) { return repository.getImageWithPath(path); }

    public void deleteAllImages() {
        Log.i("ImageViewModel", "Deleting all Images from DB!");
        this.repository.deleteAllImages();
    }
}
