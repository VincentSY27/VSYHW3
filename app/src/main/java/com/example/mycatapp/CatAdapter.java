package com.example.mycatapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder> {
    private List<Cat> catsToAdapt;
    private List<Cat> catsToAdaptFull;

    private Context context;
    private List<Breeds> breedsToAdapt;

    public void setData(List<Breeds> breedsToAdapt) {
        this.breedsToAdapt = breedsToAdapt;
    }

    public void CatAdapter(List<Cat> catsToAdapt, Context context) {
        this.catsToAdapt = catsToAdapt;
        catsToAdaptFull = new ArrayList<>(catsToAdapt);
        this.context = context;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat, parent, false);

        CatViewHolder catViewHolder = new CatViewHolder(view);
        return catViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        final Cat catAtPosition = catsToAdapt.get(position);

        holder.bind(catAtPosition);
    }

    @Override
    public int getItemCount() {
        return catsToAdapt.size();
    }

    //@Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Cat> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll( catsToAdaptFull );
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Cat cat : catsToAdaptFull){
                    if(cat.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(cat);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            catsToAdapt.clear();
            catsToAdapt.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public static class CatViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView nameTextView;
        public TextView temperamentTextView;

        public CatViewHolder(View v) {
            super(v);
            view = v;
            nameTextView = v.findViewById(R.id.cat_name);
            temperamentTextView = v.findViewById(R.id.cat_temperament);
        }


        public void bind(final Cat cat) {
            nameTextView.setText(cat.getName());
            temperamentTextView.setText(cat.getTemperament());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();

                    Intent intent = new Intent(context, CatDetailActivity.class);

                    intent.putExtra("name", cat.getName());
                    String imageID = cat.getId();
                    intent.putExtra("imageId", imageID);
                    intent.putExtra("id", cat.getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
