package nl.erc69.barapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class ConfigureOrderMenu extends Fragment {


    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new CategoryTab(this.getChildFragmentManager()));

        mTabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        mTabLayout.setupWithViewPager(mViewPager,true);
    }

    /*@Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add(0,R.id.menu_update_category,0,R.string.menu_update_category);
        menuItem.setIcon(R.drawable.ic_edit);

        MenuItemCompat.setShowAsAction(menuItem,MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);

        super.onPrepareOptionsMenu(menu);
    }*/

    public void dataSetChanged() {
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    public int getCurrentCategory(){
        return mViewPager.getCurrentItem();
    }

    public class CategoryTab extends FragmentStatePagerAdapter{

        public CategoryTab(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return Category.CATEGORIES_POS.size() + 1;
        }

        @Override
        public Fragment getItem(int position) {
            if (position<Category.CATEGORIES_POS.size())
                return ConfigureOrderGridFragment.newInstance(position);
            else
                return PlusOneFragment.newInstance(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position<Category.CATEGORIES_POS.size())
                return Category.CATEGORIES_POS.get(position).getName();
            else
                return "New Category";
        }
    }


}
