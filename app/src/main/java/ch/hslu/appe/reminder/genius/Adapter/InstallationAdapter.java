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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import ch.hslu.appe.reminder.genius.Activity.AddInstallationActivity;
import ch.hslu.appe.reminder.genius.Activity.ShowInstallationActivity;
import ch.hslu.appe.reminder.genius.DB.Entity.Contact;
import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.DB.Entity.ProductCategory;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.ContactViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationViewModel;
import ch.hslu.appe.reminder.genius.ViewModel.ProductCategoryViewModel;

import static ch.hslu.appe.reminder.genius.Activity.AddInstallationActivity.INSTALLATION_TO_EDIT;
import static ch.hslu.appe.reminder.genius.Activity.ShowInstallationActivity.SHOW_INSTALLATION;

public class InstallationAdapter extends RecyclerView.Adapter<InstallationAdapter.InstallationViewHolder> implements Filterable {

    private final LayoutInflater mInflater;
    private List<Installation> installations; // Cached copy of installations
    private List<Installation> installationsFull;

    private Activity context;

    private InstallationViewModel installationViewModel;
    private ProductCategoryViewModel productCategoryViewModel;
    private ContactViewModel contactViewModel;

    private Installation mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    public InstallationAdapter(Activity context, InstallationViewModel installationViewModel, ProductCategoryViewModel productCategoryViewModel, ContactViewModel contactViewModel) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;

        this.installationViewModel = installationViewModel;
        this.productCategoryViewModel = productCategoryViewModel;
        this.contactViewModel = contactViewModel;
    }

    @Override
    public InstallationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.installation_list_recycler_view_item, parent,false);
        return new InstallationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InstallationViewHolder holder, int position) {
        if (installations != null) {
            Installation current = installations.get(position);
            ProductCategory productCategory = this.productCategoryViewModel.getSingleProductCategoryByIdSync(current.getProductCategoryId());
            Contact contact = this.contactViewModel.getSingleContactByIdSync(current.getContactId());

            holder.installationItemViewProduct.setText(productCategory.getCategoryName());
            holder.installationItemViewCustomer.setText(contact.getFirstName() + " " + contact.getLastName());
            holder.installationItemViewInstallationDetails.setText("Installationsdatum: "  + current.getFriendlyInstallationDateAsString() + "\n" +
                    "Nächster Service: " + current.getFriendlyExpireDateAsString());

            // start AddContact Activity if an item on the RecyclerView is clicked.
            holder.parentLayout.setOnClickListener(view -> {
                Installation installation = installations.get(position);
                Log.d("InstallationAdapter", "Starting ShowInstallation Activity in order to show installation: " + installations.toString());
                Intent showInstallationIntent = new Intent(context, ShowInstallationActivity.class);
                showInstallationIntent.putExtra(SHOW_INSTALLATION, installation);
                context.startActivity(showInstallationIntent);
            });

            holder.parentLayout.setOnLongClickListener(view -> {
                Installation installation = installations.get(position);
                Log.d("InstallationAdapter", "Starting AddInstallation Activity in order to edit installation: " + installation.toString());
                Intent editInstallationIntent = new Intent(context, AddInstallationActivity.class);
                editInstallationIntent.putExtra(INSTALLATION_TO_EDIT, installation);
                context.startActivity(editInstallationIntent);
                return true;
            });
        } else {
            // Covers the case of data not being ready yet.
            holder.installationItemViewProduct.setText("No Product");
        }
    }

    public Context getContext() {
        return context;
    }

    public void setInstallations(List<Installation> installations){
        if ((installations != null) & (!(installations.isEmpty()))) {
            Log.d("InstallationAdapter", "Setting installations: " + installations.get(0).toString());
            this.installations = installations;
            this.installationsFull = new ArrayList<>(this.installations);
            notifyDataSetChanged();
        } else {
            Log.w("InstallationAdapter", "No Installations available.");
        }
    }

    // getItemCount() is called many times, and when it is first called,
    // installations has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (this.installations != null)
            return this.installations.size();
        else return 0;
    }

    public List<Installation> getInstallations() {
        return this.installations;
    }

    protected List<Installation> filterInstallations(String filterString) {
        List<Installation> filteredList = new ArrayList<>();

        for (Installation installation : this.installationsFull) {
            ProductCategory productCategory = this.productCategoryViewModel.getSingleProductCategoryByIdSync(installation.getProductCategoryId());
            Contact contact = this.contactViewModel.getSingleContactByIdSync(installation.getContactId());

            if ((installation.getProductDetails().toLowerCase().contains(filterString)) | (installation.getNotes().toLowerCase().contains(filterString))) {
                filteredList.add(installation);
            } else if ((productCategory.getCategoryName().toLowerCase().contains(filterString)) | (productCategory.getDescription().toLowerCase().contains(filterString))) {
                filteredList.add(installation);
            } else if (contact.getFormattedAddressWithName().toLowerCase().contains(filterString)) {
                filteredList.add(installation);
            } else if ((contact.getLastName() + " " + contact.getFirstName()).toLowerCase().contains(filterString)) {
                // Add Contact even if user is searching for lastname + firstname instead of firstname + lastname
                filteredList.add(installation);
            }
        }

        return filteredList;
    }

    @Override
    public Filter getFilter() {
        return installationFilter;
    }

    private Filter installationFilter = new Filter() {
        @Override
        // Automatically performed in the Background
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Installation> filteredInstallations = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredInstallations.addAll(installationsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                filteredInstallations.addAll(filterInstallations(filterPattern));
            }

            FilterResults results = new FilterResults();
            results.values = filteredInstallations;

            return results;
        }

        @Override
        // Results from Filtering will be returned to this method from performFiltering
        protected void publishResults(CharSequence constraint, FilterResults results) {
            installations.clear();
            installations.addAll((List) results.values);
            // Notify observers that the List has changed.
            notifyDataSetChanged();
        }
    };


    public void deleteItem(int position) {
        mRecentlyDeletedItem = installations.get(position);
        mRecentlyDeletedItemPosition = position;
        installationViewModel.delete(mRecentlyDeletedItem);
        installations.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = context.findViewById(R.id.installation_constraint_layout);
        Snackbar snackbar = Snackbar.make(view, R.string.main_snack_bar_installation_deleted, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.main_snack_bar_installation_delete_undo, v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        installationViewModel.insert(mRecentlyDeletedItem);
        installations.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    public static class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

        private InstallationAdapter mAdapter;
        private Drawable icon;
        private final ColorDrawable background;

        public SwipeToDeleteCallback(InstallationAdapter adapter) {
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


    class InstallationViewHolder extends RecyclerView.ViewHolder {
        private final TextView installationItemViewProduct;
        private final TextView installationItemViewCustomer;
        private final TextView installationItemViewInstallationDetails;
        private final RelativeLayout parentLayout;

        private InstallationViewHolder(View itemView) {
            super(itemView);
            this.installationItemViewProduct = itemView.findViewById(R.id.installation_recycler_view_item_product_name);
            this.installationItemViewCustomer = itemView.findViewById(R.id.installation_recycler_view_item_contact);
            this.installationItemViewInstallationDetails = itemView.findViewById(R.id.installation_recycler_view_item_installation_details);
            this.parentLayout = itemView.findViewById(R.id.installation_foreground_parent_layout);
        }
    }
}