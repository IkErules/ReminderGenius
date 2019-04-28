package ch.hslu.appe.reminder.genius.DB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.hslu.appe.reminder.genius.DB.Entity.Customer;
import ch.hslu.appe.reminder.genius.R;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.CustomerViewHolder> {

    class CustomerViewHolder extends RecyclerView.ViewHolder {
        private final TextView customerItemView;

        private CustomerViewHolder(View itemView) {
            super(itemView);
            customerItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Customer> customers; // Cached copy of customers

    public CustomerListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new CustomerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        if (customers != null) {
            Customer current = customers.get(position);
            holder.customerItemView.setText(current.getFirstName() + " " + current.getLastName());
        } else {
            // Covers the case of data not being ready yet.
            holder.customerItemView.setText("No Customer");
        }
    }

    public void setCustomers(List<Customer> customers){
        customers = customers;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // customers has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (customers != null)
            return customers.size();
        else return 0;
    }
}