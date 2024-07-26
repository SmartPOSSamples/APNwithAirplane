package com.wizarpos.wizarviewagentassistant.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class NetworkType implements Parcelable{
    private String name;
    private int typeId;

    public NetworkType(String name, int typeId) {
        this.name = name;
        this.typeId = typeId;
    }

    protected NetworkType(Parcel in) {
        name = in.readString();
        typeId = in.readInt();
    }

    public static final Creator<NetworkType> CREATOR = new Creator<NetworkType>() {
        @Override
        public NetworkType createFromParcel(Parcel in) {
            return new NetworkType(in);
        }

        @Override
        public NetworkType[] newArray(int size) {
            return new NetworkType[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.typeId);
    }

    @Override
    public String toString() {
        return "NetworkMode{" +
                "name='" + name + '\'' +
                ", modeId=" + typeId +
                '}';
    }
}
