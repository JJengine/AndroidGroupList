package com.jj.grouplist;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * AbsGroupAdapter provide a easy way to create a listview with many different
 * view type(each type as one group). Each group's view will be reused and send
 * to {@link AbsGroupAdapter#getChildView(View, int, int, int, ViewGroup)}. <br>
 * You can also call {@link AbsGroupAdapter#addStableView(View)} to add a single
 * view into the listview which will not be reused. <br>
 * 
 * After calling {@link ListView#setAdapter(ListAdapter)}, You must bind your
 * listview for adapter ( {@link AbsGroupAdapter#bindListView(ListView)}}). <br>
 * <br>
 * 
 * @author luozhiping <br>
 *         <br>
 */
public abstract class AbsGroupAdapter extends BaseAdapter {

	private static int sUnStableItemType = 0;
	private final static int STABLE_VIEW_ITEM_TYPE = -1;
	private List<GroupInfo> mGroupInfos = new ArrayList<GroupInfo>();
	private List<GroupChildInfo> mGroupChildInfos = new ArrayList<GroupChildInfo>();
	protected Context mContext;
	private ListView mListView;

	public AbsGroupAdapter(Context context) {
		mContext = context;
	}

	/** listview group method *****************************/

	/**
	 * add a group to listview, every child in the group can be scraped and
	 * reused.<br>
	 * call {@link AbsGroupAdapter#notifyDataSetChanged()} to apply change
	 * 
	 * @param childCount
	 *            this group's child count
	 * 
	 * @return the index of the group
	 */
	public int addGroup(int childCount) {
		return addGroup(childCount, mGroupInfos.size());
	}

	/**
	 * add a group to listview, every child in the group can be scraped and
	 * reused.<br>
	 * call {@link AbsGroupAdapter#notifyDataSetChanged()} to apply change
	 * 
	 * @param childCount
	 *            this group's child count
	 * @param groupIndex
	 * @return the index of the group
	 */
	public int addGroup(int childCount, int groupIndex) {
		if (childCount <= 0) {
			throw new IllegalArgumentException("childCount must > 0");
		}
		GroupInfo info = new GroupInfo();
		for (int i = 0; i < childCount; i++) {
			GroupChildInfo childInfo = new GroupChildInfo();
			childInfo.mItemType = sUnStableItemType;
			childInfo.mGroupIndex = groupIndex;
			childInfo.mChildIndex = i;
			info.mChildInfos.add(childInfo);
		}

		sUnStableItemType++;
		mGroupInfos.add(groupIndex, info);
		return groupIndex;
	}

	/**
	 * change groupcount<br>
	 * call {@link AbsGroupAdapter#notifyDataSetChanged()} to apply change
	 * 
	 * @param groupIndex
	 * @param newCount
	 */
	public void setGroupCount(int groupIndex, int childCount) {
		if (childCount <= 0) {
			throw new IllegalArgumentException("childCount must > 0");
		}
		GroupInfo groupInfo = mGroupInfos.get(groupIndex);
		GroupChildInfo tempChild = groupInfo.mChildInfos.get(0);
		groupInfo.mChildInfos.clear();
		for (int i = 0; i < childCount; i++) {
			GroupChildInfo childInfo = new GroupChildInfo();
			childInfo.mItemType = tempChild.mItemType;
			childInfo.mGroupIndex = groupIndex;
			childInfo.mChildIndex = i;
			groupInfo.mChildInfos.add(childInfo);
		}
	}

	/**
	 * remove group by index,the index of other groups after this group will
	 * minus 1<br>
	 * call {@link AbsGroupAdapter#notifyDataSetChanged()} to apply change
	 * 
	 * @param groupIndex
	 */
	public void removeGroup(int groupIndex) {
		mGroupInfos.remove(groupIndex);
	}

	/** stable view handle method ***********************************/

	/**
	 * add a stable view to list, this view will be one group and can not be
	 * scraped and reused.<br>
	 * call {@link AbsGroupAdapter#notifyDataSetChanged()} to apply change
	 * 
	 * @param view
	 * @return groupIndex
	 */
	public int addStableView(View view) {
		return addStableView(view, mGroupInfos.size());
	}
	
	/**
	 * add a stable view to list, this view will be one group and can not be
	 * scraped and reused.<br>
	 * call {@link AbsGroupAdapter#notifyDataSetChanged()} to apply change
	 * 
	 * @param view
	 * @return groupIndex
	 */
	public int addStableView(View view, int groupIndex) {
		if (view == null) {
			throw new NullPointerException("view can't be null");
		}
		GroupInfo info = new GroupInfo();
		info.mIsViewStable = true;
		GroupChildInfo childInfo = new GroupChildInfo();
		childInfo.mItemType = STABLE_VIEW_ITEM_TYPE;
		childInfo.mGroupIndex = groupIndex;
		childInfo.mChildIndex = 0;
		childInfo.mIsViewStable = true;
		childInfo.mViewHolder = view;
		info.mChildInfos.add(childInfo);
		mGroupInfos.add(groupIndex, info);
		return groupIndex;
	}

	/**
	 * remove stable view in listview <br>
	 * call {@link AbsGroupAdapter#notifyDataSetChanged()} to apply change
	 * 
	 * @param view
	 *            the view should be removed
	 * @return true if remove view success
	 */
	public boolean removeStableView(View view) {
		for (int i = 0; i < mGroupInfos.size(); i++) {
			GroupInfo groupInfo = mGroupInfos.get(i);
			if (groupInfo.mIsViewStable) {
				for (GroupChildInfo groupChildInfo : groupInfo.mChildInfos) {
					if (groupChildInfo.mViewHolder == view) {
						mGroupInfos.remove(i);
						return true;
					}
				}
			}
		}
		return false;
	}

	/** abstract method *****************************/

	/**
	 * use this method to create listItemView or reuse scrapedView
	 * 
	 * @param probableView
	 *            probableView provide a probable scrapedView to reuse.Note: You
	 *            should check that this view is non-null and of an appropriate
	 *            type (probable the right type in each group) before using.
	 * @param groupIndex
	 *            group index
	 * @param childIndex
	 *            child index in group
	 * @return
	 */
	public abstract View getChildView(View probableView, int groupIndex, int childIndex, int position,
			ViewGroup parent);

	/** innerclass ******************/

	/**
	 * Group's child Info
	 * 
	 * @author JJ
	 *
	 */
	class GroupChildInfo {
		private int mItemType;
		private boolean mIsViewStable = false;
		private View mViewHolder;
		private Integer mGroupIndex;
		private int mChildIndex;
	}

	/**
	 * Group Info
	 * 
	 * @author JJ
	 *
	 */
	class GroupInfo {
		private boolean mIsViewStable = false;
//		private Integer mGroupIndex;
		private List<GroupChildInfo> mChildInfos = new ArrayList<GroupChildInfo>();
	}

	/*******************************************
	 * framework method, Should not to modify them
	 ********************************************/

	/**
	 * bindListView must be called after
	 * {@link ListView#setAdapter(ListAdapter)}
	 * 
	 * @param listView
	 */
	public void bindListView(ListView listView) {
		mListView = listView;
	}

	@Override
	public int getCount() {
		return mGroupChildInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * 
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GroupChildInfo info = mGroupChildInfos.get(position);
		if (info.mIsViewStable) {
			return info.mViewHolder;
		} else {
			return getChildView(convertView, info.mGroupIndex, info.mChildIndex, position, parent);
		}
	}

	@Override
	public int getItemViewType(int position) {
		return mGroupChildInfos.get(position).mItemType;
	}

	@Override
	public int getViewTypeCount() {
		return sUnStableItemType < 1 ? 1 : sUnStableItemType;
	}

	@Override
	public void notifyDataSetChanged() {
		mGroupChildInfos.clear();
		for (int i = 0; i < mGroupInfos.size(); i++) {
			GroupInfo groupInfo = mGroupInfos.get(i);
			for (int j = 0; j < groupInfo.mChildInfos.size(); j++) {
				if (groupInfo.mChildInfos.get(j).mGroupIndex != i) {
					groupInfo.mChildInfos.get(j).mGroupIndex = i;
				} else {
					break;
				}
			}
			mGroupChildInfos.addAll(groupInfo.mChildInfos);
		}
		resetViewTypeCount();
		super.notifyDataSetChanged();
	}

	/** pivate method ********************************/

	/**
	 * use reflect to reset RecycleBin
	 */
	private void resetViewTypeCount() {
		Class clazz = mListView.getClass();
		for (; clazz != clazz.getClass(); clazz = clazz.getSuperclass()) {
			try {
				Field filed = clazz.getDeclaredField("mRecycler");
				filed.setAccessible(true);
				Method method = filed.get(mListView).getClass().getDeclaredMethod("setViewTypeCount", int.class);
				method.invoke(filed.get(mListView), getViewTypeCount());
				return;
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}
	
}
