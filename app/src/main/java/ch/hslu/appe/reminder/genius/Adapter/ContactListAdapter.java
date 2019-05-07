package ch.hslu.appe.reminder.genius.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import ch.hslu.appe.reminder.genius.Activity.AddContactActivity;
import ch.hslu.appe.reminder.genius.Activity.ShowContactActivity;
import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.ContactViewModel;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> implements Filterable {

    public static final String EDIT_CONTACT = "contact.to.edit";
    public static final String SHOW_CONTACT = "contact.to.show";
    private final LayoutInflater mInflater;

    private List<Contact> contacts; // Cached copy of contacts
    private List<Contact> contactsFull;

    private Activity context;
    private ContactViewModel contactViewModel;
    private Contact mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    public ContactListAdapter(Activity context, ContactViewModel contactViewModel) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.contactViewModel = contactViewModel;
    }

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

            // start AddContact Activity if an item on the RecyclerView is clicked.
            holder.parentLayout.setOnClickListener(view -> {
                Contact contact = contacts.get(position);
                Log.d("ContactListAdapter", "Starting ShowContact Activity in order to show contact: " + contact.toString());
                Intent showContactIntent = new Intent(context, ShowContactActivity.class);
                showContactIntent.putExtra(SHOW_CONTACT, contact);
                context.startActivity(showContactIntent);
            });

            holder.parentLayout.setOnLongClickListener(view -> {
                Contact contact = contacts.get(position);
                Log.d("ContactListAdapter", "Starting AddContact Activity in order to edit contact: " + contact.toString());
                Intent editContactIntent = new Intent(context, AddContactActivity.class);
                editContactIntent.putExtra(EDIT_CONTACT, contact);
                context.startActivity(editContactIntent);
                return true;
            });
        } else {
            // Covers the case of data not being ready yet.
            holder.contactItemViewName.setText("No Contact");
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContacts(List<Contact> contacts){
        this.contacts = contacts;
        this.contactsFull = new ArrayList<>(this.contacts);
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

    public void deleteItem(int position) {
        mRecentlyDeletedItem = contacts.get(position);
        mRecentlyDeletedItemPosition = position;
        contactViewModel.delete(mRecentlyDeletedItem);
        contacts.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = context.findViewById(R.id.contact_constraint_layout);
        Snackbar snackbar = Snackbar.make(view, R.string.contact_snack_bar_deleted, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.contact_snack_bar_undo_delete, v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        contactViewModel.insert(mRecentlyDeletedItem);
        contacts.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    protected List<Contact> filterContacts(String filterString) {
        List<Contact> filteredList = new ArrayList<>();

        for (Contact contact : this.contactsFull) {
            if (contact.getFormattedAddressWithName().toLowerCase().contains(filterString)) {
                filteredList.add(contact);
            } else if ((contact.getLastName() + " " + contact.getFirstName()).toLowerCase().contains(filterString)) {
                // Add Contact even if user is searching for lastname + firstname instead of firstname + lastname
                filteredList.add(contact);
            }
        }

        return filteredList;
    }

    @Override
    public Filter getFilter() {
        return contactFilter;
    }

    private Filter contactFilter = new Filter() {
        @Override
        // Automatically performed in the Background
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contact> filteredContacts = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredContacts.addAll(contactsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                filteredContacts.addAll(filterContacts(filterPattern));
            }

            FilterResults results = new FilterResults();
            results.values = filteredContacts;

            return results;
        }

        @Override
        // Results from Filtering will be returned to this method from performFiltering
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contacts.clear();
            contacts.addAll((List) results.values);
            // Notify observers that the List has changed.
            notifyDataSetChanged();
        }
    };

    public static class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

        private ContactListAdapter mAdapter;
        private Drawable icon;
        private final ColorDrawable background;

        public SwipeToDeleteCallback(ContactListAdapter adapter) {
            super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            mAdapter = adapter;
            icon = ContextCompat.getDrawable(mAdapter.getContext(),
                    R.drawable.ic_delete);

            background = new ColorDrawable(Color.RED);
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX,
                    dY, actionState, isCurrentlyActive);
            View itemView = viewHolder.itemView;
            int backgroundCornerOffset = 20;

            if (dX > 0) { // Swiping to the right
                background.setBounds(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                        itemView.getBottom());

            } else if (dX < 0) { // Swiping to the left
                background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                        itemView.getTop(), itemView.getRight(), itemView.getBottom());
            } else { // view is unSwiped
                background.setBounds(0, 0, 0, 0);
            }
            background.draw(c);

            int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
            int iconBottom = iconTop + icon.getIntrinsicHeight();

            if (dX > 0) { // Swiping to the right
                int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                int iconRight = itemView.getLeft() + iconMargin;
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getLeft(), itemView.getTop(),
                        itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                        itemView.getBottom());
            } else if (dX < 0) { // Swiping to the left
                int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                int iconRight = itemView.getRight() - iconMargin;
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                        itemView.getTop(), itemView.getRight(), itemView.getBottom());
            } else { // view is unSwiped
                background.setBounds(0, 0, 0, 0);
            }

            background.draw(c);
            icon.draw(c);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            mAdapter.deleteItem(position);
        }
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {
        private final TextView contactItemViewName;
        private final TextView contactItemViewAddress;
        private final RelativeLayout parentLayout;

        private ContactViewHolder(View itemView) {
            super(itemView);
            this.contactItemViewName = itemView.findViewById(R.id.contact_recycler_view_item_name);
            this.contactItemViewAddress = itemView.findViewById(R.id.contact_recycler_view_item_address);
            this.parentLayout = itemView.findViewById(R.id.contact_foreground_parent_layout);
        }
    }
}