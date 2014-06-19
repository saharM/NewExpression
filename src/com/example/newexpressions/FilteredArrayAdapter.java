package com.example.newexpressions;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

/**
* Arrayadapter (for Android) with text filtering for the use with a TextWatcher.
* Note: the objects in the List need a valid toString() method.
* @author Tobias Schürg
*
*/
 
 
public class FilteredArrayAdapter extends ArrayAdapter<Expression> {
 
	private List<Expression> objects;
	private Context context;
	private Filter filter;
	private ExpressionsDataSource datasource;
	 
	public FilteredArrayAdapter(Context context, int resourceId, List<Expression> objects, ExpressionsDataSource datasource) {
		super(context, resourceId, objects);
		this.context = context;
		this.objects = objects;
		this.datasource = datasource;
	}
	 
	@Override
	public int getCount() {
		return objects.size();
	}
	 
	@Override
	public Expression getItem(int position) {
		return objects.get(position);
	}
	 
	@Override
	public long getItemId(int position) {
		return objects.get(position).getId();
	}
	 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {	 
		// TODO: inflate your view HERE ...
		 
		//return super.getView(position, convertView, parent);
		
		View row;
		if (null == convertView) {
		row = mInflater.inflate(R.layout.list_item, null);
		} else {
		row = convertView;
		}
		TextView tv = (TextView) row.findViewById(android.R.id.text1);
		tv.setText(getItem(position));
		return row;
	}
	 
	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new AppFilter<Expression>(objects, datasource);
		return filter;
	}
	 
	/**
	* Class for filtering in Arraylist listview. Objects need a valid
	* 'toString()' method.
	*
	* @author Tobias Schürg inspired by Alxandr
	* (http://stackoverflow.com/a/2726348/570168)
	*
	*/
	private class AppFilter<T> extends Filter {
	 
		private List<T> sourceObjects;
		private ExpressionsDataSource datasource;
		 
		public AppFilter(List<T> objects, ExpressionsDataSource datasource) {
			this.datasource = datasource;
			sourceObjects = new ArrayList<T>();
			synchronized (this) {
				sourceObjects.addAll(objects);
			}
		}
		 
		@Override
		protected FilterResults performFiltering(CharSequence chars) {
			String filterSeq = chars.toString();//.toLowerCase();
			FilterResults result = new FilterResults();
			if (filterSeq != null && filterSeq.length() > 0) {
				//ArrayList<T> filter = new ArrayList<T>();
				List<Expression> filter = datasource.getLikeExpressions(filterSeq);
				result.count = filter.size();
				result.values = filter;
//				for (T object : sourceObjects) {
//					// the filtering itself:
//					if (object.toString().toLowerCase().contains(filterSeq))
//					filter.add(object);
//					}
//					result.count = filter.size();
//					result.values = filter;
//					} else {
//					// add all objects
//					synchronized (this) {
//					result.values = sourceObjects;
//					result.count = sourceObjects.size();
//				}
			}
			return result;
		}
		 
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			// NOTE: this function is *always* called from the UI thread.
			ArrayList<T> filtered = (ArrayList<T>) results.values;
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = filtered.size(); i < l; i++)
				add((Expression) filtered.get(i));
			notifyDataSetInvalidated();
		}
	}
	 
}