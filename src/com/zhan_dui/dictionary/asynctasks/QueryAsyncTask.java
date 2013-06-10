package com.zhan_dui.dictionary.asynctasks;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.viewpagerindicator.PageIndicator;
import com.zhan_dui.dictionary.datacenter.DictionaryDataCenter;
import com.zhan_dui.dictionary.datacenter.QueryProcessor;
import com.zhan_dui.dictionary.datacenter.QueryProcessor.QueryResult;

/**
 * 负责查询的AsyncTask
 * 
 * @author xuanqinanhai
 * 
 */
public class QueryAsyncTask extends AsyncTask<Void, Object, Boolean> {
	private PageIndicator mPageIndicator;
	private PagerAdapter mPagerAdapter;
	private DictionaryDataCenter mDataCenter;
	private String mQueryWord;
	private Context mContext;

	private static QueryProcessor mQueryProcessor;

	public QueryAsyncTask(Context context, PageIndicator pageIndicator,
			PagerAdapter adapter, String queryWord) {
		mContext = context;
		mPagerAdapter = adapter;
		mQueryWord = queryWord;
		mPageIndicator = pageIndicator;
		mDataCenter = DictionaryDataCenter.instance(context);
		mQueryProcessor = QueryProcessor.instance(mContext);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		Boolean result = true;

		for (int i = 0; i < mQueryProcessor.getDictionaryUsingCount(); i++) {
			try {
				QueryResult queryResult = mQueryProcessor.query(mContext,
						mQueryWord, i);
				publishProgress(queryResult.getDictionaryName(),
						queryResult.getDictionaryView());
			} catch (ParserConfigurationException e) {
				result = false;
				e.printStackTrace();
			} catch (SAXException e) {
				result = false;
				e.printStackTrace();
			} catch (IOException e) {
				result = false;
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	protected void onProgressUpdate(Object... values) {
		super.onProgressUpdate(values);
		mDataCenter.addDictionaryView((String) values[0], (View) values[1]);
		mPagerAdapter.notifyDataSetChanged();
		mPageIndicator.notifyDataSetChanged();
	}

}
