package com.example.streethustling.bookstore3;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by StreetHustling on 12/2/15.
 */
public class BookDetailsFragment extends Fragment {

    Context activity;
    TextView bkTitle;
    TextView bkAuthor;
    TextView bkISBN;
    TextView bkPrice;
    TextView bkYear;
    TextView bkDescription;
    HashMap<String, String> singleBookData;
    public static final String BOOK_DETAILS = "book_details";

    public static BookDetailsFragment newInstance(HashMap<String, String> bookDetails) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(BOOK_DETAILS, bookDetails);
        fragment.setArguments(args);

        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        activity = getContext();
        Bundle data =  this.getArguments();
        if(data != null){
            singleBookData = (HashMap) data.getSerializable(BOOK_DETAILS);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_details_fragment, container, false);
        bkTitle = (TextView) view.findViewById(R.id.bookTitle);
        bkAuthor = (TextView) view.findViewById(R.id.bookAuthor);
        bkISBN = (TextView) view.findViewById(R.id.bookISBN);
        bkPrice = (TextView) view.findViewById(R.id.bookPrice);
        bkYear = (TextView) view.findViewById(R.id.bookYr);
        bkDescription = (TextView) view.findViewById(R.id.descText);

        bkTitle.setText(singleBookData.get(BooksListFragment.BOOK_TITLE));
        bkAuthor.setText(singleBookData.get(BooksListFragment.BOOK_AUTHOR));
        bkPrice.setText("$ "+ singleBookData.get(BooksListFragment.BOOK_PRICE));
        bkISBN.setText("ISBN: " + singleBookData.get(BooksListFragment.BOOK_ISBN));
        bkYear.setText("Written In: " + singleBookData.get(BooksListFragment.BOOK_YEAR));
        bkDescription.setText( singleBookData.get(BooksListFragment.BOOK_DESCRIPTION));

        String author = singleBookData.get(BooksListFragment.BOOK_AUTHOR);
        Toast.makeText(getContext(), "position: " + author, Toast.LENGTH_SHORT).show();

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //getListView().setOnItemClickListener(this);
    }



}
