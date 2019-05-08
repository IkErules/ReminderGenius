package ch.hslu.appe.reminder.genius.ViewModel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;

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
        this.allInstallationImageRelations = this.repository.getAllInstallationImageRelations();
    }

    public LiveData<List<InstallationImage>> getAllInstallationImageRelations() { return this.allInstallationImageRelations; }

    public LiveData<List<Image>> getImagesForInstallation(int installationId) { return this.repository.getImagesForInstallation(installationId); }

    public void insert(InstallationImage installationImage) { this.repository.insert(installationImage); }

    public void deleteAllInstallationImageRelations() {
        Log.i("InstallationImageViewModel", "Deleting all InstallationImageRelations from DB!");
        this.repository.deleteAllInstallationImageRelations();
    }

    public void fullScreen(Window window) {

        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = window.getDecorView().getSystemUiVisibility();

        int newUiOptions = uiOptions;
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("ShowInstallationActivity", "Turning immersive mode mode off. ");
        } else {
            Log.i("ShowInstallationActivity", "Turning immersive mode mode on.");
        }

        // Navigation bar hiding:  Backwards compatible to ICS.
        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        // Status bar hiding: Backwards compatible to Jellybean
        newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        window.getDecorView().setSystemUiVisibility(newUiOptions);
        //END_INCLUDE (set_ui_flags)
    }
}
