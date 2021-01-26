package com.rginfotech.egames.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.MainActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.adapter.CountryListAdapter;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.localization.LocaleManager;
import com.rginfotech.egames.model.CountryList;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

import static com.rginfotech.egames.utility.CommanMethod.getCustomOkAlert;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArabicFragment extends Fragment {

    private ListView listView;
    //private GifImageView gifImageView;
    private ArrayList<CountryList> countryLists;
    private CountryListAdapter countryListAdapter;
    private Button applay_button;
    SessionManager sessionManager;
    private String country_code="";
    private String currency_code="";
    int check_positon=-1;

    public ArabicFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_arabic, container, false);

        sessionManager = new SessionManager(getActivity());
        listView = (ListView)view.findViewById(R.id.listView);
        //gifImageView = (GifImageView)view.findViewById(R.id.gifImageView);
        applay_button = (Button)view.findViewById(R.id.applay_button);
        if (CommanMethod.isInternetConnected(getContext())){
            getCountryList();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                countryListAdapter.getselectedPosition = position;
                countryListAdapter.notifyDataSetChanged();
                CountryList countryList = countryLists.get(position);
                country_code = countryList.getCode();
                currency_code = countryList.getCurrency_code();
            }
        });

        applay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("code"+country_code+" cu "+currency_code);
                sessionManager.setCountryCode(country_code);
                sessionManager.setCurrencyCode(currency_code);
                setNewLocale(getLanguageNotation(1));
                //Intent intent = new Intent(getActivity(), HomeActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
            }
        });
        return view;
    }

    private void getCountryList(){
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(getActivity());
        //gifImageView.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"country_list", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                    countryLists = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONArray jsonArray=new JSONArray(object.getString("result"));
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            CountryList countryList = new CountryList();
                            countryList.setId(jsonObject.getString("id"));
                            countryList.setCode(jsonObject.getString("code"));
                            countryList.setIso3(jsonObject.getString("iso3"));
                            countryList.setIso_numeric(jsonObject.getString("iso_numeric"));
                            countryList.setFips(jsonObject.getString("fips"));
                            countryList.setName(jsonObject.getString("name"));
                            countryList.setAsciiname(jsonObject.getString("asciiname_ar"));
                            countryList.setAsciiname_ar(jsonObject.getString("asciiname_ar"));
                            countryList.setCapital(jsonObject.getString("capital"));
                            countryList.setArea(jsonObject.getString("area"));
                            countryList.setPopulation(jsonObject.getString("population"));
                            countryList.setContinent_code(jsonObject.getString("continent_code"));
                            countryList.setTld(jsonObject.getString("tld"));
                            countryList.setCurrency_code(jsonObject.getString("currency_code"));
                            countryList.setPhone(jsonObject.getString("phone"));
                            countryList.setPostal_code_format(jsonObject.getString("postal_code_format"));
                            countryList.setPostal_code_regex(jsonObject.getString("postal_code_regex"));
                            countryList.setLanguages(jsonObject.getString("languages"));
                            countryList.setNeighbours(jsonObject.getString("neighbours"));
                            countryList.setEquivalent_fips_code(jsonObject.getString("equivalent_fips_code"));
                            countryList.setFlag(API.FlagURL+jsonObject.getString("flag"));
                            countryList.setAdmin_type(jsonObject.getString("admin_type"));
                            countryList.setAdmin_field_active(jsonObject.getString("admin_field_active"));
                            countryList.setActive(jsonObject.getString("active"));
                            countryList.setDelivery_charge(jsonObject.getString("delivery_charge"));

                            countryLists.add(countryList);
                        }

                        for(int i=0;i<countryLists.size();i++){
                            if(sessionManager.getCountryCode().equals(countryLists.get(i).getCode())){
                                check_positon=i;
                            }
                        }

                    }else{
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }

                countryListAdapter = new CountryListAdapter(countryLists,getContext());
                listView.setAdapter(countryListAdapter);
                countryListAdapter.getselectedPosition = check_positon;
                countryListAdapter.notifyDataSetChanged();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //gifImageView.setVisibility(View.GONE);
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("only_selected_countries", "1");

                return params;
            }
        };

        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 30000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }

    /*private void setNewLocale(String language) {
        LocaleManager.setNewLocale(getContext(), language);
        SessionManager sessionManager = new SessionManager(getContext());
        sessionManager.setLanguageSelected(language);
        Intent refresh = new Intent(getContext(), MainActivity.class);
        startActivity(refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        getActivity().finish();

    }*/

    private void setNewLocale(String language) {
        SessionManager sessionManager = new SessionManager(getContext());
        if(TextUtils.isEmpty(sessionManager.getCountryCode()) && TextUtils.isEmpty(sessionManager.getCurrencyCode())){
            getCustomOkAlert(getActivity(), "يرجى تحديد البلد.");
        }else {
            LocaleManager.setNewLocale(getContext(), language);
            sessionManager.setLanguageSelected(language);
            Intent refresh = new Intent(getContext(), HomeActivity.class);
            startActivity(refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            getActivity().finish();
        }
    }

    public static void getCustomOkAlert(Context context, String message){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ok_alert_layout);
        TextView title_tv = dialog.findViewById(R.id.title_tv);
        TextView info_tv = dialog.findViewById(R.id.info_tv);
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        title_tv.setText("محزر");
        info_tv.setText("يرجى تحديد البلد.");
        ok_tv.setText("نعم");
        ok_tv.setOnClickListener(view ->  {

            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public String getLanguageNotation(int position){
        switch (position){
            case 0:
                return LocaleManager.LANGUAGE_ENGLISH;
            case 1:
                return LocaleManager.LANGUAGE_ARABIC;
            default:
                return LocaleManager.LANGUAGE_ENGLISH;

        }
    }

}
