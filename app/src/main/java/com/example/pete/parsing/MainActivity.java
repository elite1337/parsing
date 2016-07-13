package com.example.pete.parsing;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    String string;

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String, String> hashMap;

    EditText editText;
    ImageButton imageButton;
    ListView listView;
    Adapter adapter = new Adapter(this, arrayList);

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);
        imageButton = (ImageButton)findViewById(R.id.imageButton);
        listView = (ListView)findViewById(R.id.listView);

        requestQueue = Volley.newRequestQueue(this);


        Log.d("thisarraylist4", arrayList+"");
        if(arrayList.size() > 0)
        {
            listView.setAdapter(adapter);
        }


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if(arrayList.size() >= 1)
                {
                    arrayList.clear();
                }

                setString(java.net.URLEncoder.encode(editText.getText().toString()));

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        "https://itunes.apple.com/search?term=" + string + "&mdeia=music",

                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    JSONArray jsonArray = response.getJSONArray("results");
//                                    Log.d("thisjsonarray", jsonArray+"");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObjectInner = jsonArray.getJSONObject(i);
//                                        Log.d("jsonobjectinner", jsonObjectInner+"");

                                        hashMap = new HashMap<>();

                                        Iterator<String> iterator = jsonObjectInner.keys();
                                        while (iterator.hasNext()) {
                                            String key = iterator.next();
                                            if (key.equals("artistName")) {
                                                String keyArtist = key;
                                                String valueArtist = jsonObjectInner.get(keyArtist).toString();

                                                hashMap.put(keyArtist, valueArtist);
                                            }
                                            if (key.equals("trackName")) {
                                                String keyTrack = key;
                                                String valueTrack = jsonObjectInner.get(keyTrack).toString();

                                                hashMap.put(keyTrack, valueTrack);
                                            }
                                            if (key.equals("artworkUrl100")) {
                                                String keyPic = key;
                                                String valuePic = jsonObjectInner.get(keyPic).toString();

                                                hashMap.put(keyPic, valuePic);
                                            }
                                            if (key.equals("previewUrl")) {
                                                String keyPre = key;
                                                String valuePre = jsonObjectInner.get(keyPre).toString();

                                                hashMap.put(keyPre, valuePre);
                                            }
                                        }
                                        arrayList.add(hashMap);
                                        Log.d("thisarraylist1", arrayList+"");
                                    }
                                } catch (Exception exception) {
                                    Log.d("exception", exception+"");
                                }
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );
                requestQueue.add(jsonObjectRequest);

//                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                editText.setText("");
                listView.setAdapter(adapter);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String artistName = arrayList.get(position).get("artistName");
                String trackName = arrayList.get(position).get("trackName");
                String artworkUrl100 = arrayList.get(position).get("artworkUrl100");
                String previewurl = arrayList.get(position).get("previewUrl");

                Intent intent = new Intent(getApplicationContext(), PreviewActivity.class);
                intent.putExtra("artistName", artistName);
                intent.putExtra("trackName", trackName);
                intent.putExtra("previewurl", previewurl);
                intent.putExtra("artworkUrl100", artworkUrl100);
                startActivity(intent);
            }
        });



//        Rectangle rectangle = new Rectangle();
//
//        rectangle.one = 11;
//        rectangle.two = 22;
//
//        Square square = new Square();
//
//        square.one = 11;
//        square.two = 22;
//
//        Log.d("searchthis", square.two+"");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        Log.d("thisarraylist2", arrayList+"");
        outState.putSerializable("thisArrayList", arrayList);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null)
        {
            arrayList = (ArrayList<HashMap<String,String>>) savedInstanceState.getSerializable("thisArrayList");
        }
        Log.d("thisarraylist3", arrayList+"");
    }

}
