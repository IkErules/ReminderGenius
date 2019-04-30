package ch.hslu.appe.reminder.genius.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Dao.ImageDao;
import ch.hslu.appe.reminder.genius.DB.Entity.Image;
import ch.hslu.appe.reminder.genius.DB.ReminderGeniusRoomDb;

public class ImageRepository {

    private ImageDao imageDao;
    private LiveData<List<Image>> allImages;

    public ImageRepository(Application application) {
        ReminderGeniusRoomDb db = ReminderGeniusRoomDb.getDatabase(application);
        this.imageDao = db.imageDao();
        this.allImages = this.imageDao.getAllImages();
    }

    public LiveData<List<Image>> getAllImages() {
        return this.allImages;
    }

    public void insert (Image image) {
        new insertAsyncTask(this.imageDao).execute(image);
    }

    public void deleteAllImages() { new deleteAllImagesAsyncTask(this.imageDao).execute(); }

    private static class insertAsyncTask extends AsyncTask<Image, Void, Void> {

        private ImageDao mAsyncTaskDao;

        insertAsyncTask(ImageDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Image... params) {
            this.mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllImagesAsyncTask extends AsyncTask<Void, Void, Void> {

        private ImageDao mAsyncTaskDao;

        deleteAllImagesAsyncTask(ImageDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            this.mAsyncTaskDao.deleteAllImages();
            return null;
        }
    }
}