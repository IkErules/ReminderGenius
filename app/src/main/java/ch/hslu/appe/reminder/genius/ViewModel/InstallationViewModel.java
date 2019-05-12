package ch.hslu.appe.reminder.genius.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.Repository.InstallationRepository;

/*
The ViewModel does provide data to the UI while surviving configuration changes.
It acts as a communication center between the Repository and the UI.

SRP: Activity draws UI, ViewModel holds the data.
 */
public class InstallationViewModel extends AndroidViewModel {

    private InstallationRepository repository;
    private LiveData<List<Installation>> allInstallations;

    public InstallationViewModel(Application application) {
        super(application);
        this.repository = new InstallationRepository(application);
        this.allInstallations = this.repository.getAllInstallations();
    }

    public LiveData<List<Installation>> getAllInstallations() { return this.allInstallations; }

    public LiveData<Installation> getInstallationById(int id) { return repository.getInstallationById(id); }

    public Installation getSingleInstallationByIdSync(int id) { return repository.getSingleInstallationByIdSync(id); }

    public LiveData<List<Installation>> getInstallationsByExpireDate(LocalDate date) { return repository.getInstallationsByExpireDate(date); }

    public LiveData<Installation> getInstallationByAllProperties(int prodCatId, int contactId, String prodDetails, LocalDate instDate,
                                                                 LocalDate expireDate, int serviceInterval, String notes,
                                                                 boolean notCustMail, boolean notCustSms, boolean notCreatorMail,
                                                                 boolean notCreatorSms) {
        return repository.getInstallationByAllProperties(prodCatId, contactId, prodDetails, instDate, expireDate, serviceInterval,
                notes, notCustMail, notCustSms, notCreatorMail, notCreatorSms); }

    public void insert(Installation... installations) { this.repository.insert(installations); }

    public int insert(Installation installation) { return this.repository.insert(installation); }

    public void deleteAllInstallations() {
        Log.i("InstallationViewModel", "Deleting all Installations from DB!");
        this.repository.deleteAllInstallations();
    }

    public void delete(Installation installation) {
        this.repository.delete(installation);
    }
}
