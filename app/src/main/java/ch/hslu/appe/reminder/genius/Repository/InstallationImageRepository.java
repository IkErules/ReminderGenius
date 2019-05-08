package ch.hslu.appe.reminder.genius.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Dao.InstallationImageDao;
import ch.hslu.appe.reminder.genius.DB.Entity.Image;
import ch.hslu.appe.reminder.genius.DB.Entity.InstallationImage;
import ch.hslu.appe.reminder.genius.DB.ReminderGeniusRoomDb;

public class InstallationImageRepository {

    private InstallationImageDao installationImageDao;
    private LiveData<List<InstallationImage>> allInstallationImageRelations;

    public InstallationImageRepository(Application application) {
        ReminderGeniusRoomDb db = ReminderGeniusRoomDb.getDatabase(application);
        this.installationImageDao = db.installationImageDao();
        this.allInstallationImageRelations = this.installationImageDao.getAllInstallationImageRelations();
    }

    public LiveData<List<InstallationImage>> getAllInstallationImageRelations() {
        return this.allInstallationImageRelations;
    }

    public LiveData<List<Image>> getImagesForInstallation(int installationId) {
        return this.installationImageDao.getImagesForInstallation(installationId);
    }

    public void insert (InstallationImage installationImage) {
        new insertAsyncTask(this.installationImageDao).execute(installationImage);
    }

    public void deleteAllInstallationImageRelations() { new deleteAllInstallationImageRelationsAsyncTask(this.installationImageDao).execute(); }

    private static class insertAsyncTask extends AsyncTask<InstallationImage, Void, Void> {

        private InstallationImageDao mAsyncTaskDao;

        insertAsyncTask(InstallationImageDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final InstallationImage... params) {
            this.mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAllInstallationImageRelationsAsyncTask extends AsyncTask<Void, Void, Void> {

        private InstallationImageDao mAsyncTaskDao;

        deleteAllInstallationImageRelationsAsyncTask(InstallationImageDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            this.mAsyncTaskDao.deleteAllInstallationImageRelations();
            return null;
        }
    }
}