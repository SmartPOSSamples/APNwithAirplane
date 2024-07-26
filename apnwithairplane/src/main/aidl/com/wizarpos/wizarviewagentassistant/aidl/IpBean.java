package com.wizarpos.wizarviewagentassistant.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class IpBean implements Parcelable {
    private String ipAddress;
    private String prefixLength;
    private String gateway;
    private String dnsServer;

    public IpBean(){}

    protected IpBean(Parcel in) {
        ipAddress = in.readString();
        prefixLength = in.readString();
        gateway = in.readString();
        dnsServer = in.readString();
    }

    public static final Creator<IpBean> CREATOR = new Creator<IpBean>() {
        @Override
        public IpBean createFromParcel(Parcel in) {
            return new IpBean(in);
        }

        @Override
        public IpBean[] newArray(int size) {
            return new IpBean[size];
        }
    };

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPrefixLength() {
        return prefixLength;
    }

    public void setPrefixLength(String prefixLength) {
        this.prefixLength = prefixLength;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getDnsServer() {
        return dnsServer;
    }

    public void setDnsServer(String dnsServer) {
        this.dnsServer = dnsServer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ipAddress);
        dest.writeString(this.prefixLength);
        dest.writeString(this.gateway);
        dest.writeString(this.dnsServer);
    }

    @Override
    public String toString() {
        return "IpBean{" +
                "ipAddress='" + ipAddress + '\'' +
                ", prefixLength='" + prefixLength + '\'' +
                ", gateway='" + gateway + '\'' +
                ", dnsServer='" + dnsServer + '\'' +
                '}';
    }
}
