package com.rginfotech.egames.myretorfit;

import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("gender")
	private String gender;

	@SerializedName("activation_code")
	private Object activationCode;

	@SerializedName("created_at")
	private Object createdAt;

	@SerializedName("password")
	private String password;

	@SerializedName("user_type")
	private String userType;

	@SerializedName("updated_at")
	private Object updatedAt;

	@SerializedName("added_by")
	private Object addedBy;

	@SerializedName("id")
	private String id;

	@SerializedName("email")
	private String email;

	@SerializedName("forgotten_password_code")
	private Object forgottenPasswordCode;

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("last_login")
	private Object lastLogin;

	@SerializedName("active")
	private String active;

	@SerializedName("useracessid")
	private Object useracessid;

	@SerializedName("permission")
	private Object permission;

	@SerializedName("signup_type")
	private Object signupType;

	@SerializedName("country_code")
	private String countryCode;

	@SerializedName("profilephoto")
	private String profilephoto;

	@SerializedName("phone")
	private String phone;

	@SerializedName("parent_id")
	private String parentId;

	@SerializedName("dob")
	private Object dob;

	@SerializedName("name")
	private String name;

	@SerializedName("current_language")
	private String currentLanguage;

	@SerializedName("loyality_point")
	private String loyalityPoint;

	@SerializedName("user_wallet")
	private String userWallet;

	@SerializedName("username")
	private String username;

	@SerializedName("status")
	private String status;

	public String getGender(){
		return gender;
	}

	public Object getActivationCode(){
		return activationCode;
	}

	public Object getCreatedAt(){
		return createdAt;
	}

	public String getPassword(){
		return password;
	}

	public String getUserType(){
		return userType;
	}

	public Object getUpdatedAt(){
		return updatedAt;
	}

	public Object getAddedBy(){
		return addedBy;
	}

	public String getId(){
		return id;
	}

	public String getEmail(){
		return email;
	}

	public Object getForgottenPasswordCode(){
		return forgottenPasswordCode;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public Object getLastLogin(){
		return lastLogin;
	}

	public String getActive(){
		return active;
	}

	public Object getUseracessid(){
		return useracessid;
	}

	public Object getPermission(){
		return permission;
	}

	public Object getSignupType(){
		return signupType;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public String getProfilephoto(){
		return profilephoto;
	}

	public String getPhone(){
		return phone;
	}

	public String getParentId(){
		return parentId;
	}

	public Object getDob(){
		return dob;
	}

	public String getName(){
		return name;
	}

	public String getCurrentLanguage(){
		return currentLanguage;
	}

	public String getLoyalityPoint(){
		return loyalityPoint;
	}

	public String getUserWallet(){
		return userWallet;
	}

	public String getUsername(){
		return username;
	}

	public String getStatus(){
		return status;
	}
}