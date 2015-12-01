package com.lb.quicknews.activity;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import com.lb.quicknews.R;
import com.lb.quicknews.adapter.CityAdapter;
import com.lb.quicknews.bean.CityItem;
import com.lb.quicknews.dao.CityData;
import com.lb.quicknews.wedget.city.ContactItemInterface;
import com.lb.quicknews.wedget.city.ContactListViewImpl;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

@EActivity(R.layout.activity_choose_city)
public class ChooseCityActivity extends BaseActivity {
	private Context context_;
	@ViewById(R.id.listview)
	ContactListViewImpl listview;
	@ViewById(R.id.title)
	TextView mTitle;
	@ViewById(R.id.input_search_query)
	EditText searchBox;
	private String searchString;
	private CityAdapter adapter;
	private Object searchLock;
	boolean inSearchMode = false;
	private final static String TAG = "ChooseCityActivity";
	List<ContactItemInterface> contactList;
	List<ContactItemInterface> filterList;
	private SearchListTask curSearchTask = null;

	@AfterInject
	void init() {
		context_ = ChooseCityActivity.this;
		searchLock = new Object();
		filterList = new ArrayList<ContactItemInterface>();
		contactList = CityData.getSampleContactList();
		adapter = new CityAdapter(this, R.layout.city_item, contactList);
	}

	@AfterViews
	void initView() {
		listview.setFastScrollEnabled(true);
		listview.setAdapter(adapter);
		mTitle.setText("选择城市");
	}

	@ItemClick(R.id.listview)
	void OnItemClick(int position) {
		List<ContactItemInterface> searchList = inSearchMode ? filterList : contactList;
		Intent intent = new Intent();
		intent.putExtra("cityname", searchList.get(position).getDisplayInfo());
		this.setResult(1001, intent);
		this.finish();
	}

	@AfterTextChange(R.id.input_search_query)
	void afterTextChanged(Editable s) {
		searchString = searchBox.getText().toString().trim().toUpperCase();
		if (curSearchTask != null && curSearchTask.getStatus() != AsyncTask.Status.FINISHED) {
			try {
				curSearchTask.cancel(true);
			} catch (Exception e) {
			}
		}
		curSearchTask = new SearchListTask();
		curSearchTask.execute(searchString);
	}

	class SearchListTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			filterList.clear();
			String keyword = params[0];
			inSearchMode = (keyword.length() > 0);
			if (inSearchMode) {
				// get all the items matching this
				for (ContactItemInterface item : contactList) {
					CityItem contact = (CityItem) item;
					boolean isPinyin = contact.getFullName().toUpperCase().indexOf(keyword) > -1;
					boolean isChinese = contact.getNickName().indexOf(keyword) > -1;
					if (isPinyin || isChinese) {
						filterList.add(item);
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			synchronized (searchLock) {
				if (inSearchMode) {
					CityAdapter adapter = new CityAdapter(context_, R.layout.city_item, filterList);
					adapter.setInSearchMode(true);
					listview.setInSearchMode(true);
					listview.setAdapter(adapter);
				} else {
					CityAdapter adapter = new CityAdapter(context_, R.layout.city_item, contactList);
					adapter.setInSearchMode(false);
					listview.setInSearchMode(false);
					listview.setAdapter(adapter);
				}
			}
		}
	}
}
