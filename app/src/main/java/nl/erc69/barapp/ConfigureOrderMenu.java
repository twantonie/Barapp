package nl.erc69.barapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigureOrderMenu extends Fragment {


    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_menu, container, false);
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
            return Category.CATEGORIES.size() + 1;
        }

        @Override
        public Fragment getItem(int position) {
            if (position<Category.CATEGORIES.size())
                return OrderGridFragment.newInstance(position);
            else
                return PlusOneFragment.newInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            if (position<Category.CATEGORIES.size())
                return Category.CATEGORIES.get(position).getName();
            else
                return "New Category";
        }
    }


}
