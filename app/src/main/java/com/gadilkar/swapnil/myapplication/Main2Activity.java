package com.gadilkar.swapnil.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;



import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity {


    TextView mTextViewResult;
    String imgLink;
    ImageView imageView;
    String imgID;
    TextView textView;
    EditText editText;
    Button btnSearch;
    Button btnPost;
    String imgTitle="";
    String commentLink="";
    TextView txtName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        imageView = findViewById(R.id.imageView);
        mTextViewResult = findViewById(R.id.tvJson);
        editText = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
       // listView = findViewById(R.id.listView);
        textView = findViewById(R.id.txtView2);
        txtName = findViewById(R.id.textName);
        btnPost = findViewById(R.id.btnPost);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgID = editText.getText().toString().trim();
                //setUrl(imgID);

                OkHttpClient client = new OkHttpClient();



                String url = "https://api.imgur.com/3/gallery/"+imgID;//getUrl();

                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .addHeader("Authorization", "client-ID 25984232c47f751")
                        .addHeader("cache-control", "no-cache")
                        .build();
                        //.addHeader("Postman-Token", "16d67adb-7c01-4b30-a111-58f2bb038d86")

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {


                            final String myResponse = response.body().string();


                            JSONObject data=null;
                            try {
                                data = new JSONObject(myResponse);
                                JSONObject jsonObject = data.getJSONObject("data");
                                imgLink = String.valueOf(jsonObject.optString("link"));
                                imgTitle = String.valueOf(jsonObject.optString("title"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Main2Activity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Picasso.get().load(imgLink).into(imageView);
                                    mTextViewResult.setText(imgLink);
                                    txtName.setText("Title : "+imgTitle);

                                }
                            });
                            onClickSearch();

                        }

                    }
                });

                btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //onAddComment();
                        Toast.makeText(getApplicationContext(),"Message Completed",Toast.LENGTH_SHORT).show();
                        //onClickSearch();

                    }
                });
                }

        });
    }

    public void onClickSearch()
    {
        OkHttpClient client2 = new OkHttpClient();

        Request request2 = new Request.Builder()
                .url("https://api.imgur.com/3/gallery/"+imgID+"/comments")
                .get()
                .addHeader("Authorization", "client-ID 25984232c47f751")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "f304732e-378b-4c81-bd04-9801cf9c22ae")
                .build();
        client2.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    JSONObject data=null;
                    try {
                        data = new JSONObject(myResponse);
                        JSONArray jsonArray = data.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++)
                        {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            commentLink += "Posted By ->"+" "+jsonObject.optString("author")+":\n" + jsonObject.optString("comment")+"\n\n"+"----------------------------------------------------------------------\n";
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Main2Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Picasso.get().load(imgLink).into(imageView);
                            textView.setText(commentLink);


                        }
                    });
                }
            }
        });
    }

    public void onAddComment()
    {
        OkHttpClient client3 = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("image_id","OUHDm");
            jsonObject.put("comment","NICE");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON,jsonObject.toString());

        Request request3 = new Request.Builder()
                .url("https://api.imgur.com/3/comment/")
                .post(body)
                .addHeader("Authorization", "client-ID 25984232c47f751")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "76eb08d0-45b0-4e1d-a599-8961aaac18dd")
                .build();

        client3.newCall(request3).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(),"We Fail",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                {
                    Toast.makeText(getApplicationContext(),"Success in placiing Comment",Toast.LENGTH_SHORT).show();
                    if (response.isSuccessful()) {


                        final String myResponse = response.body().string();


                        JSONObject data=null;
                        try {
                            data = new JSONObject(myResponse);
                            String result = data.optString("success");
                            if(result=="true")
                            {
                                Toast.makeText(getApplicationContext(),"Success in placiing Comment",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Failed in placiing Comment",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });



    }


}

