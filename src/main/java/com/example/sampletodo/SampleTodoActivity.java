
package com.example.sampletodo;

import java.util.HashMap;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectResource;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

public class SampleTodoActivity  extends RoboFragmentActivity
	implements TabHost.OnTabChangeListener {

	@InjectResource(R.string.all_todos) String allIndicator;
	@InjectResource(R.string.active_todos) String activeIndicator;
	@InjectResource(R.string.completed_todos) String completedIndicator;
		
	private TabHost mTabHost;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
	private TabInfo mLastTab = null;
	
	private class TabInfo {
		private String tag;
		private Class<?> clss;
		private Bundle args;
		private Fragment fragment;
		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}
	}
	
	class TabFactory implements TabContentFactory {
		private final Context mContext;
		
		/**
		 * @param context
		 */
		public TabFactory(Context context) {
			mContext = context;
		}
		
	    /** (non-Javadoc)
	     * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
	     */
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    // Step 1: Inflate layout
		setContentView(R.layout.sample_todo);
		// Step 2: Setup TabHost
		initialiseTabHost(savedInstanceState);
		if (savedInstanceState != null) {
			//set the tab as per the saved state
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
	}
	
	/** (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab selected
		super.onSaveInstanceState(outState);
	}
	
	/**
	 * Step 2: Setup TabHost
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;
		SampleTodoActivity.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator(allIndicator),
				(tabInfo = new TabInfo("Tab1", Tab1Fragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		SampleTodoActivity.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator(activeIndicator),
				(tabInfo = new TabInfo("Tab2", Tab2Fragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		SampleTodoActivity.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator(completedIndicator),
				(tabInfo = new TabInfo("Tab3", Tab3Fragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		// Default to first tab
		this.onTabChanged("Tab1");
		mTabHost.setOnTabChangedListener(this);
	}
	
	private static void addTab(SampleTodoActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec,
			TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(activity.new TabFactory(activity));
		String tag = tabSpec.getTag();
		
	    // Check to see if we already have a fragment for this tab, probably
	    // from a previously saved state.  If so, deactivate it, because our
	    // initial state is that a tab isn't shown.
		tabInfo.fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
		if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
			FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
			ft.detach(tabInfo.fragment);
			ft.commit();
			activity.getSupportFragmentManager().executePendingTransactions();
		}
		
		tabHost.addTab(tabSpec);
	}
	
	public void onTabChanged(String tag) {
		TabInfo newTab = (TabInfo) this.mapTabInfo.get(tag);
		if (mLastTab != newTab) {
			FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
			if (mLastTab != null) {
				if (mLastTab.fragment != null) {
					ft.detach(mLastTab.fragment);
				}
			}
			if (newTab != null) {
				if (newTab.fragment == null) {
					newTab.fragment = Fragment.instantiate(this, newTab.clss.getName(), newTab.args);
					ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
				} else {
					ft.attach(newTab.fragment);
				}
			}
	
			mLastTab = newTab;
			ft.commit();
			this.getSupportFragmentManager().executePendingTransactions();
		}
	}
}
