package ch.hslu.appe.reminder.genius.Adapters;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.hslu.appe.reminder.genius.Activities.SearchContactResultActivity;
import ch.hslu.appe.reminder.genius.Models.SearchContact;
import ch.hslu.appe.reminder.genius.R;
import android.content.Intent;


public class SearchContactAdapter extends RecyclerView.Adapter<SearchContactAdapter.MyViewHolder> {

    private List<SearchContact> searchContactList;
    private SearchContactResultActivity context;

    public SearchContactAdapter(List<SearchContact> searchContactList, SearchContactResultActivity context) {
        this.searchContactList = searchContactList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, content;
        RelativeLayout parentLayout;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.search_contact_item_title);
            content = view.findViewById(R.id.search_contact_item_content);
            parentLayout = view.findViewById(R.id.search_contact_parent_layout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_contact_list_recycler_view_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SearchContact searchContact = searchContactList.get(position);
        holder.title.setText(searchContact.getTitle());
        holder.content.setText(searchContact.getContent());
        holder.parentLayout.setOnClickListener(view -> {
            SearchContact contact = searchContactList.get(position);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("search.contact.to.import", contact);
            context.setResult(Activity.RESULT_OK, returnIntent);
            context.finish();
        });
    }

    @Override
    public int getItemCount() {
        return searchContactList.size();
    }
}