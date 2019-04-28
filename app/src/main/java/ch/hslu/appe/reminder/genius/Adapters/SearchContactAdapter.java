package ch.hslu.appe.reminder.genius.Adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.hslu.appe.reminder.genius.Models.SearchContact;
import ch.hslu.appe.reminder.genius.R;

public class SearchContactAdapter extends RecyclerView.Adapter<SearchContactAdapter.MyViewHolder> {

    private List<SearchContact> searchContactList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, content;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.search_contact_item_title);
            content = view.findViewById(R.id.search_contact_item_content);
        }
    }


    public SearchContactAdapter(List<SearchContact> searchContactList) {
        this.searchContactList = searchContactList;
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
    }

    @Override
    public int getItemCount() {
        return searchContactList.size();
    }
}