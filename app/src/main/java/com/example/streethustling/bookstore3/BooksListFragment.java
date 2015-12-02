package com.example.streethustling.bookstore3;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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
 * Created by StreetHustling on 12/1/15.
 */
public class BooksListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OnItemSelectedListener listener;
    public static final String BOOK_YEAR = "year";
    public static final String BOOK_DESCRIPTION = "description";
    public static final String BOOK_AUTHOR = "author";
    public static final String BOOK_ISBN = "isbn";
    public static final String BOOK_TITLE = "title";
    public static final String TAG_BOOKS = "books";
    public static final String BOOK_PRICE = "price";
    public static final String BOOK_ID = "id";

    List<HashMap<String,String>> bookList = new ArrayList<HashMap<String,String>>();


    private static final String ARG_SECTION_NUMBER = "section_number";


    public static BooksListFragment newInstance(int sectionNumber) {
        BooksListFragment fragment = new BooksListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        readWebpage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_list_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(android.R.id.list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        readWebpage();
                                    }
                                }
        );
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        HashMap<String, String> singleBook = bookList.get(position);

                        String author = singleBook.get(BOOK_AUTHOR);

                        Toast.makeText(getContext(), "position: " + author, Toast.LENGTH_SHORT).show();
                        updateDetail(singleBook );
                    }
                })
        );
    }


    public interface OnItemSelectedListener {
        public void onRssItemSelected( HashMap<String, String> bookData);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement BookListFragment.OnItemSelectedListener");
        }
    }

    // triggers update of the details fragment
    public void updateDetail(HashMap<String, String> book) {
        // create fake data
        String newTime = String.valueOf(System.currentTimeMillis());
        // send data to activity
        listener.onRssItemSelected(book);
    }



    public void readWebpage(){
        DownloadWebPageTask bookPageTask = new DownloadWebPageTask();
        bookPageTask.execute(new String[]{"http://50.63.128.135/~csashesi/class2016/" +
                "kenneth-mensah/pos/model/book.php?cmd=9"});
    }

    public void showList(){
        BookListAdapter sAdapter = new BookListAdapter(bookList, getActivity());
        mRecyclerView.setAdapter(sAdapter);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onRefresh() {
        readWebpage();
    }


    public class DownloadWebPageTask extends AsyncTask<String, Void, String> {



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

                    books = jsonObj.getJSONArray(TAG_BOOKS);

                    for (int i = 0; i < books.length(); i++) {
                        JSONObject m = books.getJSONObject(i);

                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put(BOOK_TITLE, m.getString(BOOK_TITLE));
                        hm.put(BOOK_ID, m.getString(BOOK_ID));
                        hm.put(BOOK_PRICE, m.getString(BOOK_PRICE));
                        hm.put(BOOK_AUTHOR, m.getString(BOOK_AUTHOR));
                        hm.put(BOOK_YEAR, m.getString(BOOK_YEAR));
                        hm.put(BOOK_DESCRIPTION, m.getString(BOOK_DESCRIPTION));
                        hm.put(BOOK_ISBN, m.getString(BOOK_ISBN));
                        bookList.add(hm);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
