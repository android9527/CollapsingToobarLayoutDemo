/*
 * Copyright 2016 "Henry Tao <hi@henrytao.me>"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.henrytao.smoothappbarlayoutdemo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzw.ctlsdemo.R;

import me.henrytao.recyclerview.SimpleRecyclerViewAdapter;
import me.henrytao.recyclerview.holder.HeaderHolder;
import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import me.henrytao.smoothappbarlayoutdemo.apdater.DynamicAdapter;
import me.henrytao.smoothappbarlayoutdemo.util.Utils;

public class SmoothSwipeRefreshLayoutActivity extends AppCompatActivity
{

    RecyclerView vRecyclerView;

    SwipeRefreshLayout vSwipeRefreshLayout;

    Toolbar vToolbar;

    private DynamicAdapter<String> mAdapter;

    private Runnable mCallback;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smooth_swipe_refresh_layout);
        vRecyclerView = (RecyclerView) findViewById(R.id.list);
        vSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        vToolbar = (Toolbar) findViewById(R.id.toolbar);
        vToolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        mAdapter = new DynamicAdapter<>(Utils.getSampleData());
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

        vRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        vRecyclerView.setAdapter(adapter);


        // set progress view offset
        int actionBarSize = 60;
        int progressViewStart = getResources().getDimensionPixelSize(R.dimen.app_bar_height) -
                actionBarSize;
        int progressViewEnd = progressViewStart + (int) (actionBarSize * 1.5f);
        vSwipeRefreshLayout.setProgressViewOffset(true, progressViewStart, progressViewEnd);

        mHandler = new Handler();
        mCallback = new Runnable()
        {
            @Override
            public void run()
            {
                if (!isFinishing())
                {
                    vSwipeRefreshLayout.setRefreshing(false);
                }
            }
        };
        vSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                mHandler.postDelayed(mCallback, 2000);
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mHandler.removeCallbacks(mCallback);
        mCallback = null;
        mHandler = null;
    }
}
