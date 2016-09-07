package com.rxjava.frpsearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rxjava.frpsearch.model.Issues;

import java.util.ArrayList;
import java.util.List;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder>{

    List<Issues> items;

    public IssueAdapter() {
        items = new ArrayList<>();
    }

    public void addItems(List<Issues> issues) {
        items.clear();
        items.addAll(issues);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.issue_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Issues issue = items.get(position);
        String text = "Issue #" + issue.number;
        holder.issueId.setText(text);
        holder.issueTitle.setText(issue.title);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView issueId;
        public TextView issueTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            issueId = (TextView) itemView.findViewById(R.id.issue_id);
            issueTitle = (TextView) itemView.findViewById(R.id.issue_title);
        }
    }
}
