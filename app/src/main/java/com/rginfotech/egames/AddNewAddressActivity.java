package com.rginfotech.egames;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.localization.BaseActivity;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.GPSTracker;
import com.rginfotech.egames.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AddNewAddressActivity extends BaseActivity implements View.OnClickListener, LocationListener {

    protected static final int REQUEST_CHECK_SETTINGS = 122;
    private static final int LOCATION_REQUEST_CODE = 100;
    private static final int REQUEST_LOCATION = 1002;
    LocationListener locationListener;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder builder;
    LocationManager locationManager;
    String latitude, longitude;
    RadioGroup addressTypeRadioGroup;
    RadioButton homeRB;
    RadioButton officeRB;
    RadioButton otherRB;
    private EditText full_name;
    private EditText current_location_name;
    private EditText current_location_lat;
    private EditText current_location_long;
    private TextView area;
    private EditText area_text;
    private TextView block;
    private EditText block_text;
    private TextView street;
    private EditText street_text;
    private EditText avenue_text;
    private TextView house;
    private EditText house_text;
    private EditText floor_text;
    private EditText flat_text;
    private EditText phone_text;
    private EditText paci_text;
    private EditText comment_text;
    private LinearLayout linerLayout2;
    private SessionManager sessionManager;
    private String Latitude, Longitude;
    private String get_full_name;
    private String get_current_location_name;
    private String get_current_location_lat;
    private String get_current_location_long;
    private String get_area_text;
    private String get_block_text;
    private String get_street_text;
    private String get_avenue_text;
    private String get_house_text;
    private String get_floor_text;
    private String get_flat_text;
    private String get_phone_text;
    private String get_paci_text;
    private String get_comment_text;
    private String address_type;
    private TextView button;
    private boolean isNewAddress = true;
    private String user_billing_address_id;
    private ImageView back_image;
    private GoogleApiClient googleApiClient;

    public String getAddress(Context context, String LATITUDE, String LONGITUDE) {
        List<String> addressIs = new ArrayList<>();
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(LATITUDE), Double.parseDouble(LONGITUDE), 1);
            if (addresses != null && addresses.size() > 0) {
                addressIs.add(addresses.get(0).getAddressLine(0));
                addressIs.add(addresses.get(0).getLocality());
                addressIs.add(addresses.get(0).getAdminArea());
                addressIs.add(addresses.get(0).getCountryName());
                addressIs.add(addresses.get(0).getPostalCode());
                addressIs.add(addresses.get(0).getFeatureName());

                area_text.setText(addresses.get(0).getAddressLine(0));
                block_text.setText(addresses.get(0).getFeatureName());
                street_text.setText(addresses.get(0).getPostalCode());
                house_text.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return TextUtils.join(",", addressIs);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        sessionManager = new SessionManager(this);
        findViewById(R.id.back_image).setOnClickListener(this);
        findViewById(R.id.linerLayout).setOnClickListener(this);
        findViewById(R.id.close_text).setOnClickListener(this);
        findViewById(R.id.save_button).setOnClickListener(this);
        full_name = (EditText) findViewById(R.id.full_name);
        current_location_name = (EditText) findViewById(R.id.current_location_name);
        addressTypeRadioGroup = findViewById(R.id.addressTypeRadioGroupId);

        homeRB = findViewById(R.id.homeRBId);
        officeRB = findViewById(R.id.officeRBId);
        otherRB = findViewById(R.id.otherRBId);

        current_location_lat = (EditText) findViewById(R.id.current_location_lat);
        current_location_long = (EditText) findViewById(R.id.current_location_long);
        area = (TextView) findViewById(R.id.area);
        area_text = (EditText) findViewById(R.id.area_text);
        block = (TextView) findViewById(R.id.block);
        block_text = (EditText) findViewById(R.id.block_text);
        street = (TextView) findViewById(R.id.street);
        street_text = (EditText) findViewById(R.id.street_text);
        avenue_text = (EditText) findViewById(R.id.avenue_text);
        house = (TextView) findViewById(R.id.house);
        house_text = (EditText) findViewById(R.id.house_text);
        floor_text = (EditText) findViewById(R.id.floor_text);
        flat_text = (EditText) findViewById(R.id.flat_text);
        phone_text = (EditText) findViewById(R.id.phone_text);
        paci_text = (EditText) findViewById(R.id.paci_text);
        comment_text = (EditText) findViewById(R.id.comment_text);
        linerLayout2 = (LinearLayout) findViewById(R.id.linerLayout2);
        button = (TextView) findViewById(R.id.button);
        //linerLayout2.setVisibility(View.GONE);
        // findViewById(R.id.linerLayout).setVisibility(View.GONE);
        back_image = (ImageView) findViewById(R.id.back_image);
        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            back_image.setImageResource(R.drawable.arrow_30);
            findViewById(R.id.ballon_iv).setScaleX(1f);

        } else {
            back_image.setImageResource(R.drawable.arrow_right_30);
            findViewById(R.id.ballon_iv).setScaleX(-1f);
        }
        locationListener = this;
        if (sessionManager.getCurrencyCode().equals("KWD")) {
            findViewById(R.id.linerLayout).setVisibility(View.VISIBLE);
        }
        checkAndRequestPermissions();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isNewAddress = false;
            user_billing_address_id = bundle.getString("user_billing_address_id");
            get_full_name = bundle.getString("get_full_name");
            full_name.setText(get_full_name);
            get_current_location_name = bundle.getString("get_current_location_name");
            get_current_location_lat = bundle.getString("get_current_location_lat");
            address_type = bundle.getString("address_type");
            get_current_location_long = bundle.getString("get_current_location_long");
            if (TextUtils.isEmpty(get_current_location_name)) {
                linerLayout2.setVisibility(View.GONE);
                area.setText(this.getString(R.string.area));
                block.setText(this.getString(R.string.block));
                street.setText(this.getString(R.string.street));
                house.setText(this.getString(R.string.house));
            } else {
                area.setText(this.getString(R.string.area_hint));
                block.setText(this.getString(R.string.block_hint));
                street.setText(this.getString(R.string.street_hint));
                linerLayout2.setVisibility(View.VISIBLE);
                current_location_name.setText(get_current_location_name);
                // current_location_lat.setText(get_current_location_lat);
                // current_location_long.setText(get_current_location_long);
            }
            if (address_type.equals("Home")) {
                address_type = "Home";
                homeRB.setChecked(true);
            } else if (address_type.equals("Office")) {
                address_type = "Office";
                officeRB.setChecked(true);

            } else if (address_type.equals("Other")) {
                address_type = "Other";
                otherRB.setChecked(true);

                addressTypeRadioGroup.getChildAt(0).setActivated(false);
                addressTypeRadioGroup.getChildAt(1).setActivated(false);
                addressTypeRadioGroup.getChildAt(2).setActivated(true);
            }


            get_area_text = bundle.getString("get_area_text");
            area_text.setText(get_area_text);
            get_block_text = bundle.getString("get_block_text");
            block_text.setText(get_block_text);
            get_street_text = bundle.getString("get_street_text");
            street_text.setText(get_street_text);
            get_avenue_text = bundle.getString("get_avenue_text");
            avenue_text.setText(get_avenue_text);
            get_house_text = bundle.getString("get_house_text");
            house_text.setText(get_house_text);
            get_floor_text = bundle.getString("get_floor_text");
            floor_text.setText(get_floor_text);
            get_flat_text = bundle.getString("get_flat_text");
            flat_text.setText(get_flat_text);
            get_phone_text = bundle.getString("get_phone_text");
            phone_text.setText(get_phone_text);
            get_paci_text = bundle.getString("get_paci_text");
            paci_text.setText(get_paci_text);
            get_comment_text = bundle.getString("get_comment_text");
            comment_text.setText(get_comment_text);
            button.setText(this.getString(R.string.update_address));
        }
        gpsSettings();

    }

    private void gpsSettings() {
        googleApiClient = getAPIClientInstance();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    private GoogleApiClient getAPIClientInstance() {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build();
        return mGoogleApiClient;
    }

    private void requestGPSSettings() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(500);
        builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("", "All location settings are satisfied.");
                        getLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("", "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");
                        try {
                            status.startResolutionForResult(AddNewAddressActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e("Applicationsett", e.toString());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("", "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                        Toast.makeText(getApplication(), "Location settings are inadequate, and cannot be fixed here", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(AddNewAddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddNewAddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        } else {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 1, this);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                getLocation();
                break;
            default:
                break;
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location != null) {
            double lat = location.getLatitude();
            double longi = location.getLongitude();

            latitude = String.valueOf(lat);
            longitude = String.valueOf(longi);

            current_location_lat.setText(latitude);
            current_location_long.setText(longitude);

            Log.e("LOCATIONIS", getAddress(AddNewAddressActivity.this, latitude, longitude));

        } else {
            Toast.makeText(AddNewAddressActivity.this, "Network Issue", Toast.LENGTH_SHORT).show();
        }
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_image:
                super.onBackPressed();
                break;
            case R.id.linerLayout:
                linerLayout2.setVisibility(View.VISIBLE);
                findViewById(R.id.linerLayout).setVisibility(View.GONE);
               /* area.setText(this.getString(R.string.area_hint));
                block.setText(this.getString(R.string.block_hint));
                street.setText(this.getString(R.string.street_hint));
                house.setText(this.getString(R.string.house_hint));*/
                //locationFind();
                requestGPSSettings();

                break;
            case R.id.close_text:
                linerLayout2.setVisibility(View.GONE);
                findViewById(R.id.linerLayout).setVisibility(View.VISIBLE);
                current_location_name.setText("");
                // current_location_lat.setText("");
                //  current_location_long.setText("");
                area.setText(this.getString(R.string.area));
                block.setText(this.getString(R.string.block));
                street.setText(this.getString(R.string.street));
                house.setText(this.getString(R.string.house));
                break;
            case R.id.save_button:
                validation();
                break;
        }
    }

    private void validation() {
        get_full_name = full_name.getText().toString();
        get_current_location_name = current_location_name.getText().toString();
        get_current_location_lat = current_location_lat.getText().toString();
        get_current_location_long = current_location_long.getText().toString();
        get_area_text = area_text.getText().toString();
        get_block_text = block_text.getText().toString();
        get_street_text = street_text.getText().toString();
        get_avenue_text = avenue_text.getText().toString();
        get_house_text = house_text.getText().toString();
        get_floor_text = floor_text.getText().toString();
        get_flat_text = flat_text.getText().toString();
        get_phone_text = phone_text.getText().toString();
        get_paci_text = paci_text.getText().toString();
        get_comment_text = comment_text.getText().toString();
        int checkedId = addressTypeRadioGroup.getCheckedRadioButtonId();
        if (checkedId == R.id.homeRBId) {
            address_type = "Home";
        } else if (checkedId == R.id.officeRBId) {
            address_type = "Office";
        } else if (checkedId == R.id.otherRBId) {
            address_type = "Other";
        } else {
            address_type = "";
        }
        /*if (get_full_name.equals("") || get_full_name.length() == 0) {
            full_name.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_message_name),Toast.LENGTH_SHORT).show();
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_name));
        } else if (get_phone_text.equals("") || get_phone_text.length() == 0) {
            phone_text.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_massage_mobile),Toast.LENGTH_SHORT).show();
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_massage_mobile));
        } else if (TextUtils.isEmpty(get_current_location_name)) {
            if (get_area_text.equals("") || get_area_text.length() == 0) {
                area_text.requestFocus();
                ///Toast.makeText(this,this.getString(R.string.toast_message_area),Toast.LENGTH_SHORT).show();
                CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_area));
            } else if (get_block_text.equals("") || get_block_text.length() == 0) {
                block_text.requestFocus();
                //Toast.makeText(this,this.getString(R.string.toast_message_block),Toast.LENGTH_SHORT).show();
                CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_block));
            } else if (get_street_text.equals("") || get_street_text.length() == 0) {
                street_text.requestFocus();
                //Toast.makeText(this,this.getString(R.string.toast_message_street),Toast.LENGTH_SHORT).show();
                CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_street));
            } else if (get_house_text.equals("") || get_house_text.length() == 0) {
                house_text.requestFocus();
                //Toast.makeText(this,this.getString(R.string.toast_message_house),Toast.LENGTH_SHORT).show();
                CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_house));
            } else if (TextUtils.isEmpty(get_paci_text)) {
                if (get_house_text.equals("") || get_house_text.length() == 0) {
                    house_text.requestFocus();
                    //Toast.makeText(this,this.getString(R.string.toast_message_house),Toast.LENGTH_SHORT).show();
                    CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_house));
                } else if (get_floor_text.equals("") || get_floor_text.length() == 0) {
                    floor_text.requestFocus();
                    //Toast.makeText(this,this.getString(R.string.toast_floor),Toast.LENGTH_SHORT).show();
                    CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_floor));
                } else if (get_flat_text.equals("") || get_flat_text.length() == 0) {
                    flat_text.requestFocus();
                    //Toast.makeText(this,this.getString(R.string.toast_flat),Toast.LENGTH_SHORT).show();
                    CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_flat));
                }
                if (isNewAddress) {
                    if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                        addNewAddress();
                    }
                } else {
                    if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                        updateAddress(user_billing_address_id);
                    }
                }

            } else {
                if (isNewAddress) {
                    if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                        addNewAddress();
                    }
                } else {
                    if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                        updateAddress(user_billing_address_id);
                    }
                }
            }
        } else if (TextUtils.isEmpty(get_paci_text)) {
            if (get_house_text.equals("") || get_house_text.length() == 0) {
                house_text.requestFocus();
                CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_house));
                //Toast.makeText(this,this.getString(R.string.toast_message_house),Toast.LENGTH_SHORT).show();
            } else if (get_floor_text.equals("") || get_floor_text.length() == 0) {
                floor_text.requestFocus();
                CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_floor));
                //Toast.makeText(this,this.getString(R.string.toast_floor),Toast.LENGTH_SHORT).show();
            } else if (get_flat_text.equals("") || get_flat_text.length() == 0) {
                flat_text.requestFocus();
                CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_flat));
                //Toast.makeText(this,this.getString(R.string.toast_flat),Toast.LENGTH_SHORT).show();
            } else {
                if (isNewAddress) {
                    if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                        addNewAddress();
                    }
                } else {
                    if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                        updateAddress(user_billing_address_id);
                    }
                }
            }

        } else {
            if (isNewAddress) {
                if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                    addNewAddress();
                }
            } else {
                if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                    updateAddress(user_billing_address_id);
                }

            }
        }*/
        /*if (get_full_name.equals("") || get_full_name.length() == 0) {
            full_name.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_message_name),Toast.LENGTH_SHORT).show();
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_name));
        } else if (get_phone_text.equals("") || get_phone_text.length() == 0) {
            phone_text.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_massage_mobile),Toast.LENGTH_SHORT).show();
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_massage_mobile));
        } else if (TextUtils.isEmpty(get_current_location_name)) {
            if (get_area_text.equals("") || get_area_text.length() == 0) {
                area_text.requestFocus();
                ///Toast.makeText(this,this.getString(R.string.toast_message_area),Toast.LENGTH_SHORT).show();
                CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_area));
            } else if (get_block_text.equals("") || get_block_text.length() == 0) {
                block_text.requestFocus();
                //Toast.makeText(this,this.getString(R.string.toast_message_block),Toast.LENGTH_SHORT).show();
                CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_block));
            } else if (get_street_text.equals("") || get_street_text.length() == 0) {
                street_text.requestFocus();
                //Toast.makeText(this,this.getString(R.string.toast_message_street),Toast.LENGTH_SHORT).show();
                CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_street));
            } else if (get_house_text.equals("") || get_house_text.length() == 0) {
                house_text.requestFocus();
                //Toast.makeText(this,this.getString(R.string.toast_message_house),Toast.LENGTH_SHORT).show();
                CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_house));
            } else if (TextUtils.isEmpty(get_paci_text)) {
                if (get_house_text.equals("") || get_house_text.length() == 0) {
                    house_text.requestFocus();
                    //Toast.makeText(this,this.getString(R.string.toast_message_house),Toast.LENGTH_SHORT).show();
                    CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_house));
                } else if (get_floor_text.equals("") || get_floor_text.length() == 0) {
                    floor_text.requestFocus();
                    //Toast.makeText(this,this.getString(R.string.toast_floor),Toast.LENGTH_SHORT).show();
                    CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_floor));
                } else if (get_flat_text.equals("") || get_flat_text.length() == 0) {
                    flat_text.requestFocus();
                    //Toast.makeText(this,this.getString(R.string.toast_flat),Toast.LENGTH_SHORT).show();
                    CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_flat));
                }
                if (isNewAddress) {
                    if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                        addNewAddress();
                    }
                } else {
                    if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                        updateAddress(user_billing_address_id);
                    }
                }

            } else {

            }
        } else if (TextUtils.isEmpty(get_paci_text)) {
            if (get_house_text.equals("") || get_house_text.length() == 0) {
                house_text.requestFocus();
                CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_house));
                //Toast.makeText(this,this.getString(R.string.toast_message_house),Toast.LENGTH_SHORT).show();
            } else if (get_floor_text.equals("") || get_floor_text.length() == 0) {
                floor_text.requestFocus();
                CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_floor));
                //Toast.makeText(this,this.getString(R.string.toast_floor),Toast.LENGTH_SHORT).show();
            } else if (get_flat_text.equals("") || get_flat_text.length() == 0) {
                flat_text.requestFocus();
                CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_flat));
                //Toast.makeText(this,this.getString(R.string.toast_flat),Toast.LENGTH_SHORT).show();
            } else {
                if (isNewAddress) {
                    if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                        addNewAddress();
                    }
                } else {
                    if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                        updateAddress(user_billing_address_id);
                    }
                }
            }

        } else {
            if (isNewAddress) {
                if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                    addNewAddress();
                }
            } else {
                if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                    updateAddress(user_billing_address_id);
                }

            }
        }*/

        if (TextUtils.isEmpty(get_phone_text)) {
            phone_text.requestFocus();
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_massage_mobile));
        } else if (get_phone_text.length() < 8) {
            phone_text.requestFocus();
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_massage_mobile));
        } else if (TextUtils.isEmpty(address_type)) {
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.please_select_addresstype));
        } else if (isNewAddress) {
            if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                addNewAddress();
            }
        } else {
            if (CommanMethod.isInternetConnected(AddNewAddressActivity.this)) {
                updateAddress(user_billing_address_id);
            }
        }
    }

    private void locationFind() {
        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            Latitude = String.valueOf(gps.getLatitude());
            Longitude = String.valueOf(gps.getLongitude());
            try {
                String address = "", city = "", postalcode = "";
                List<Address> addresses = geocoder.getFromLocation(gps.getLatitude(), gps.getLongitude(), 10);
                for (int i = 0; i < addresses.size(); i++) {
                    if (!TextUtils.isEmpty(addresses.get(i).getAddressLine(0))) {
                        address = addresses.get(i).getAddressLine(0);
                        city = addresses.get(i).getLocality();
                        postalcode = addresses.get(i).getPostalCode();
                        break;
                    }
                }
                current_location_name.setText(address);
                current_location_lat.setText(Latitude);
                current_location_long.setText(Longitude);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

        } else {

            gps.showSettingsAlert();

        }
    }

    private boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(AddNewAddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(AddNewAddressActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), LOCATION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    private void addNewAddress() {
        //gifImageView.setVisibility(View.VISIBLE);
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "user_billing_address_add", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    // gifImageView.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        Intent intent = new Intent(AddNewAddressActivity.this, CheckOutActivity.class);
                        startActivity(intent);

                    } else {
                        // String message = object.getString("message");
                        //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                    //gifImageView.setVisibility(View.GONE);
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //gifImageView.setVisibility(View.GONE);
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("full_name", get_full_name);
                params.put("address_type", address_type);
                params.put("area", get_area_text);
                params.put("block", get_block_text);
                params.put("street", get_street_text);
                params.put("avenue", get_avenue_text);
                params.put("house_no", get_house_text);
                params.put("floor_no", get_floor_text);
                params.put("flat_no", get_flat_text);
                params.put("phone_no", get_phone_text);
                params.put("comments", get_comment_text);
                params.put("latitude", get_current_location_lat);
                params.put("longitude", get_current_location_long);
                params.put("user_id", sessionManager.getUserId());
                params.put("currrent_location", get_current_location_name);
                params.put("paci_number", get_paci_text);
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

    private void updateAddress(final String user_billing_address_id) {
        //gifImageView.setVisibility(View.VISIBLE);
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "user_billing_address_update", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        Intent intent = new Intent(AddNewAddressActivity.this, CheckOutActivity.class);
                        startActivity(intent);

                    } else {
                        String message = CommanMethod.getMessage(AddNewAddressActivity.this, object);
                        CommanMethod.getCustomOkAlert(AddNewAddressActivity.this, message);
                        //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                    //gifImageView.setVisibility(View.GONE);
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //gifImageView.setVisibility(View.GONE);
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("full_name", get_full_name);
                params.put("area", get_area_text);
                params.put("block", get_block_text);
                params.put("street", get_street_text);
                params.put("avenue", get_avenue_text);
                params.put("house_no", get_house_text);
                params.put("floor_no", get_floor_text);
                params.put("flat_no", get_flat_text);
                params.put("phone_no", get_phone_text);
                params.put("comments", get_comment_text);
                params.put("latitude", get_current_location_lat);
                params.put("longitude", get_current_location_long);
                params.put("user_id", sessionManager.getUserId());
                params.put("currrent_location", get_current_location_name);
                params.put("address_type", address_type);
                params.put("paci_number", get_paci_text);
                params.put("user_billing_address_id", user_billing_address_id);
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


}
