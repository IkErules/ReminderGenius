package ch.hslu.appe.reminder.genius.DB.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.NO_ACTION;

@Entity(tableName = "installationimage",
        primaryKeys = {"installationIdFk", "imageIdFk"},
        foreignKeys = {
                @ForeignKey(entity = Installation.class,
                        parentColumns = "installationId",
                        childColumns = "installationIdFk",
                        onDelete = CASCADE),
                @ForeignKey(entity = Image.class,
                        parentColumns = "imageId",
                        childColumns = "imageIdFk",
                        onDelete = CASCADE)
        })
public class InstallationImage implements Parcelable {
    @NonNull
    public int installationIdFk;
    @NonNull
    private int imageIdFk;

    public InstallationImage(int installationIdFk,
                             int imageIdFk) {

        this.installationIdFk = installationIdFk;
        this.imageIdFk = imageIdFk;
    }

    public int getInstallationIdFk() {
        return this.installationIdFk;
    }

    public int getImageIdFk() {
        return this.imageIdFk;
    }


    public void setInstallationIdFk(int installationId) {
        this.installationIdFk = installationId;
    }

    public void setImageIdFk(int imageId) {
        this.imageIdFk = imageId;
    }

    @Override
    public String toString() {
        return String.format("InstallationImage[installationId: %s, imageId: %s]",
                this.getInstallationIdFk(), this.getImageIdFk());
    }

    public static final Creator<InstallationImage> CREATOR = new Creator<InstallationImage>() {
        @Override
        public InstallationImage createFromParcel(Parcel in) {
            return new InstallationImage(in);
        }

        @Override
        public InstallationImage[] newArray(int size) {
            return new InstallationImage[size];
        }
    };

    // Convert Parcel to Contact --> Deserialize
    protected InstallationImage(Parcel in) {
        this.installationIdFk = in.readInt();
        this.imageIdFk = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    // Serialize Contact to Parcel
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getInstallationIdFk());
        dest.writeInt(this.getImageIdFk());
    }
}
