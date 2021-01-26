package com.rginfotech.egames;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.interfacelenzzo.SortByInterface;
import com.rginfotech.egames.localization.BaseActivity;
import com.rginfotech.egames.model.CountryList;
import com.rginfotech.egames.model.ProductList;
import com.rginfotech.egames.model.ProductSearchModel;
import com.rginfotech.egames.model.SortByModel;
import com.rginfotech.egames.myretorfit.ApiInterface;
import com.rginfotech.egames.myretorfit.RetrofitInstance;
import com.rginfotech.egames.myretorfit.UploadImageResponse;
import com.rginfotech.egames.utility.CommanClass;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.CustomVolleyRequest;
import com.rginfotech.egames.utility.FileUtils;
import com.rginfotech.egames.utility.SessionManager;
import com.rginfotech.egames.utility.SortFilterSessionManager;
import com.rginfotech.egames.utility.Utils;
import com.rginfotech.egames.utility.VolleyMultipartRequest;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener,SortByInterface {

    public static final int MULTIPLE_PERMISSIONS = 10;
    private static final String IMAGE_DIRECTORY = "/profile picture";
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public static int count = 0;
    String mCurrentPhotoPath;
    String globalImagePath;
    Uri imageFileUri;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    String dob;
    Calendar calendar;
    int year;
    int month;
    int dayOfMonth;
    private TextView number;
    private CircleImageView profilePic;
    private EditText name_edittext;
    private TextView dob_edittext;
    private AutoCompleteTextView number_code_edittext;
    private EditText phone_edittext;
    private Button update_button;
    //private GifImageView gifImageView;
    private ArrayList<CountryList> countryLists;
    private ArrayAdapter<CountryList> countryAdapter;
    private SessionManager sessionManager;
    private Dialog dialog;
    private List<ProductSearchModel> productLists;
    private ArrayAdapter<ProductSearchModel> searchadapter;
    private ArrayList<ProductList> array_of_product_lists;
    private String search_text = "";
    private ImageView search_image;
    private String total_count = "";
    private int total_value;
    private ImageLoader imageLoader;
    private String getName;
    private String getCountryCode;
    private String getNumber;
    private RequestQueue rQueue;
    private ImageView back_image;
    private LinearLayout liner, backLinLayoutId;
    private AutoCompleteTextView searchView;


    TextView product_not_av;
    private Dialog dialog1;
    private List<SortByModel> sortByModelList;
    private SortFilterSessionManager sortFilterSessionManager;

    public static byte[] getFileDataFromDrawable(Context context, int id) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Turn drawable into byte array.
     *
     * @param drawable data
     * @return byte array
     */
    public static byte[] getFileDataFromDrawable(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        sortFilterSessionManager = new SortFilterSessionManager(EditProfileActivity.this);

        sessionManager = new SessionManager(this);
        findViewById(R.id.cart_image).setOnClickListener(this);
        findViewById(R.id.search_image).setOnClickListener(this);
        findViewById(R.id.filter_image).setOnClickListener(this);
        findViewById(R.id.update_button).setOnClickListener(this);
        number = (TextView) findViewById(R.id.number);
        profilePic = (CircleImageView) findViewById(R.id.profilePic);
        profilePic.setOnClickListener(this);
        name_edittext = (EditText) findViewById(R.id.name_edittext);
        dob_edittext = (TextView) findViewById(R.id.dob_edittext);
        number_code_edittext = (AutoCompleteTextView) findViewById(R.id.number_code_edittext);
        phone_edittext = (EditText) findViewById(R.id.phone_edittext);
        phone_edittext.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        back_image = (ImageView) findViewById(R.id.back_image);
        backLinLayoutId = findViewById(R.id.backLinLayoutId);
        backLinLayoutId.setOnClickListener(this);
        liner = (LinearLayout) findViewById(R.id.liner);
        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            back_image.setImageResource(R.drawable.arrow_30);
            liner.setGravity(Gravity.END);
        } else {
            back_image.setImageResource(R.drawable.arrow_right_30);
            liner.setGravity(Gravity.START);
        }
        if (CommanMethod.isInternetConnected(EditProfileActivity.this)) {
            getCountryList();
            getProfileDetails();
            //searchProduct();
            CommanClass.getCartValue(this, number);
        }

        number_code_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_code_edittext.showDropDown();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditProfileActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View convertView = (View) inflater.inflate(R.layout.custom_countr_list, null);
                alertDialog.setNegativeButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setView(convertView);
                ListView lv = (ListView) convertView.findViewById(R.id.listView);
                final AlertDialog alert = alertDialog.create();
                alert.setTitle(getString(R.string.dialog_select_country));
                countryAdapter = new ArrayAdapter<CountryList>(EditProfileActivity.this, android.R.layout.simple_list_item_1, countryLists);
                lv.setAdapter(countryAdapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        CountryList countryList = (CountryList) arg0.getItemAtPosition(position);
                        number_code_edittext.setText("+"+ countryList.getCode());
                        alert.cancel();
                    }
                });
                alert.show();
            }
        });

        dob_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                dob_edittext.setText(year + "-" + (month + 1) + "-" + day);
                            }
                        }, year, month, dayOfMonth);
               // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        getSortBy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLinLayoutId:
                super.onBackPressed();
                break;
            case R.id.cart_image:
                Intent intent = new Intent(EditProfileActivity.this, UserCartActivity.class);
                startActivity(intent);
                break;
            case R.id.search_image:
                searchsortDialog();
                break;
            case R.id.layout1:
                sortDialog();
                break;
            case R.id.layout:
                Intent intent1 = new Intent(EditProfileActivity.this, FilterActivity.class);
                intent1.putExtra("from", "list_fragment");
                startActivity(intent1);
                break;
            case R.id.filter_image:
                intent1 = new Intent(EditProfileActivity.this, FilterActivity.class);
                startActivity(intent1);
                break;
            case R.id.profilePic:
                /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 10);
                    return;
                }else{
                    try{
                        selectImage();
                    }
                    catch (android.content.ActivityNotFoundException ex){
                    }
                }*/
                if (checkPermissions()) {
                    selectImage();
                }
                break;
            case R.id.update_button:
                validation();
                break;
        }
    }

    private void validation() {
        getName = name_edittext.getText().toString();
        getCountryCode = number_code_edittext.getText().toString().replace("+", "");
        dob = dob_edittext.getText().toString();
        getNumber = phone_edittext.getText().toString();

        if (getCountryCode.equals("965")) {
            //number_edittext.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(8)});
            phone_edittext.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        } else {
            //number_edittext.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(10)});
            phone_edittext.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        }

        if (getName.equals("") || getName.length() == 0) {
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_profile_name));
            //Toast.makeText(this, this.getString(R.string.toast_message_profile_name), Toast.LENGTH_SHORT).show();
        }
        if (dob.equals("") || dob.length() == 0) {
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.dateofbirth));
            //Toast.makeText(this, this.getString(R.string.toast_message_profile_name), Toast.LENGTH_SHORT).show();
        } else if (getCountryCode.equals("") || getCountryCode.length() == 0) {
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_country_code));
            //Toast.makeText(this, this.getString(R.string.toast_message_country_code), Toast.LENGTH_SHORT).show();
        } else if (getNumber.equals("") || getNumber.length() == 0) {
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_number));
            //Toast.makeText(this, this.getString(R.string.toast_message_number), Toast.LENGTH_SHORT).show();
        } else if (getCountryCode.equals("965") && getNumber.length() != 8) {
            CommanMethod.getCustomOkAlert(EditProfileActivity.this, this.getString(R.string.please_enter_valid_no));
            phone_edittext.requestFocus();

        } else if (!getCountryCode.equals("965") && getNumber.length() != 10) {
            CommanMethod.getCustomOkAlert(EditProfileActivity.this, this.getString(R.string.please_enter_valid_no));
            phone_edittext.requestFocus();

        } else {
            profileUpdate();
        }
    }

    private void profileUpdate() {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "my_profile_update", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        sessionManager.setUserName(getName);
                        Intent intent4 = new Intent(EditProfileActivity.this, HomeActivity.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent4);
                    } else {
                        // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", sessionManager.getUserId());
                params.put("name", getName);
                params.put("dob", dob);
                params.put("country_code", getCountryCode);
                params.put("phone", getNumber);
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

    private void uploadPanImage() {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        String userId = sessionManager.getUserId();
        RequestBody requestBodyUserId = RequestBody.create(MediaType.parse("multipart/form-data"), userId);
        File file = new File(globalImagePath);
        long length = (file.length() / (1024 * 1024));
        /*if(length>1) {
            file = saveBitmapToFile(file);
        }*/
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part uploadPic = MultipartBody.Part.createFormData("profilephoto", file.getName(), requestFile);
        ApiInterface apiInterface = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        apiInterface.uploadImage(requestBodyUserId, uploadPic).enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(Call<UploadImageResponse> call, retrofit2.Response<UploadImageResponse> response) {
                dialog.dismiss();
                UploadImageResponse uploadImageResponse = response.body();
                if (uploadImageResponse.getStatus().equalsIgnoreCase("success")) {
                    Picasso.get().load(API.PROFILE_IMAGE + uploadImageResponse.getResponse().getProfilephoto()).placeholder(R.drawable.no_img).into(profilePic);
                }

                Toast.makeText(EditProfileActivity.this, uploadImageResponse.getMessage(), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void uploadImage(final Bitmap bitmap) {
        //  Picasso.get().load(globalImagePath).into(profilePic);
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, API.BASE_URL + "my_profile_update",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        dialog.dismiss();
                        Log.d("ressssssoo", new String(response.data));
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
                            Picasso.get().load(API.PROFILE_IMAGE + jsonObject1.getString("profilephoto")).placeholder(R.drawable.no_img).into(profilePic);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", sessionManager.getUserId());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("profilephoto", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                //   params.put("profilephoto", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                //     params.put("profilephoto", new DataPart("file_avatar.jpg", getFileDataFromDrawable(getBaseContext(), profilePic.getDrawable())));

                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(EditProfileActivity.this);
        rQueue.add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void getCountryList() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(EditProfileActivity.this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "country_list", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    countryLists = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONArray jsonArray = new JSONArray(object.getString("result"));
                        for (int i = 0; i < jsonArray.length(); i++) {
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
                } catch (JSONException e) {
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
    private void getProfileDetails() {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "my_profile", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        imageLoader = CustomVolleyRequest.getInstance(EditProfileActivity.this).getImageLoader();
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        name_edittext.setText(jsonObject.getString("name"));
                        dob_edittext.setText(jsonObject.getString("dob"));
                        number_code_edittext.setText("+" +jsonObject.getString("country_code"));
                        phone_edittext.setText(jsonObject.getString("phone"));
                        //imageLoader.get(API.PROFILE_IMAGE+jsonObject.getString("profilephoto"), ImageLoader.getImageListener(profilePic, R.drawable.no_img, android.R.drawable.ic_dialog_alert));
                        Picasso.get().load(API.PROFILE_IMAGE + jsonObject.getString("profilephoto")).placeholder(R.drawable.no_img).into(profilePic);
                    } else {
                        // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", sessionManager.getUserId());
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



    public void getCartValue() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "usercart", response -> {
            try {
                JSONObject object = new JSONObject(response);
                String status = object.getString("status");
                if (status.equals("success")) {
                    JSONObject jsonObject = new JSONObject(object.getString("response"));
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("usercart_Array"));
                    JSONArray jsonArray = new JSONArray(jsonObject1.getString("usercart"));
                    if (jsonArray.length() > 0) {
                        total_value = jsonArray.length();
                        total_count = String.valueOf(total_value);
                        number.setText(total_count);
                    } else {
                        number.setText("");
                    }
                } else {
                    //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()) {
                    params.put("user_id", sessionManager.getUserId());
                } else {
                    params.put("user_id", sessionManager.getRandomValue());
                }
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

    private void selectImage() {
        final CharSequence[] options = {getResources().getString(R.string.take_photo), getResources().getString(R.string.from_gallery), getResources().getString(R.string.cancle)};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle(getResources().getString(R.string.add_photo));
        builder.setItems(options, (dialog, item) -> {
            if (item == 0) {
                dispatchTakePictureIntent();
            } else if (item == 1) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            } else if (item == 2) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                imageFileUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    private File createImageFile() throws IOException {

        String imageFileName = "IMG_" + System.currentTimeMillis() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save ic_account file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                //  Uri selectedImage = data.getData();
                CropImage.activity(imageFileUri)
                        .start(this);


               /* BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                bmpFactoryOptions.inSampleSize = 6;
                Bitmap bitmap = BitmapFactory.decodeFile(new File(mCurrentPhotoPath).getAbsolutePath(),bmpFactoryOptions);

                int rotate = 0;
                try {
                    File imageFile = new File(mCurrentPhotoPath);
                    ExifInterface exif = new ExifInterface(
                            imageFile.getAbsolutePath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                Bitmap thumbnail = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                uploadImage(thumbnail);*/


            } else if (requestCode == 2) {


                Uri selectedImage = data.getData();
                CropImage.activity(selectedImage)
                        .start(this);

                /*InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap thumbnail = BitmapFactory.decodeStream(inputStream);
                uploadImage(thumbnail); //upload profile pic
                profilePic.setImageBitmap(thumbnail);*/


            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                uploadRotateImage(resultUri);
                 /*
                        userImageBitmap = modifyOrientation(userImageBitmap, FileUtils.getRealPath(EditProfileActivity.this, getImageUri(EditProfileActivity.this, userImageBitmap)));
//                      imagePath = FileUtils.getRealPath(EditProfileActivity.this, getImageUri(EditProfileActivity.this, userImageBitmap));
                        imagePath = FileUtils.getRealPath(EditProfileActivity.this, getImageUri(EditProfileActivity.this, userImageBitmap));
//
*/


            }
        }
    }


    private void uploadRotateImage(Uri resultUri) {

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inSampleSize = 6;
        String imagePath = FileUtils.getRealPath(this, resultUri);
        globalImagePath = imagePath;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        int rotate = 0;
        try {
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        Bitmap thumbnail = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        //   uploadImage(thumbnail);
        uploadPanImage();
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(EditProfileActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                } else {
//                    Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
//                            .show();
                }
                // permissions list of don't granted permission
            }
            return;
        }
    }


    /////search wala

    private void searchsortDialog() {
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.search_dialog_box);
        ImageView dialog_back_image = (ImageView) dialog.findViewById(R.id.dialog_back_image);
        searchView = (AutoCompleteTextView) dialog.findViewById(R.id.searchView);
        Button search_button = (Button) dialog.findViewById(R.id.search_button);
        product_not_av = (TextView) dialog.findViewById(R.id.product_not_av);
        product_not_av.setVisibility(View.GONE);



        productLists = new ArrayList<>();
        searchView.setDropDownBackgroundResource(R.color.white);
        searchView.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);
        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            dialog_back_image.setImageResource(R.drawable.arrow_30);
        } else {
            dialog_back_image.setImageResource(R.drawable.arrow_right_30);
        }

        dialog.findViewById(R.id.layout1).setOnClickListener(this);
        dialog.findViewById(R.id.layout).setOnClickListener(this);

        if(!TextUtils.isEmpty(Utils.search_text)){
            searchView.setText(Utils.search_text);
        }

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("before");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("start");

            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("change" + s);
                search_text = s.toString();
                if (CommanMethod.isInternetConnected(EditProfileActivity.this)) {
                    searchProduct(search_text);
                }
            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                array_of_product_lists = new ArrayList<>();
                ProductList productList = new ProductList();
                productList.setId(searchadapter.getItem(position).getId());
                productList.setQuantity(searchadapter.getItem(position).getQuantity());
                productList.setUser_id(searchadapter.getItem(position).getUser_id());
                productList.setCate_id(searchadapter.getItem(position).getCate_id());
                productList.setCate_name(searchadapter.getItem(position).getCate_name());
                productList.setTitle(searchadapter.getItem(position).getTitle());
                productList.setDescription(searchadapter.getItem(position).getDescription());
                productList.setProduct_image(searchadapter.getItem(position).getProduct_image());
                productList.setProduct_images(searchadapter.getItem(position).getProduct_images());
                productList.setModel_no(searchadapter.getItem(position).getModel_no());
                productList.setSku_code(searchadapter.getItem(position).getSku_code());
                productList.setPrice(searchadapter.getItem(position).getPrice());
                productList.setCurrent_currency(searchadapter.getItem(position).getCurrent_currency());
                productList.setSale_price(searchadapter.getItem(position).getSale_price());
                productList.setNegotiable(searchadapter.getItem(position).getNegotiable());
                productList.setBrand_name(searchadapter.getItem(position).getBrand_name());
                productList.setBrand_id(searchadapter.getItem(position).getBrand_id());
                productList.setVariation_color(searchadapter.getItem(position).getVariation_color());
                productList.setTags(searchadapter.getItem(position).getTags());
                productList.setIs_hide(searchadapter.getItem(position).getIs_hide());
                productList.setReviewed(searchadapter.getItem(position).getReviewed());
                productList.setFeatured(searchadapter.getItem(position).getFeatured());
                productList.setArchived(searchadapter.getItem(position).getArchived());
                productList.setStatus(searchadapter.getItem(position).getStatus());
                productList.setStock_flag(searchadapter.getItem(position).getStock_flag());
                productList.setRating(searchadapter.getItem(position).getRating());
                productList.setReplacement(searchadapter.getItem(position).getReplacement());
                productList.setReleted_product(searchadapter.getItem(position).getReleted_product());
                productList.setOffer_id(searchadapter.getItem(position).getOffer_id());
                productList.setOffer_name(searchadapter.getItem(position).getOffer_name());
                array_of_product_lists.add(productList);

                /*Intent intent = new Intent(ProductDetailsActivity.this,SearchResultsActivity.class);
                intent.putExtra("array_of_product_lists",array_of_product_lists);
                startActivity(intent);*/
                Intent intent = new Intent(EditProfileActivity.this, ProductDetailsActivity.class);
                intent.putExtra("product_id", searchadapter.getItem(position).getId());
                intent.putExtra("current_currency", searchadapter.getItem(position).getCurrent_currency());
                intent.putExtra("title_name", searchadapter.getItem(position).getTitle());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        });
        dialog_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(searchView.getText().toString())){
                    array_of_product_lists = new ArrayList<>();
                    String getSearchText = searchView.getText().toString();
                    if (getSearchText.equals("") || getSearchText.length() == 0) {
                    } else {
                        search_text = getSearchText;
                        /*for (int i = 0; i < productLists.size(); i++) {
                            ProductList productList = new ProductList();
                            productList.setId(productLists.get(i).getId());
                            productList.setQuantity(productLists.get(i).getQuantity());
                            productList.setUser_id(productLists.get(i).getUser_id());
                            productList.setCate_id(productLists.get(i).getCate_id());
                            productList.setCate_name(productLists.get(i).getCate_name());
                            productList.setTitle(productLists.get(i).getTitle());
                            productList.setDescription(productLists.get(i).getDescription());
                            productList.setProduct_image(productLists.get(i).getProduct_image());
                            productList.setProduct_images(productLists.get(i).getProduct_images());
                            productList.setModel_no(productLists.get(i).getModel_no());
                            productList.setSku_code(productLists.get(i).getSku_code());
                            productList.setPrice(productLists.get(i).getPrice());
                            productList.setCurrent_currency(productLists.get(i).getCurrent_currency());
                            productList.setSale_price(productLists.get(i).getSale_price());
                            productList.setNegotiable(productLists.get(i).getNegotiable());
                            productList.setBrand_name(productLists.get(i).getBrand_name());
                            productList.setBrand_id(productLists.get(i).getBrand_id());
                            productList.setVariation_color(productLists.get(i).getVariation_color());
                            productList.setTags(productLists.get(i).getTags());
                            productList.setIs_hide(productLists.get(i).getIs_hide());
                            productList.setReviewed(productLists.get(i).getReviewed());
                            productList.setFeatured(productLists.get(i).getFeatured());
                            productList.setArchived(productLists.get(i).getArchived());
                            productList.setStatus(productLists.get(i).getStatus());
                            productList.setStock_flag(productLists.get(i).getStock_flag());
                            productList.setRating(productLists.get(i).getRating());
                            productList.setReplacement(productLists.get(i).getReplacement());
                            productList.setReleted_product(productLists.get(i).getReleted_product());
                            productList.setOffer_id(productLists.get(i).getOffer_id());
                            productList.setOffer_name(productLists.get(i).getOffer_name());
                            array_of_product_lists.add(productList);
                        }*/
                        Utils.search_text = search_text;
                        Intent intent = new Intent(EditProfileActivity.this, SearchResultsActivity.class);
                        intent.putExtra("array_of_product_lists", array_of_product_lists);
                        intent.putExtra("search_string", search_text);
                        startActivity(intent);
                        Utils.hideKeyBoard(EditProfileActivity.this, searchView);
                    }
                }

            }
        });
        dialog.show();
    }

    private void searchProduct(String search_text) {
       /* final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();*/
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "productlist_of_brand", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        productLists.clear();
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("product_list_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("product_list"));

                        if(jsonArray.length()>0){
                            product_not_av.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                ProductSearchModel productList = new ProductSearchModel();
                                productList.setId(jsonObject2.getString("id"));
                                productList.setQuantity(jsonObject2.getString("quantity"));
                                productList.setUser_id(jsonObject2.getString("user_id"));
                                productList.setCate_id(jsonObject2.getString("cate_id"));
                                productList.setCate_name(jsonObject2.getString("cate_name"));
                                productList.setTitle(jsonObject2.getString("title"));
                                productList.setDescription(jsonObject2.getString("description"));
                                productList.setProduct_image(API.ProductURL + jsonObject2.getString("product_image"));
                                productList.setProduct_images(jsonObject2.getString("product_images"));
                                productList.setModel_no(jsonObject2.getString("model_no"));
                                productList.setSku_code(jsonObject2.getString("sku_code"));
                                productList.setPrice(jsonObject2.getString("price"));
                                productList.setCurrent_currency(jsonObject2.getString("current_currency"));
                                //productList.setSale_price(jsonObject2.getString("sale_price"));
                                productList.setNegotiable(jsonObject2.getString("negotiable"));
                                productList.setBrand_name(jsonObject2.getString("brand_name"));
                                productList.setBrand_id(jsonObject2.getString("brand_id"));
                                productList.setVariation_color(jsonObject2.getString("variation_color"));
                                productList.setTags(jsonObject2.getString("tags"));
                                productList.setIs_hide(jsonObject2.getString("is_hide"));
                                productList.setReviewed(jsonObject2.getString("reviewed"));
                                productList.setFeatured(jsonObject2.getString("featured"));
                                productList.setArchived(jsonObject2.getString("archived"));
                                productList.setDeleted_at(jsonObject2.getString("deleted_at"));
                                productList.setStatus(jsonObject2.getString("status"));
                                productList.setStock_flag(jsonObject2.getString("stock_flag"));
                                productList.setRating(jsonObject2.getString("rating"));
                                productList.setReplacement(jsonObject2.getString("replacement"));
                                productList.setReleted_product(jsonObject2.getString("releted_product"));
                                productList.setOffer_id(jsonObject2.getString("offer_id"));
                                productList.setOffer_name(jsonObject2.getString("offer_name"));

                                productLists.add(productList);

                            }
                        }
                        else{
                            product_not_av.setVisibility(View.VISIBLE);
                        }


                    } else {
                        product_not_av.setVisibility(View.VISIBLE);

                        //Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }
                searchadapter = new ArrayAdapter<ProductSearchModel>(EditProfileActivity.this, android.R.layout.simple_list_item_1, productLists);
                searchView.setAdapter(searchadapter);
                // searchView.showDropDown();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //gifImageView.setVisibility(View.GONE);
                //dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("search_text", search_text);
                params.put("current_currency", "KWD");
               /* if (bundle.containsKey("brand_id"))
                    params.put("brand_id", bundle.getString("brand_id"));*/

                /*if (sessionManager.getPushFamilyId() != null) {
                    params.put("family_id", sessionManager.getPushFamilyId());
                }*/

                if (!TextUtils.isEmpty(Utils.key)) {
                    params.put("key", Utils.key);
                    params.put("value", Utils.value);

                    //this is putted here also because Utils.shouldFilterApply is false here so get search text value
                }
                List tempList = new ArrayList();


                   /* params.put("tags", sortFilterSessionManager.getFilter_Tags().replace("[", "").replace("]", ""));
                    params.put("color", sortFilterSessionManager.getFilter_Colors().replace("[", "").replace("]", ""));
                    params.put("replacement", sortFilterSessionManager.getFilter_Replacement().replace("[", "").replace("]", ""));
                    params.put("gender", sortFilterSessionManager.getFilter_Gender().replace("[", "").replace("]", ""));
                    params.put("rating", sortFilterSessionManager.getFilter_Rating().replace("[", "").replace("]", ""));
*/
                if (Utils.selectedData.size() > 0) {
                    for (int j = 0; j < Utils.selectedData.size(); j++) {
                        if (Utils.selectedData.get(j).getTitleMain().equals("Launch")) {
                            tempList.add(Utils.selectedData.get(j).getId());
                        }
                        if (j == Utils.selectedData.size() - 1) {
                            params.put("release_days", TextUtils.join(",", tempList));
                        }
                    }
                    tempList = new ArrayList();
                    for (int j = 0; j < Utils.selectedData.size(); j++) {
                        if (Utils.selectedData.get(j).getTitleMain().equals("Latest")) {
                            tempList.add(Utils.selectedData.get(j).getId());

                        }
                        if (j == Utils.selectedData.size() - 1) {
                            params.put("latest_sorting", TextUtils.join(",", tempList));
                        }
                    }
                    tempList = new ArrayList();

                    for (int j = 0; j < Utils.selectedData.size(); j++) {
                        if (Utils.selectedData.get(j).getTitleMain().equals("Tag")) {
                            tempList.add(Utils.selectedData.get(j).getId());

                        }
                        if (j == Utils.selectedData.size() - 1) {
                            params.put("tag", TextUtils.join(",", tempList));
                        }
                    }

                    tempList = new ArrayList();
                    for (int j = 0; j < Utils.selectedData.size(); j++) {
                        if (Utils.selectedData.get(j).getTitleMain().equals("Condition")) {
                            tempList.add(Utils.selectedData.get(j).getId());

                        }
                        if (j == Utils.selectedData.size() - 1) {
                            params.put("condition", TextUtils.join(",", tempList));
                        }
                    }
                }
                    /*for (int j = 0; j < Utils.selectedData.size(); j++) {
                        if (Utils.selectedData.get(j).getTitleMain().equals("Brands")) {
                            params.put("brands", Utils.selectedData.get(j).getId());
                        }
                    }*/
                if (Utils.brandsIs.size() > 0)
                    params.put("brand_id", TextUtils.join(",", Utils.brandsIs));

                params.put("country", Utils.lastSelectedCountryId);
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
    private void sortDialog() {
        dialog1 = new Dialog(EditProfileActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.custom_dialog);
        ImageView dialog_back_image = dialog1.findViewById(R.id.dialog_back_image);
        RecyclerView sort_recycler_view = dialog1.findViewById(R.id.sort_recycler_view);

        sort_recycler_view.setLayoutManager(new LinearLayoutManager(EditProfileActivity.this, RecyclerView.VERTICAL, false));
        EditProfileActivity.SortByAdapterInFragment sortByAdapter = new EditProfileActivity.SortByAdapterInFragment(EditProfileActivity.this, sortByModelList, this);
        sort_recycler_view.setAdapter(sortByAdapter);

        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            dialog_back_image.setImageResource(R.drawable.arrow_30);
        } else {
            dialog_back_image.setImageResource(R.drawable.arrow_right_30);
        }
        dialog_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }
    private void getSortBy() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(EditProfileActivity.this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "sort_by", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    sortByModelList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("sortlist_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("sortlist"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            SortByModel sortByModel = new SortByModel();
                            sortByModel.setKey(jsonObject2.getString("key"));
                            sortByModel.setValue(jsonObject2.getString("value"));
                            sortByModel.setTitle(jsonObject2.getString("title"));
                            sortByModel.setTitleAr(jsonObject2.getString("title_ar"));
                            sortByModel.setSelected(false);
                            sortByModelList.add(sortByModel);
                        }
                    } else {
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
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
    public class SortByAdapterInFragment extends RecyclerView.Adapter<SortByAdapterInFragment.MyViewHolder> {

        private LayoutInflater inflater;
        private List<SortByModel> sortByModelList;
        private ImageLoader imageLoader;
        private Context context;
        private int lastSelectedPosition = -1;
        private SortByInterface sortByInterface;

        public SortByAdapterInFragment(Context ctx, List<SortByModel> sortByModelList, SortByInterface sortByInterface) {
            inflater = LayoutInflater.from(ctx);
            this.sortByModelList = sortByModelList;
            this.context = ctx;
            this.sortByInterface = sortByInterface;

        }

        @Override
        public SortByAdapterInFragment.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = inflater.inflate(R.layout.sort_by_recycler_view_item, parent, false);
            SortByAdapterInFragment.MyViewHolder holder = new SortByAdapterInFragment.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(SortByAdapterInFragment.MyViewHolder holder, final int position) {

            if (context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                holder.text_sort.setText(sortByModelList.get(position).getTitle());
            } else {
                holder.text_sort.setText(sortByModelList.get(position).getTitleAr());
            }
        /*if(Locale.getDefault().getLanguage().equals("en")){
            holder.text_sort.setText(sortByModelList.get(position).getTitle());
        }else{
            holder.text_sort.setText(sortByModelList.get(position).getTitleAr());
        }*/
            holder.redia_button.setChecked(sortByModelList.get(position).isSelected());
        }

        @Override
        public int getItemCount() {
            return sortByModelList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            RadioButton redia_button;
            TextView text_sort;
            LinearLayout sort_lay;

            public MyViewHolder(View itemView) {
                super(itemView);
                sort_lay = itemView.findViewById(R.id.sort_lay);
                redia_button = (RadioButton) itemView.findViewById(R.id.redia_button);
                text_sort = (TextView) itemView.findViewById(R.id.text_sort);
                sort_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastSelectedPosition = getAdapterPosition();
                        Utils.selectedSortPosition = getAdapterPosition();
                        sortByInterface.sortByPrice(sortByModelList.get(lastSelectedPosition).getKey(), sortByModelList.get(lastSelectedPosition).getValue());
                        applySelect(lastSelectedPosition);
                        notifyDataSetChanged();
                    }

                    private void applySelect(int position) {
                        for (int j = 0; j < sortByModelList.size(); j++) {
                            if (j == position) {
                                sortByModelList.get(j).setSelected(true);
                            } else {
                                sortByModelList.get(j).setSelected(false);
                            }
                        }
                    }
                });

                redia_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lastSelectedPosition = getAdapterPosition();

                        sortByInterface.sortByPrice(sortByModelList.get(lastSelectedPosition).getKey(), sortByModelList.get(lastSelectedPosition).getValue());
                        notifyDataSetChanged();
                    }
                });


            }
        }
    }
    @Override
    public void sortByPrice(String key, String value) {
        Utils.key = key;
        Utils.value = value;
        this.dialog1.dismiss();
        // this data make effect on search data show result
    }
}
