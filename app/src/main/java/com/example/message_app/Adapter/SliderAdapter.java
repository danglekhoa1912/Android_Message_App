package com.example.message_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.message_app.R;
import com.example.message_app.SliderItem;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{
    private List<SliderItem> sliderItemList;
    private ViewPager2 viewPager2;

    public SliderAdapter(List<SliderItem> sliderItemList, ViewPager2 viewPager2) {
        this.sliderItemList = sliderItemList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.image_item_container, parent,false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItemList.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView imageView;
        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageslide);
        }
        void setImage(SliderItem sliderItem){
            imageView.setImageResource(sliderItem.getImage());
        }
    }
}
