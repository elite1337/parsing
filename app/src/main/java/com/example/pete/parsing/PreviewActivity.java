package com.example.pete.parsing;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PreviewActivity extends AppCompatActivity {

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    String artistName;
    String trackName;

    Intent intent;
    String previewurl;
    String artworkUrl100;
    ImageView imageView;
    TextView artistTextView;
    TextView trackTextView;

    MediaPlayer mediaPlayer = new MediaPlayer();

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        intent = getIntent();
        setArtistName(intent.getStringExtra("artistName"));
        trackName = intent.getStringExtra("trackName");
        previewurl = intent.getStringExtra("previewurl");
        artworkUrl100 = intent.getStringExtra("artworkUrl100");

        artistTextView = (TextView)findViewById(R.id.textView4);
        trackTextView = (TextView)findViewById(R.id.textView6);
        imageView = (ImageView)findViewById(R.id.imageView2);

        artistTextView.setText(artistName);
        trackTextView.setText(trackName);

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
        }.execute(artworkUrl100);

        try
        {
            mediaPlayer.setDataSource(previewurl);
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.setLooping(true);
                }
            });
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mediaPlayer.release();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        Log.d("thisplayerint1", mediaPlayer.getCurrentPosition()+"");
        outState.putInt("mediaPlayerInt", mediaPlayer.getCurrentPosition());
//        outState.putString("artistName", getArtistName());

        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null)
        {
            Log.d("thisplayerint2", savedInstanceState.getInt("mediaPlayerInt")+"");
            mediaPlayer.seekTo(savedInstanceState.getInt("mediaPlayerInt"));
//            setArtistName(artistName = savedInstanceState.getString("artistName"));
        }
    }


//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        setCuttentPosition(mediaPlayer.getCurrentPosition());
//
//        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
//        {
//            setContentView(R.layout.activity_preview);
//        }
//        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
//        {
//            setContentView(R.layout.activity_preview);
//        }
//
//        artistTextView.setText(getArtistName());
//
//        Log.d("thisartistname", getArtistName());
//    }

}
