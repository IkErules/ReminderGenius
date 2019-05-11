package ch.hslu.appe.reminder.genius.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Dao.InstallationDao;
import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.DB.ReminderGeniusRoomDb;

public class InstallationRepository {

    private InstallationDao installationDao;
    private LiveData<List<Installation>> allInstallations;

    public InstallationRepository(Application application) {
        ReminderGeniusRoomDb db = ReminderGeniusRoomDb.getDatabase(application);
        this.installationDao = db.installationDao();
        this.allInstallations = this.installationDao.getAllInstallations();
    }

    public LiveData<List<Installation>> getAllInstallations() {
        return this.allInstallations;
    }


    public LiveData<List<Installation>> getInstallationsByExpireDate(LocalDate date) {
        return this.installationDao.findInstallationsByExpireDate(date);
    }

    public LiveData<Installation> getInstallationById(int id) { return this.installationDao.findInstallationById(id); }

    public Installation getSingleInstallationByIdSync(int id) {
        return this.installationDao.findSingleInstallationById(id);
    }

    public LiveData<Installation> getInstallationByAllProperties(int prodCatId, int contactId, String prodDetails, LocalDate instDate,
                                                                 LocalDate expireDate, int serviceInterval, String notes,
                                                                 boolean notCustMail, boolean notCustSms, boolean notCreatorMail,
                                                                 boolean notCreatorSms) {
        return this.installationDao.findInstallationByAllProperties(prodCatId, contactId, prodDetails, instDate, expireDate, serviceInterval,
                notes, notCustMail, notCustSms, notCreatorMail, notCreatorSms); }

    public void insert (Installation installation) {
        new insertAsyncTask(this.installationDao).execute(installation);
    }

    public void deleteAllInstallations() { new DeleteAllInstallationsAsyncTask(this.installationDao).execute(); }

    public void delete(Installation installation) {
        new DeleteInstallationsAsyncTask(this.installationDao).execute(installation);
    }

    private static class insertAsyncTask extends AsyncTask<Installation, Void, Void> {

        private InstallationDao mAsyncTaskDao;

        insertAsyncTask(InstallationDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Installation... params) {
            this.mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class DeleteAllInstallationsAsyncTask extends AsyncTask<Void, Void, Void> {

        private InstallationDao mAsyncTaskDao;

        DeleteAllInstallationsAsyncTask(InstallationDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            this.mAsyncTaskDao.deleteAllInstallations();
            return null;
        }
    }

    private static class DeleteInstallationsAsyncTask extends AsyncTask<Installation, Void, Void> {

        private InstallationDao mAsyncTaskDao;

        DeleteInstallationsAsyncTask(InstallationDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Installation... params) {
            this.mAsyncTaskDao.deleteInstallations(params[0]);
            return null;
        }
    }

    /* Sample Query AsyncTask
    private void asyncQueryInstallationFinished(Installation results) {
        searchResults.setValue(results);
    }

    private static class QueryInstallationAsyncTask extends
            AsyncTask<Integer, Void, Installation> {

        private InstallationDao asyncTaskDao;
        private InstallationRepository delegate = null;

        QueryInstallationAsyncTask(InstallationDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Installation doInBackground(final Integer... params) {
            return asyncTaskDao.findSingleInstallationById(params[0]);
        }

        @Override
        protected void onPostExecute(Installation result) {
            delegate.asyncQueryInstallationFinished(result);
        }
    }
    */
}