package com.ns.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.view.roundedimageview.RoundedImageView;
import com.netoperation.model.PersonaliseDetails;
import com.netoperation.model.PersonaliseModel;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.callbacks.THPPersonaliseItemClickListener;
import com.ns.thpremium.R;
import com.ns.utils.PicassoUtil;
import com.ns.utils.ResUtil;


public class AuthorsRecyclerAdapter extends BaseRecyclerViewAdapter {

    PersonaliseDetails details;
    THPPersonaliseItemClickListener itemClickListener;
    String mFrom;

    public AuthorsRecyclerAdapter(PersonaliseDetails details, String mFrom, THPPersonaliseItemClickListener itemClickListener) {
        this.details = details;
        this.itemClickListener=itemClickListener;
        this.mFrom=mFrom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.authors_row_items, viewGroup, false);
        AuthorsRecyclerAdapter.AuthorHolder holder = new AuthorsRecyclerAdapter.AuthorHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        AuthorHolder holder = (AuthorHolder) viewHolder;
        PersonaliseModel model = details.getValues().get(i);
        PicassoUtil.loadImage(holder.image_author.getContext(), holder.image_author, model.getImage());
        holder.tv_author_name.setText(ResUtil.capitalizeFirstLetter(model.getTitle().toLowerCase()));

        if(model.isSelected()) {
            holder.imageview_click.setImageResource(R.drawable.ic_tik_1);
        } else  {
            holder.imageview_click.setImageResource(R.drawable.ic_plus_1);
        }
            holder.imageview_click.setOnClickListener(v -> {
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

    private static class AuthorHolder extends RecyclerView.ViewHolder{

        RoundedImageView image_author;
        TextView tv_author_name;
        ImageView imageview_click;

        public AuthorHolder(@NonNull View itemView) {
            super(itemView);
            image_author = itemView.findViewById(R.id.image_author);
            tv_author_name = itemView.findViewById(R.id.tv_author_name);
            imageview_click = itemView.findViewById(R.id.imageview_click);
        }
    }
}
