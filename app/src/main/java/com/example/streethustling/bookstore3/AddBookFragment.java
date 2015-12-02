package com.example.streethustling.bookstore3;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by StreetHustling on 12/2/15.
 */
public class AddBookFragment extends Fragment implements View.OnClickListener{




        Button add;
        EditText bTitle, bPrice, bAuthor, bIsbn, bDesc, bYear;
        String title, price, author, isbn, desc, year ;
        Context activity;

        public static AddBookFragment newInstance(int sectionNumber) {
            AddBookFragment fragment = new AddBookFragment();
            Bundle args = new Bundle();

            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            activity = getContext();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.add_book_fragment, container, false);
            add = (Button) view.findViewById(R.id.addBookBtn);
            bTitle = (EditText) view.findViewById(R.id.addTitle);
            bPrice = (EditText) view.findViewById(R.id.addPrice);
            bYear = (EditText) view.findViewById(R.id.addYear);
            bAuthor = (EditText) view.findViewById(R.id.addAuthor);
            bIsbn = (EditText) view.findViewById(R.id.addISBN);
            bDesc = (EditText) view.findViewById(R.id.addDescription);

            add.setOnClickListener(this);

            return view;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            //getListView().setOnItemClickListener(this);
        }

        public interface AddMealListener{
            public void onMealAdded();
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

            // This makes sure that the container activity has implemented
            // the callback interface. If not, it throws an exception
            try {

            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement OnHeadlineSelectedListener");
            }
        }


        public void sendBookInfo(View view) {
            AddBookTask task = new AddBookTask();
            task.execute(new String[]{"http://cs.ashesi.edu.gh/~csashesi/class2016/kenneth-mensah/pos/model/book.php?cmd=1&"+
                    "title="+title+"&author="+author+"&price="+price+"&yr="+year+"&desc="+desc+"&isbn="+isbn});
        }

        public boolean priceValCheck(){
            price = bPrice.getText().toString();

            if(price.length() == 0){
                bPrice.setError("Nothing Entered");
                return false;
            }else {
                return true;
            }
        }

        public boolean titleValCheck(){
            title = bTitle.getText().toString();
            if(title.length() == 0){
                bTitle.setError("Nothing Entered");
                return false;
            }else{
                title = title.replace(" ", "%20");
                return true;
            }
        }

        public boolean authorValCheck(){
            author = bAuthor.getText().toString();
            if(title.length() == 0){
                bAuthor.setError("Nothing Entered");
                return false;
            }else{
                author = author.replace(" ", "%20");
                return true;
            }
        }


        @Override
        public void onClick(View v) {

            if(v == add){
                if(priceValCheck() == true && titleValCheck() == true && authorValCheck() == true) {
                    year = bYear.getText().toString();
                    isbn = bIsbn.getText().toString();
                    desc = bDesc.getText().toString();
                    desc = desc.replace(" ", "%20");
                    sendBookInfo(v);
                }
            }
        }

        public void sentNotification(){
            Toast toast;
            toast = Toast.makeText(activity, "book added", Toast.LENGTH_SHORT);
            toast.show();
        }

        public void refreshList(){
            bTitle.setText("");
            bDesc.setText("");
            bYear.setText("");
            bIsbn.setText("");
            bAuthor.setText("");
            bPrice.setText("");
            title = price = year = isbn = desc = author = "";
        }


        public class AddBookTask extends AsyncTask<String, Void, String> {

            private AddBookTask(){
                dialog = new ProgressDialog(getContext());
            }

            private ProgressDialog dialog;

            protected void onPreExecute() {
                this.dialog.setMessage("Progress start");
                this.dialog.show();
            }


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
                //show meal added and redirect to fragment 2
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                sentNotification();
                refreshList();
            }
        }
    }


