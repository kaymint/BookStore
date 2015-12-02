package com.example.streethustling.bookstore3;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by StreetHustling on 12/1/15.
 */
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder>{
    List<HashMap<String, String>> data;
    Context context;

    public static final String BOOK_YEAR = "year";
    public static final String BOOK_DESCRIPTION = "description";
    public static final String BOOK_AUTHOR = "author";
    public static final String BOOK_TITLE = "title";
    public static final String TAG_BOOKS = "books";
    public static final String BOOK_PRICE = "price";
    public static final String BOOK_ISBN = "isbn";

    public BookListAdapter(List<HashMap<String, String>> data, Context context){
        super();
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.book_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        HashMap<String, String> hashMap = data.get(i);
        viewHolder.bookTitle.setText(hashMap.get(BOOK_TITLE));
        viewHolder.bookAuthor.setText(hashMap.get(BOOK_AUTHOR));
        viewHolder.imgThumbnail.setImageResource(R.drawable.image_thumb);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgThumbnail;
        public TextView bookTitle;
        public TextView bookAuthor;


        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            bookTitle = (TextView)itemView.findViewById(R.id.tv_nature);
            bookAuthor = (TextView)itemView.findViewById(R.id.tv_des_nature);
        }
    }
}
