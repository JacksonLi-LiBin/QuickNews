package com.lb.quicknews.adapter;

import java.util.List;

import com.lb.quicknews.R;
import com.lb.quicknews.wedget.city.ContactItemInterface;
import com.lb.quicknews.wedget.city.ContactListAdapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class CityAdapter extends ContactListAdapter {

	public CityAdapter(Context _context, int _resource, List<ContactItemInterface> _items) {
		super(_context, _resource, _items);
	}

	@Override
	public void populateDataForRow(View parentView, ContactItemInterface item, int position) {
		View infoView = parentView.findViewById(R.id.infoRowContainer);
		TextView nickNameView = (TextView) infoView.findViewById(R.id.cityName);
		nickNameView.setText(item.getDisplayInfo());
	}
}
