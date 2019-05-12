package ch.hslu.appe.reminder.genius.DB.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.time.LocalDate;
import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Entity.Installation;

@Dao
public interface InstallationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Installation contact);

    @Update
    public void updateInstallations(Installation... installations);

    @Delete
    public void deleteInstallations(Installation... installations);

    @Query("DELETE FROM Installation")
    void deleteAllInstallations();

    @Query("SELECT * FROM Installation WHERE installationId = :id")
    public LiveData<Installation> findInstallationById(int id);

    @Query("SELECT * FROM Installation WHERE installationId = :id")
    public Installation findSingleInstallationById(int id);

    @Query("SELECT * FROM Installation WHERE productDetails LIKE :search ")
    public LiveData<List<Installation>> findInstallationsWithProductDetails(String search);

    @Query("SELECT * FROM Installation WHERE expireDate < :date")
    public LiveData<List<Installation>> findInstallationsByExpireDate(LocalDate date);

    @Query("SELECT * FROM Installation WHERE expireDate < :date")
    public List<Installation> findInstallationsByExpireDateSync(LocalDate date);

    @Query("SELECT * FROM Installation WHERE " +
            "productCategoryId = :prodCatId " +
            "AND contactId = :contactId " +
            "AND productDetails = :prodDetails " +
            "AND installationDate = :instDate " +
            "AND expireDate = :expireDate " +
            "AND serviceInterval = :serviceInterval " +
            "AND notes = :notes " +
            "AND notifyCustomerMail = :notCustMail " +
            "AND notifyCustomerSms = :notCustSms " +
            "AND notifyCreatorMail = :notCreatorMail " +
            "AND notifyCreatorSms = :notCreatorSms ")
    public LiveData<Installation> findInstallationByAllProperties(int prodCatId, int contactId, String prodDetails, LocalDate instDate,
                                                                        LocalDate expireDate, int serviceInterval, String notes,
                                                                        boolean notCustMail, boolean notCustSms, boolean notCreatorMail,
                                                                        boolean notCreatorSms);

    @Query("SELECT * from Installation ORDER BY expireDate ASC")
    public LiveData<List<Installation>> getAllInstallations();
}
