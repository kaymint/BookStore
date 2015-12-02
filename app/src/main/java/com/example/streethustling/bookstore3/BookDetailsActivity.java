package com.example.streethustling.bookstore3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class BookDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "url";
    TextView bkTitle;
    TextView bkAuthor;
    TextView bkISBN;
    TextView bkPrice;
    TextView bkYear;
    TextView bkDescription;
    HashMap<String, String> singleBookData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bkTitle = (TextView) findViewById(R.id.bookTitle);
        bkAuthor = (TextView) findViewById(R.id.bookAuthor);
        bkISBN = (TextView) findViewById(R.id.bookISBN);
        bkPrice = (TextView) findViewById(R.id.bookPrice);
        bkYear = (TextView) findViewById(R.id.bookYr);
        bkDescription = (TextView) findViewById(R.id.descText);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            singleBookData = (HashMap) extras.getSerializable("HashMap");

            bkTitle.setText(singleBookData.get(BooksListFragment.BOOK_TITLE));
            bkAuthor.setText(singleBookData.get(BooksListFragment.BOOK_AUTHOR));
            bkPrice.setText("$ "+ singleBookData.get(BooksListFragment.BOOK_PRICE));
            bkISBN.setText("ISBN: " + singleBookData.get(BooksListFragment.BOOK_ISBN));
            bkYear.setText("Written In: " + singleBookData.get(BooksListFragment.BOOK_YEAR));
            bkDescription.setText( singleBookData.get(BooksListFragment.BOOK_DESCRIPTION));


            String author = singleBookData.get(BooksListFragment.BOOK_AUTHOR);
            Toast.makeText(getApplicationContext(), "position: " + author, Toast.LENGTH_SHORT).show();

        }
    }

}
