package ch.hslu.appe.reminder.genius.Models;

import androidx.annotation.NonNull;

public class SearchContact {
    public final String title;
    public final String content;

    public SearchContact(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Title: %s, Content %s", title, content);
    }
}