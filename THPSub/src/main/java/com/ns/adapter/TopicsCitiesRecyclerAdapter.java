package com.ns.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.model.PersonaliseDetails;
import com.netoperation.model.PersonaliseModel;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.callbacks.THPPersonaliseItemClickListener;
import com.ns.thpremium.R;
import com.ns.utils.GlideUtil;
import com.ns.utils.ResUtil;
import com.ns.view.text.CustomTextView;

public class TopicsCitiesRecyclerAdapter extends BaseRecyclerViewAdapter {
    PersonaliseDetails details;
    THPPersonaliseItemClickListener itemClickListener;
    String imageUrl="https://subscription.thehindu.com/";
    String mFrom;

    public TopicsCitiesRecyclerAdapter(PersonaliseDetails details, String mFrom, THPPersonaliseItemClickListener itemClickListener) {
        this.details = details;
        this.itemClickListener=itemClickListener;
        this.mFrom=mFrom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.topics_row_item, viewGroup, false);
        TopicsCitiesRecyclerAdapter.TopicsCitiesHolder holder = new TopicsCitiesRecyclerAdapter.TopicsCitiesHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        TopicsCitiesHolder holder = (TopicsCitiesHolder) viewHolder;
        GlideUtil.loadImage(holder.image.getContext(), holder.image, (imageUrl+details.getValues().get(i).getImage()));
        holder.tv_topics_cities.setText(ResUtil.capitalizeFirstLetter(details.getValues().get(i).getTitle().toLowerCase()));
        PersonaliseModel model = details.getValues().get(i);
        if(model.isSelected()) {
            holder.btn_personalise.setImageResource(R.drawable.ic_tik);
        } else  {
            holder.btn_personalise.setImageResource(R.drawable.thp_ic_plus);
        }

        holder.itemView.setOnClickListener(v -> {
            model.setSelected(!model.isSelected());
            if (itemClickListener != null) {
                itemClickListener.personaliseItemClick(details.getValues().get(i), mFrom);
            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return details.getValues().size();
    }

    private static class TopicsCitiesHolder extends RecyclerView.ViewHolder{
        ImageView image;
        ImageView btn_personalise;
        CustomTextView tv_topics_cities;

        public TopicsCitiesHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            btn_personalise = itemView.findViewById(R.id.btn_personalise);
            tv_topics_cities = itemView.findViewById(R.id.tv_topics_cities);
        }
    }
}
