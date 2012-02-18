package com.company.comanda.brian.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable{

    public String name;
    public long id;
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(id);

    }


    public static final Parcelable.Creator<Category> CREATOR
    = new Parcelable.Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    
    public Category(){
        super();
    }
    
    private Category(Parcel in) {
        name = in.readString();
        id = in.readLong();
    }
}
