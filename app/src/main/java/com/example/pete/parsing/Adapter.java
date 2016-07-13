package com.example.pete.parsing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Adapter extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> arrayList;
    MainActivity mainActivity;

    Adapter(Context context_inner, ArrayList<HashMap<String,String>> arrayList_inner)
    {
        this.context = context_inner;
        this.arrayList = arrayList_inner;
        this.mainActivity = (MainActivity)context_inner;
    }

    private static Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.single_row, null);

        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        TextView textView2 = (TextView) convertView.findViewById(R.id.textView3);

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        textView.setText(arrayList.get(position).get("artistName"));
        textView2.setText(arrayList.get(position).get("trackName"));

        new AsyncTask<String, Void, Bitmap>()
        {
            @Override
            protected Bitmap doInBackground(String... params)
            {
                String url = params[0];
                return getBitmapFromURL(url);
            }

            @Override
            protected void onPostExecute(Bitmap result)
            {
                imageView. setImageBitmap(result);
                super.onPostExecute(result);
            }
        }.execute(arrayList.get(position).get("artworkUrl100"));

        return convertView;
    }
}

