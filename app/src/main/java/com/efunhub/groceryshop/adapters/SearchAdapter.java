package com.efunhub.groceryshop.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.efunhub.groceryshop.R;
import com.efunhub.groceryshop.model.ModelAllSearchItem;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends ArrayAdapter<ModelAllSearchItem> {

    private Context context;
    private int resourceId;
    private List<ModelAllSearchItem> items, tempItems, suggestions;

    public SearchAdapter(@NonNull Context context, int resourceId, List<ModelAllSearchItem> items) {
        super(context, resourceId, items);
        this.items = items;
        this.context = context;
        this.resourceId = resourceId;
        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resourceId, parent, false);
            }
            ModelAllSearchItem modelAllSearchItem = getItem(position);

            TextView name = (TextView) view.findViewById(R.id.tvItem);
            name.setTextColor(context.getResources().getColor(R.color.colorTextBlack));
            name.setText(modelAllSearchItem.getMainCategoryEname());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Nullable
    @Override
    public ModelAllSearchItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return fruitFilter;
    }

    private Filter fruitFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            ModelAllSearchItem modelAllSearchItem = (ModelAllSearchItem) resultValue;
            return modelAllSearchItem.getMainCategoryEname();
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();
                for (ModelAllSearchItem modelAllSearchItem : tempItems) {
                    if (modelAllSearchItem.getMainCategoryEname().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(modelAllSearchItem);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<ModelAllSearchItem> tempValues = (ArrayList<ModelAllSearchItem>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (ModelAllSearchItem modelAllSearchItem : tempValues) {
                    add(modelAllSearchItem);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };
}
