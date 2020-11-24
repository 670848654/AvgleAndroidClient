package pl.avgle.videos.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.HashMap;

import pl.avgle.videos.main.view.fragment.FavoriteChannelFragment;
import pl.avgle.videos.main.view.fragment.FavoriteTagsFragment;
import pl.avgle.videos.main.view.fragment.FavoriteVideosFragment;

public class FavoriteFragmentAdapter extends FragmentStatePagerAdapter {
    private int num;
    private HashMap<Integer, Fragment> mFragmentHashMap = new HashMap<>();

    public FavoriteFragmentAdapter(FragmentManager fm, int num) {
        super(fm);
        this.num = num;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return createFragment(position);
    }

    @Override
    public int getCount() {
        return num;
    }

    private Fragment createFragment(int pos) {
        Fragment fragment = mFragmentHashMap.get(pos);
        if (fragment == null) {
            switch (pos) {
                case 0:
                    fragment = new FavoriteChannelFragment();
                    break;
                case 1:
                    fragment = new FavoriteTagsFragment();
                    break;
                case 2:
                    fragment = new FavoriteVideosFragment();
                    break;
            }
            mFragmentHashMap.put(pos, fragment);
        }
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
    }
}
