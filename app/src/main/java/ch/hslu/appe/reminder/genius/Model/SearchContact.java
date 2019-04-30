package ch.hslu.appe.reminder.genius.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SearchContact implements Parcelable {
    public String title;
    public String content;

    public SearchContact(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Title: %s, Content %s", title, content);
    }

    public static final Creator<SearchContact> CREATOR = new Creator<SearchContact>() {
        @Override
        public SearchContact createFromParcel(Parcel in) {
            return new SearchContact(in);
        }

        @Override
        public SearchContact[] newArray(int size) {
            return new SearchContact[size];
        }
    };

    protected SearchContact(Parcel in) {
        title = in.readString();
        content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
    }
}