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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import ch.hslu.appe.reminder.genius.Activity.AddInstallationActivity;
import ch.hslu.appe.reminder.genius.DB.Entity.Installation;
import ch.hslu.appe.reminder.genius.R;
import ch.hslu.appe.reminder.genius.ViewModel.InstallationViewModel;

import static ch.hslu.appe.reminder.genius.Activity.AddInstallationActivity.INSTALLATION_TO_EDIT;

public class InstallationAdapter extends RecyclerView.Adapter<InstallationAdapter.InstallationViewHolder> {

    private final LayoutInflater mInflater;
    private List<Installation> installations; // Cached copy of installations
    private Activity context;
    private InstallationViewModel installationViewModel;
    private Installation mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    public InstallationAdapter(Activity context, InstallationViewModel installationViewModel) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.installationViewModel = installationViewModel;
    }

    @Override
    public InstallationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.main_installation_recycler_view_item, parent,false);
        return new InstallationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InstallationViewHolder holder, int position) {
        if (installations != null) {
            Installation current = installations.get(position);
            holder.installationItemViewProduct.setText("Product ID: " + current.getProductCategoryId());
            holder.installationItemViewCustomer.setText("Customer ID: " + current.getContactId());

            /** start AddContact Activity if an item on the RecyclerView is clicked.
            holder.parentLayout.setOnClickListener(view -> {
                Installation contact = installations.get(position);
                Log.d("ContactListAdapter", "Starting ShowContact Activity in order to show contact: " + contact.toString());
                Intent showContactIntent = new Intent(context, ShowContactActivity.class);
                showContactIntent.putExtra(SHOW_CONTACT, contact);
                context.startActivity(showContactIntent);
            }); */

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
            holder.installationItemViewProduct.setText("No Contact");
        }
    }

    public Context getContext() {
        return context;
    }

    public void setInstallations(List<Installation> installations){
        this.installations = installations;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // installations has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (this.installations != null)
            return this.installations.size();
        else return 0;
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = installations.get(position);
        mRecentlyDeletedItemPosition = position;
        installationViewModel.delete(mRecentlyDeletedItem);
        installations.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = context.findViewById(R.id.main_constraint_layout);
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
        private final RelativeLayout parentLayout;

        private InstallationViewHolder(View itemView) {
            super(itemView);
            this.installationItemViewProduct = itemView.findViewById(R.id.main_installation_item_product_id);
            this.installationItemViewCustomer = itemView.findViewById(R.id.main_installation_item_contact_id);
            this.parentLayout = itemView.findViewById(R.id.main_installation_parent_layout);
        }
    }
}