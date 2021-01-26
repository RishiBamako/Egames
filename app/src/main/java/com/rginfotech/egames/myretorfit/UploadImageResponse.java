package com.rginfotech.egames.myretorfit;

import com.google.gson.annotations.SerializedName;

public class UploadImageResponse{

	@SerializedName("message_ar")
	private String messageAr;

	@SerializedName("response")
	private Response response;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public String getMessageAr(){
		return messageAr;
	}

	public Response getResponse(){
		return response;
	}

	public String getMessage(){
		return message == null?"":message;
	}

	public String getStatus(){
		return status==null?"":status;
	}
}