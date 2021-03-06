package com.zzw.ctlsdemo;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import me.henrytao.recyclerview.SimpleRecyclerViewAdapter;
import me.henrytao.recyclerview.holder.HeaderHolder;
import me.henrytao.smoothappbarlayoutdemo.apdater.DynamicAdapter;
import me.henrytao.smoothappbarlayoutdemo.util.Utils;

public class ScrollingActivitySmooth extends AppCompatActivity
{

    private static final String TAG = "ScrollingActivity";

    private AppBarLayout appbar;
    private int verticalOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_recy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appbar = (AppBarLayout) findViewById(R.id.app_bar);

        final PtrClassicFrameLayout ptrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptr);

        ptrFrameLayout.setPtrHandler(new PtrHandler()
        {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header)
            {
                return verticalOffset >= 0 && PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame)
            {
                frame.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ptrFrameLayout.refreshComplete();
                    }
                }, 1000);
            }
        });
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
        {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
            {
                Log.e(TAG, "verticalOffset:" + verticalOffset);
                Log.e(TAG, "getTotalScrollRange():" + appbar.getTotalScrollRange());
                ScrollingActivitySmooth.this.verticalOffset = verticalOffset;
            }
        });

        Log.e(TAG, "getTotalScrollRange():" + appbar.getTotalScrollRange());


        RecyclerView vRecyclerView = (RecyclerView) findViewById(R.id.list);
        DynamicAdapter mAdapter = new DynamicAdapter<>(Utils.getSampleData());
        RecyclerView.Adapter adapter = new SimpleRecyclerViewAdapter(mAdapter)
        {
            @Override
            public RecyclerView.ViewHolder onCreateFooterViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup)
            {
                return null;
            }

            @Override
            public RecyclerView.ViewHolder onCreateHeaderViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup)
            {
                return new HeaderHolder(layoutInflater, viewGroup, R.layout.item_header_spacing);
            }
        };

        final LinearLayoutManager manager = new LinearLayoutManager(this);
        vRecyclerView.setLayoutManager(manager);
        vRecyclerView.setAdapter(adapter);
    }

}
