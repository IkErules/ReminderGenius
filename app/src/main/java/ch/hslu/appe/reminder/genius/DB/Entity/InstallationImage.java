package ch.hslu.appe.reminder.genius.DB.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "installationimage",
        primaryKeys = {"installationId", "imageId"},
        foreignKeys = {
                @ForeignKey(entity = Installation.class,
                        parentColumns = "installationId",
                        childColumns = "installationId"),
                @ForeignKey(entity = Image.class,
                        parentColumns = "imageId",
                        childColumns = "imageId")
        })
public class InstallationImage implements Parcelable {
    @NonNull
    public int installationId;
    @NonNull
    private int imageId;

    public InstallationImage(int installationId,
                             int imageId) {

        this.installationId = installationId;
        this.imageId = imageId;
    }

    public int getInstallationId() {
        return this.installationId;
    }

    public int getImageId() {
        return this.imageId;
    }


    public void setInstallationId(int installationId) {
        this.installationId = installationId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return String.format("InstallationImage[installationId: %s, imageId: %s]",
                this.getInstallationId(), this.getImageId());
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
        this.installationId = in.readInt();
        this.imageId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    // Serialize Contact to Parcel
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getInstallationId());
        dest.writeInt(this.getImageId());
    }
}
