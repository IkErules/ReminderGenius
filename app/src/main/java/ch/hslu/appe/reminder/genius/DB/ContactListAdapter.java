package ch.hslu.appe.reminder.genius.DB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.R;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView contactItemViewName;
        private final TextView contactItemViewAddress;

        private ContactViewHolder(View itemView) {
            super(itemView);
            this.contactItemViewName = itemView.findViewById(R.id.contact_recycler_view_item_name);
            this.contactItemViewAddress = itemView.findViewById(R.id.contact_recycler_view_item_address);
        }
    }

    private final LayoutInflater mInflater;
    private List<Contact> contacts; // Cached copy of contacts

    public ContactListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.contact_list_recycler_view_item, parent,false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        if (contacts != null) {
            Contact current = contacts.get(position);
            holder.contactItemViewName.setText(current.getFirstName() + " " + current.getLastName());
            holder.contactItemViewAddress.setText(current.getStreet() + "\n" +
                    current.getZip() + " " + current.getCity() + "\n" +
                    current.getCity());
        } else {
            // Covers the case of data not being ready yet.
            holder.contactItemViewName.setText("No Contact");
        }
    }

    public void setContacts(List<Contact> contacts){
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // contacts has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (this.contacts != null)
            return this.contacts.size();
        else return 0;
    }
}