package com.qihoo.feiyang.share;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ContentPagerAdapter extends FragmentPagerAdapter {

	private Fragment[] fragments;

	public ContentPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		fragments = new Fragment[3];
		fragments[0] = new FilesFragment(context);
		fragments[1] = new FindFragment(context);
		fragments[2] = new HistoryFragment(context);
	}

	@Override
	public Fragment getItem(int position) {
		return fragments[position];
	}

	@Override
	public int getCount() {
		return fragments.length;
	}
}