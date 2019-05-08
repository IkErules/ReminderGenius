package ch.hslu.appe.reminder.genius.DB.Entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Entity(tableName = "image")
public class Image implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int imageId;
    @NonNull
    private String path;
    private String description;
    @Ignore
    private Bitmap img;

    public Image(String path,
                 String description) {

        this.path = path;
        this.description = description;
    }

    public int getImageId(){return this.imageId;}
    public String getPath() {return this.path;}
    public String getDescription(){return this.description;}

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setPath(@NonNull String path) {
        this.path = path;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public Bitmap loadImageFromStorage() {
        this.img = null;

        try {
            File f = new File(this.getPath());
            this.img = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return this.img;
    }

    @Override
    public String toString() {
        return String.format("Image[imageId: %s, path: %s, description: %s]",
                this.getImageId(), this.getPath(), this.getDescription());
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    // Convert Parcel to Contact --> Deserialize
    protected Image(Parcel in) {
        this.imageId = in.readInt();
        this.path = in.readString();
        this.description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    // Serialize Contact to Parcel
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.getImageId());
        dest.writeString(this.getPath());
        dest.writeString(this.getDescription());
    }
}
