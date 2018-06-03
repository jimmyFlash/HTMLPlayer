package com.jimmy.htmlplayer.datahandlers.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class HTMLObject implements Parcelable{
	
	
	private String htmlPath = "";



	private String id ;


	protected HTMLObject(Parcel in) {
		htmlPath = in.readString();
		id = in.readString();
		thumb = in.readString();
	}

	public static final Creator<HTMLObject> CREATOR = new Creator<HTMLObject>() {
		@Override
		public HTMLObject createFromParcel(Parcel in) {
			return new HTMLObject(in);
		}

		@Override
		public HTMLObject[] newArray(int size) {
			return new HTMLObject[size];
		}
	};

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	private String thumb;
	
	public HTMLObject() {

		// constructor
	}
	
	
	public String getHtml(){
		return htmlPath;
	}
	
	public void setHtml(String s ){
		 htmlPath = s;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(htmlPath);
		dest.writeString(id);
		dest.writeString(thumb);
	}
}
