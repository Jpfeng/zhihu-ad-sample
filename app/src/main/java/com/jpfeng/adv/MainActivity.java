package com.jpfeng.adv;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView list = findViewById(R.id.rv_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration decor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decor.setDrawable(getResources().getDrawable(R.drawable.shape_item_divider));
        list.addItemDecoration(decor);
        list.setLayoutManager(layoutManager);

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int first = layoutManager.findFirstVisibleItemPosition();
                int last = layoutManager.findLastVisibleItemPosition();

                // 遍历当前可见
                for (int i = first; i <= last; i++) {
                    RecyclerView.ViewHolder holder = list.findViewHolderForAdapterPosition(i);
                    // 找到需要操作的 item
                    if (holder instanceof ListAdapter.ParallaxItemHolder) {
                        int itemTop = holder.itemView.getTop();
                        int listH = layoutManager.getHeight();
                        int parallaxTop = ((ListAdapter.ParallaxItemHolder) holder).pvParallax.getTop();
                        int parallaxH = ((ListAdapter.ParallaxItemHolder) holder).pvParallax.getMeasuredHeight();

                        // 计算偏移比例
                        float offset = (float) (itemTop + parallaxTop) / (listH - parallaxH);
                        // 限制在 0.0 ~ 1.0 之间
                        offset = Math.max(Math.min(offset, 1f), 0f);
                        ((ListAdapter.ParallaxItemHolder) holder).pvParallax.setOffset(offset);

                    } else if (holder instanceof ListAdapter.RippleItemHolder) {
                        int itemTop = holder.itemView.getTop();
                        int listH = layoutManager.getHeight();
                        int parallaxTop = ((ListAdapter.RippleItemHolder) holder).rvRipple.getTop();
                        int parallaxH = ((ListAdapter.RippleItemHolder) holder).rvRipple.getMeasuredHeight();

                        // 计算偏移比例
                        float offset = 1 - (float) (itemTop + parallaxTop) / (listH - parallaxH);
                        // 添加限制，只在 0.2 ~ 0.8 之间产生效果
                        if (offset <= 0.2) {
                            offset = 0;
                        } else if (offset >= 0.8) {
                            offset = 1;
                        } else {
                            offset = (offset - 0.2f) / 0.6f;
                        }
                        ((ListAdapter.RippleItemHolder) holder).rvRipple.setOffset(offset);
                    }
                }
            }
        });

        list.setAdapter(new ListAdapter());
    }
}
