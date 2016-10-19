package com.aang.aplikasigempa;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aang.aplikasigempa.adapter.DataGempaHolder;
import com.aang.aplikasigempa.adapter.ReycleViewAdapter;
import com.aang.aplikasigempa.api.ApiInterface;
import com.aang.aplikasigempa.model.Data;
import com.aang.aplikasigempa.model.Gempa;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        progressBar = (ProgressBar) findViewById(R.id.progreeBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        GetDataGempa();
    }

    private  void loading(boolean status){
        progressBar.setVisibility(status ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(status ? View.GONE : View.VISIBLE);
    }

    private void  GetDataGempa(){
        loading(true);

        ApiInterface apiInterface =ApiInterface.Factory.create(context);
        Call<Data> getData = apiInterface.getDataGempa();

        getData.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Data resData = response.body();

                if(resData.getStatus().equals("Succes")){
                    ReycleViewAdapter adapter = new ReycleViewAdapter <Gempa, DataGempaHolder>(R.layout.item_list,
                            DataGempaHolder.class, Gempa.class, resData.getData()){
                        @Override
                        protected void bindView(DataGempaHolder holder, Gempa model,final int position){
                            holder.bind(context,model);
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(context,"Data Ke "+position+"Cilcked!",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    };
                    recyclerView.setAdapter(adapter);
                }else {
                    Toast.makeText(context,resData.getMessage(),Toast.LENGTH_LONG).show();
                }
                loading(false);
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
                loading(false);

            }
        });

    }
}
