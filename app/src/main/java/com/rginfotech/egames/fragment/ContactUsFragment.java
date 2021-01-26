package com.rginfotech.egames.fragment;


import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.adapter.SocialAdapter;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.model.CountryList;
import com.rginfotech.egames.model.SocialModel;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment implements View.OnClickListener {

    private TextView whatsup_number_tv;
    private TextView order_email;
    private TextView support_email;
    private TextView support_number_tv;
    //private GifImageView gifImageView;
    private EditText name_edittext;
    private EditText email_edittext;
    private AutoCompleteTextView number_code_edittext;
    private EditText number_edittext;
    private EditText message_edittext;
    private Button submit_button;
    private Button reset_button;
    private String getName;
    private String getEmail;
    private String getNumberCode;
    private String getNumber;
    private String getMessage;
    private String getCodeWithNumber="";
    private ArrayList<CountryList> countryLists;
    private ArrayAdapter<CountryList> countryAdapter;
    private RecyclerView social_recycler;
    private SocialAdapter socialAdapter;
    private List<SocialModel> socialModelList;
    private String support_number;
    private String whatsup_number;
    private String get_order_email;
    private String get_support_email;
    private ImageView back_image;
    private LinearLayout liner;

    public ContactUsFragment() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        whatsup_number_tv = (TextView)view.findViewById(R.id.whatsup_number_tv);
        order_email = (TextView)view.findViewById(R.id.order_email);
        support_email = (TextView)view.findViewById(R.id.support_email);
        support_number_tv = (TextView)view.findViewById(R.id.support_number_tv);
        //gifImageView = (GifImageView)view.findViewById(R.id.gifImageView);
        name_edittext = (EditText)view.findViewById(R.id.name_edittext);
        email_edittext = (EditText)view.findViewById(R.id.dob_edittext);
        number_code_edittext = (AutoCompleteTextView)view.findViewById(R.id.number_code_edittext);
        number_edittext = (EditText)view.findViewById(R.id.number_edittext);
        message_edittext = (EditText)view.findViewById(R.id.message_edittext);
        submit_button = (Button)view.findViewById(R.id.submit_button);
        view.findViewById(R.id.submit_button).setOnClickListener(this);
        reset_button = (Button)view.findViewById(R.id.reset_button);
        view.findViewById(R.id.reset_button).setOnClickListener(this);
        view.findViewById(R.id.back_image).setOnClickListener(this);
        social_recycler = (RecyclerView)view.findViewById(R.id.social_recycler);
        view.findViewById(R.id.linerLayout1).setOnClickListener(this);
        view.findViewById(R.id.linerLayout2).setOnClickListener(this);
        view.findViewById(R.id.linerLayout3).setOnClickListener(this);
        view.findViewById(R.id.linerLayout4).setOnClickListener(this);
        back_image = (ImageView)view.findViewById(R.id.back_image);
        liner = (LinearLayout)view.findViewById(R.id.liner);
        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
            liner.setGravity(Gravity.END);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
            liner.setGravity(Gravity.START);
        }

        if (CommanMethod.isInternetConnected(getContext())){
            getSupportDetails();
            getCountryList();
            getSocialData();
        }

        number_code_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_code_edittext.showDropDown();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom_countr_list, null);
                alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setView(convertView);
                ListView lv = (ListView) convertView.findViewById(R.id.listView);
                final AlertDialog alert = alertDialog.create();
                alert.setTitle(" Select country....");
                countryAdapter = new ArrayAdapter<CountryList>(getActivity(), android.R.layout.simple_list_item_1, countryLists);
                lv.setAdapter(countryAdapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        CountryList countryList = (CountryList) arg0.getItemAtPosition(position);
                        number_code_edittext.setText(countryList.getCode());
                        alert.cancel();
                    }
                });
                alert.show();
            }
        });

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_button:
                validation();
                break;
            case R.id.reset_button:
                name_edittext.setText("");
                email_edittext.setText("");
                number_edittext.setText("");
                message_edittext.setText("");
                break;
            case R.id.back_image:
                getActivity().onBackPressed();
                break;
            case R.id.linerLayout1:
                //whatsappCall();
                openWhatsApp();
            break;
            case R.id.linerLayout2:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{get_order_email});
                email.putExtra(Intent.EXTRA_SUBJECT, "subject");
                email.putExtra(Intent.EXTRA_TEXT, "message");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, getResources().getString(R.string.choose_email_app)));
                break;
            case R.id.linerLayout3:
                Intent email_support = new Intent(Intent.ACTION_SEND);
                email_support.putExtra(Intent.EXTRA_EMAIL, new String[]{get_support_email});
                email_support.putExtra(Intent.EXTRA_SUBJECT, "subject");
                email_support.putExtra(Intent.EXTRA_TEXT, "message");
                email_support.setType("message/rfc822");
                startActivity(Intent.createChooser(email_support, getResources().getString(R.string.choose_email_app)));
                break;
            case R.id.linerLayout4:
                callDialog();
                break;
        }
    }

    private void whatsappCall(){
        String mimeString = "vnd.android.cursor.item/vnd.com.whatsapp.voip.call";
        String displayName = null;
        String name="ABC";
        Long _id;
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Data.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        while (cursor.moveToNext()) {
            _id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data._ID));
            displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            String mimeType = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
            if (displayName.equals(name)) {
                if (mimeType.equals(mimeString)) {
                    String data = "content://com.android.contacts/data/" + _id;
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_VIEW);
                    sendIntent.setDataAndType(Uri.parse(data), mimeString);
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                }
            }
        }
    }

    public void openWhatsApp(){

        PackageManager packageManager = getActivity().getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone="+ whatsup_number +"&text=" + URLEncoder.encode("", "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(), getResources().getString(R.string.whatsapp_not_found), Toast.LENGTH_LONG).show();
        }

    }

    private void callDialog(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+support_number));
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 10);
                    return;
                }else {
                    try{
                        startActivity(callIntent);
                    }
                    catch (android.content.ActivityNotFoundException ex){
                        //Toast.makeText(getActivity(),"yourActivity is not founded",Toast.LENGTH_SHORT).show();
                    }
                }
    }

    private void validation(){
        getName = name_edittext.getText().toString();
        getEmail = email_edittext.getText().toString();
        getNumberCode = number_code_edittext.getText().toString();
        getNumber = number_edittext.getText().toString();
        getMessage = message_edittext.getText().toString();
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmail);

        if(getName.equals("") || getName.length()==0){
            //Toast.makeText(getContext(),"Please enter name",Toast.LENGTH_SHORT).show();
            name_edittext.requestFocus();
            CommanMethod.getCustomOkAlert(getActivity(), getResources().getString(R.string.enter_name));
        }else if(getEmail.equals("") || getEmail.length()==0){
            //Toast.makeText(getContext(),"Please enter email id",Toast.LENGTH_SHORT).show();
            email_edittext.requestFocus();
            CommanMethod.getCustomOkAlert(getActivity(), this.getString(R.string.toast_message_register_email));
        }else if(!m.find()){
            //Toast.makeText(getContext(),"Your Email Id is Invalid",Toast.LENGTH_SHORT).show();
            email_edittext.requestFocus();
            CommanMethod.getCustomOkAlert(getActivity(), this.getString(R.string.toast_message_register_invalid));

        }else if(getNumberCode.equals("") || getNumberCode.length()==0){
            //Toast.makeText(getContext(),"Please select country code",Toast.LENGTH_SHORT).show();
            CommanMethod.getCustomOkAlert(getActivity(), this.getString(R.string.toast_message_register_country_code));
        }else if(getNumber.equals("") || getNumber.length()==0){
            //Toast.makeText(getContext(),"Please enter phone number",Toast.LENGTH_SHORT).show();
            number_edittext.requestFocus();
            CommanMethod.getCustomOkAlert(getActivity(), this.getString(R.string.toast_message_register_number));
        }else if(TextUtils.isEmpty(getMessage)){
            //Toast.makeText(getContext(),"Please enter phone number",Toast.LENGTH_SHORT).show();
            message_edittext.requestFocus();
            CommanMethod.getCustomOkAlert(getActivity(), this.getString(R.string.comment_message));
        }else{
            getCodeWithNumber = getNumberCode+getNumber;
            getEnqueryForm();
        }
    }

    private void getSupportDetails(){
        //gifImageView.setVisibility(View.VISIBLE);
        Dialog dialog = CommanMethod.getProgressDialogForFragment(getActivity());
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"contact_us", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject=new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("cms_detail_Array"));
                        JSONObject jsonObject12 = new JSONObject(jsonObject1.getString("admin_settings"));
                        whatsup_number_tv.setText(jsonObject12.getString("whatsup_number"));
                        whatsup_number = jsonObject12.getString("whatsup_number");
                        order_email.setText(jsonObject12.getString("order_email"));
                        get_order_email = jsonObject12.getString("order_email");
                        support_email.setText(jsonObject12.getString("support_email"));
                        get_support_email = jsonObject12.getString("support_email");
                        support_number_tv.setText(jsonObject12.getString("support_number"));
                        support_number = jsonObject12.getString("support_number");
                    }else if(status.equals("failed")){
                       // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                   //gifImageView.setVisibility(View.GONE);
                    dialog.dismiss();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //gifImageView.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void getCountryList(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"country_list", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    countryLists = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONArray jsonArray = new JSONArray(object.getString("result"));
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            CountryList countryList = new CountryList();
                            countryList.setId(jsonObject.getString("id"));
                            countryList.setCode(jsonObject.getString("code"));

                            if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                                countryList.setAsciiname(jsonObject.getString("asciiname"));
                            }
                            else{
                                countryList.setAsciiname(jsonObject.getString("asciiname_ar"));
                            }

                            countryList.setCurrency_code(jsonObject.getString("currency_code"));
                            countryList.setFlag(jsonObject.getString("flag"));
                            countryLists.add(countryList);
                        }

                    } else if (status.equals("0")) {
                        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("only_selected_countries", "1");

                return params;
            }
        };

        mRequestQueue.add(mStringRequest);
    }


    private void getEnqueryForm(){
        Dialog dialog = CommanMethod.getProgressDialogForFragment(getActivity());
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"enquery_form", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //gifImageView.setVisibility(View.GONE);
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String message = CommanMethod.getMessage(getActivity(), object);
                    if(status.equals("success")){
                        Intent intent = new Intent(getActivity(),HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }else{
                        CommanMethod.getCustomOkAlert(getActivity(), message);
                        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //parseVolleyError(error);
                //gifImageView.setVisibility(View.GONE);
                dialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("name", getName);
                params.put("email", getEmail);
                params.put("phone", getCodeWithNumber);
                params.put("message", getMessage);
                return params;
            }
        };

        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void getSocialData(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"social", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    socialModelList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("social_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("social_list"));
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            SocialModel socialModel = new SocialModel();
                            socialModel.setId(jsonObject2.getString("id"));
                            socialModel.setName(jsonObject2.getString("name"));
                            socialModel.setUrl(jsonObject2.getString("url"));
                            socialModel.setImage(API.SocialURL+jsonObject2.getString("image"));
                            socialModel.setIcon(jsonObject2.getString("icon"));
                            socialModel.setStatus(jsonObject2.getString("status"));
                            socialModelList.add(socialModel);
                        }

                    }else{
                        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false);
                social_recycler.setLayoutManager(gridLayoutManager);
                socialAdapter = new SocialAdapter(getActivity(),socialModelList);
                social_recycler.setAdapter(socialAdapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }
}
