package com.rginfotech.egames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.rginfotech.egames.adapter.BrandListForFilterAdapter;
import com.rginfotech.egames.adapter.BrandListOfCategoryAdapter;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.customviews.CustomTextViewMedium;
import com.rginfotech.egames.model.BrandListOfCategory;
import com.rginfotech.egames.utility.SortFilterSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BrandListForFilterActivity extends AppCompatActivity implements View.OnClickListener{

    private String category_list;
    private RecyclerView brandLists_recycler;
    private List<BrandListOfCategory> brandListOfCategoryList;
    private BrandListForFilterAdapter brandListForFilterAdapter;
    private SortFilterSessionManager sortFilterSessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_list_for_filter);
        brandLists_recycler = findViewById(R.id.brandLists_recycler);
        sortFilterSessionManager = new SortFilterSessionManager(this);
        ((CustomTextViewMedium)findViewById(R.id.title_text_view)).setText(getIntent().getStringExtra("title"));

        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            ((ImageView)findViewById(R.id.back_image)).setImageResource(R.drawable.arrow_30);
        }else{
            ((ImageView)findViewById(R.id.back_image)).setImageResource(R.drawable.arrow_right_30);
        }

        {
            //gifImageView.setVisibility(View.GONE);
            category_list = getIntent().getStringExtra("category_list");
            try {
                brandListOfCategoryList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(category_list);

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                    BrandListOfCategory brandListOfCategory = new BrandListOfCategory();
                    brandListOfCategory.setId(jsonObject3.getString("id"));
                    brandListOfCategory.setName(jsonObject3.getString("name"));
                    brandListOfCategory.setSlug(jsonObject3.getString("slug"));
                    brandListOfCategory.setIcon(jsonObject3.getString("icon"));
                    brandListOfCategory.setStatus(jsonObject3.getString("status"));
                    brandListOfCategory.setBrand_image(API.BrandURL+jsonObject3.getString("brand_image"));
                    brandListOfCategory.setCategory_id(jsonObject3.getString("category_id"));
                    brandListOfCategory.setUi_order(jsonObject3.getString("ui_order"));
                    brandListOfCategory.setStart_range(jsonObject3.getString("start_range"));
                    brandListOfCategory.setEnd_range(jsonObject3.getString("end_range"));
                    brandListOfCategory.setOffer_id(jsonObject3.getString("offer_id"));
                    brandListOfCategory.setOffer_name(jsonObject3.getString("offer_name"));
                    brandListOfCategory.setOffer_subtitle(jsonObject3.getString("offer_subtitle"));
                    JSONArray child_jsonArray = new JSONArray(jsonObject3.getString("child"));
                    //JSONArray offer_jsonArray = new JSONArray(jsonObject3.getString("offer"));
                    brandListOfCategory.setJsonArray(child_jsonArray);
                    brandListOfCategoryList.add(brandListOfCategory);


                }

                try {
                    String[] brandIdArr = sortFilterSessionManager.getFilter_Brands()
                            .replaceFirst(",", "").split(",");
                    for (int j=0;j<brandIdArr.length; j++){
                        for (int k=0;k<brandListOfCategoryList.size(); k++){
                            if (brandIdArr[j].equals(brandListOfCategoryList.get(k).getId())){
                                brandListOfCategoryList.get(k).setSelected(true);
                            }

                        }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }


                brandLists_recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
                brandListForFilterAdapter = new BrandListForFilterAdapter(this,brandListOfCategoryList);
                brandLists_recycler.setAdapter(brandListForFilterAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        findViewById(R.id.back_image).setOnClickListener(this);
        findViewById(R.id.done_tv).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.back_image:
                super.onBackPressed();
                break;
            case R.id.done_tv:
                String brand_id = "";
                //String[] arr = new String[0];
                for (int i = 0; i<brandListOfCategoryList.size(); i++){
                    if (brandListOfCategoryList.get(i).isSelected()){
                        brand_id = brand_id+","+brandListOfCategoryList.get(i).getId();
                    }

                }
                if (!TextUtils.isEmpty(brand_id)){
                    Intent intent = new Intent();
                    intent.putExtra("brand_id", brand_id);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }

                break;
        }
    }

    public void selectBrand(int position){
        if (brandListOfCategoryList.get(position).isSelected()){
            brandListOfCategoryList.get(position).setSelected(false);
        }else {
            brandListOfCategoryList.get(position).setSelected(true);
        }

        brandListForFilterAdapter.notifyDataSetChanged();
    }
}
