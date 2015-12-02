package com.example.streethustling.bookstore3;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by StreetHustling on 12/2/15.
 */
public class ReviewFragment extends Fragment {

    Context activity;
    public static final String BOOK_ID = "id";
    int bookId ;
    ExpandableListView exListView;
    ReviewListAdapter listAdapter;

    List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();


    public static final String REVIEWER = "reviewer";
    public static final String DATE = "date";
    public static final String REVIEW = "review";
    public static final String TAG_REVIEWS = "reviews";

    List<HashMap<String,String>> reviewList = new ArrayList<HashMap<String,String>>();


    public static ReviewFragment newInstance(int bookId) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putInt(BOOK_ID, bookId);
        fragment.setArguments(args);

        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        activity = getContext();
        Bundle data =  this.getArguments();
        if(data != null){
           bookId  = data.getInt(BOOK_ID);
        }
        getReviews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_list_fragment, container, false);
        exListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

        return view;
    }

    public void getReviews(){
        ReviewPageTask reviewPageTask = new ReviewPageTask();
        reviewPageTask.execute(new String[]{"http://50.63.128.135/~csashesi/class2016/" +
                "kenneth-mensah/pos/model/book.php?cmd=6&id="+bookId});
    }

    public void showList(){
        listAdapter = new ReviewListAdapter(getContext(), listDataHeader, listDataChild);
        exListView.setAdapter(listAdapter);
        //swipeRefreshLayout.setRefreshing(false);

    }

//    @Override
//    public void onRefresh() {
//        readWebpage();
//    }

    public class ReviewPageTask extends AsyncTask<String, Void, String> {

        public JSONArray books;

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection conn = null;
            for (String url : urls) {
                try {
                    URL theUrl = new URL(url);

                    conn = (HttpURLConnection) theUrl.openConnection();
                    System.out.println(theUrl);

                    InputStream content = new BufferedInputStream(conn.getInputStream());

                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        System.out.println(s);
                        response += s;
                    }
                    System.out.println(response);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    conn.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            parseJSONLocally(result);
            showList();
        }

        public void parseJSONLocally(String response) {
            System.out.println("inside parse local" + response);
            if (response != null) {
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    books = jsonObj.getJSONArray(TAG_REVIEWS);

                    for (int i = 0; i < books.length(); i++) {
                        JSONObject m = books.getJSONObject(i);

                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put(REVIEW, m.getString(REVIEW));
                        hm.put(BOOK_ID, m.getString(BOOK_ID));
                        hm.put(REVIEWER, m.getString(REVIEWER));
                        hm.put(DATE, m.getString(DATE));

                        listDataHeader.add(m.getString(REVIEWER));
                        List<String> review = new ArrayList<String>();

                        review.add( m.getString(DATE));
                        review.add( m.getString(REVIEW));

                        listDataChild.put(listDataHeader.get(i), review);

                        reviewList.add(hm);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
