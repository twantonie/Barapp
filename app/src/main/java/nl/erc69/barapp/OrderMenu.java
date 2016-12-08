/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.erc69.barapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;


public class OrderMenu extends Fragment {

    private ViewPager mViewPager;

    public static final String SHOW_FAB = "showFab";
    private boolean showFAB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle!=null)
            showFAB = bundle.getBoolean(SHOW_FAB,false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_menu, container, false);

        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.receipt);
        if (showFAB){
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    OrderReceipt orderReceipt = new OrderReceipt();
                    transaction.add(R.id.order_grid_fragment,orderReceipt,MainActivity.RECEIPT_TAG);
                    transaction.addToBackStack("Receipt");
                    transaction.commit();
                }
            });
        }else {
            floatingActionButton.setVisibility(View.GONE);
        }


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new CategoryTab(this.getChildFragmentManager()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void dataSetChanged() {
        CategoryTab categoryTab = (CategoryTab) mViewPager.getAdapter();
        categoryTab.notifyDataSetChanged();
    }

    public class CategoryTab extends FragmentPagerAdapter {

        public CategoryTab(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return Category.CATEGORIES_POS.size();
        }

        @Override
        public Fragment getItem(int position) {
            return OrderGridFragment.newInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return Category.CATEGORIES_POS.get(position).getName();
        }
    }



}

