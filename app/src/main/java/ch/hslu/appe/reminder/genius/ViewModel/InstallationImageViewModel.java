package ch.hslu.appe.reminder.genius.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Entity.Image;
import ch.hslu.appe.reminder.genius.DB.Entity.InstallationImage;
import ch.hslu.appe.reminder.genius.Repository.ImageRepository;
import ch.hslu.appe.reminder.genius.Repository.InstallationImageRepository;

/*
The ViewModel does provide data to the UI while surviving configuration changes.
It acts as a communication center between the Repository and the UI.

SRP: Activity draws UI, ViewModel holds the data.
 */
public class InstallationImageViewModel extends AndroidViewModel {

    private InstallationImageRepository repository;
    private LiveData<List<InstallationImage>> allInstallationImageRelations;

    public InstallationImageViewModel(Application application) {
        super(application);
        this.repository = new InstallationImageRepository(application);
        this.allInstallationImageRelations = this.repository.getAllInstallatioImageRelations();
    }

    public LiveData<List<InstallationImage>> getAllInstallationImageRelations() { return this.allInstallationImageRelations; }

    public LiveData<List<Image>> getImagesForInstallation(int installationId) { return this.repository.getImagesForInstallation(installationId); }

    public void insert(InstallationImage installationImage) { this.repository.insert(installationImage); }

    public void deleteAllInstallationImageRelations() {
        Log.i("InstallationImageViewModel", "Deleting all InstallationImageRelations from DB!");
        this.repository.deleteAllInstallationImageRelations();
    }
}
