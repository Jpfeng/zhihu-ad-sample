package com.jpfeng.adv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Jpfeng
 * E-mail: fengjup@live.com
 * Date: 2019/3/6
 */
public class ListAdapter extends RecyclerView.Adapter {

    private final static int TYPE_PARALLAX = 1;
    private final static int TYPE_RIPPLE = 2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        switch (viewType) {
            case TYPE_PARALLAX:
                return new ParallaxItemHolder(LayoutInflater.from(context)
                        .inflate(R.layout.item_parallax, parent, false));

            case TYPE_RIPPLE:
                return new RippleItemHolder(LayoutInflater.from(context)
                        .inflate(R.layout.item_ripple, parent, false));

            default:
                return new NormalItemHolder(LayoutInflater.from(context)
                        .inflate(R.layout.item_normal, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalItemHolder) {
            ((NormalItemHolder) holder).tvTitle.setText(
                    String.format(Locale.getDefault(), "如何评价XXX…##%d##？", position));

        } else if (holder instanceof ParallaxItemHolder) {
            ((ParallaxItemHolder) holder).pvParallax.setImageResource(R.drawable.img_parallax);

        } else if (holder instanceof RippleItemHolder) {
            ((RippleItemHolder) holder).rvRipple.setDrawables(R.drawable.ripple_top, R.drawable.ripple_bottom);
        }
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 5:
                return TYPE_PARALLAX;
            case 10:
                return TYPE_RIPPLE;
            default:
                return super.getItemViewType(position);
        }
    }

    class NormalItemHolder extends RecyclerView.ViewHolder {

        final TextView tvTitle;

        NormalItemHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
        }
    }

    class ParallaxItemHolder extends RecyclerView.ViewHolder {

        final ParallaxView pvParallax;

        ParallaxItemHolder(@NonNull View itemView) {
            super(itemView);
            pvParallax = itemView.findViewById(R.id.pv_item_parallax);
            // 固定大小，长宽比
            ViewGroup.LayoutParams layoutParams = pvParallax.getLayoutParams();
            int width = itemView.getResources().getDisplayMetrics().widthPixels;
            layoutParams.height = width / 2;
            pvParallax.setLayoutParams(layoutParams);
        }
    }

    class RippleItemHolder extends RecyclerView.ViewHolder {

        final RippleView rvRipple;

        RippleItemHolder(@NonNull View itemView) {
            super(itemView);
            rvRipple = itemView.findViewById(R.id.rv_item_ripple);
            // 固定大小，长宽比
            ViewGroup.LayoutParams layoutParams = rvRipple.getLayoutParams();
            int width = itemView.getResources().getDisplayMetrics().widthPixels;
            layoutParams.height = width / 2;
            rvRipple.setLayoutParams(layoutParams);
        }
    }
}
