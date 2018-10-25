package com.example.keiji.contacts;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Keiji on 3/24/2018.
 */

public class Contact implements Parcelable {
    int _id;
    String _Last_Name = "";
    String _First_Name = "";
    String _Address_1 = "";
    String _Address_2 = "";
    String _City = "";
    String _State = "";
    String _Zip = "";
    String _Country = "";
    String _Phone_Number = "";
    String _Email = "";

    public Contact() {
    }

    public Contact(int _id, String _Last_Name, String _First_Name, String _Address_1, String _Address_2, String _City, String _State, String _Zip, String _Country, String _Phone_Number, String _Email) {
        this._id = _id;
        this._Last_Name = _Last_Name;
        this._First_Name = _First_Name;
        this._Address_1 = _Address_1;
        this._Address_2 = _Address_2;
        this._City = _City;
        this._State = _State;
        this._Zip = _Zip;
        this._Country = _Country;
        this._Phone_Number = _Phone_Number;
        this._Email = _Email;
    }

    public Contact(String _Last_Name, String _First_Name, String _Address_1, String _Address_2, String _City, String _State, String _Zip, String _Country, String _Phone_Number, String _Email) {
        this._Last_Name = _Last_Name;
        this._First_Name = _First_Name;
        this._Address_1 = _Address_1;
        this._Address_2 = _Address_2;
        this._City = _City;
        this._State = _State;
        this._Zip = _Zip;
        this._Country = _Country;
        this._Phone_Number = _Phone_Number;
        this._Email = _Email;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_Last_Name() {
        return _Last_Name;
    }

    public void set_Last_Name(String _Last_Name) {
        this._Last_Name = _Last_Name;
    }

    public String get_First_Name() {
        return _First_Name;
    }

    public void set_First_Name(String _First_Name) {
        this._First_Name = _First_Name;
    }

    public String get_Address_1() {
        return _Address_1;
    }

    public void set_Address_1(String _Address_1) {
        this._Address_1 = _Address_1;
    }

    public String get_Address_2() {
        return _Address_2;
    }

    public void set_Address_2(String _Address_2) {
        this._Address_2 = _Address_2;
    }

    public String get_City() {
        return _City;
    }

    public void set_City(String _City) {
        this._City = _City;
    }

    public String get_State() {
        return _State;
    }

    public void set_State(String _State) {
        this._State = _State;
    }

    public String get_Zip() {
        return _Zip;
    }

    public void set_Zip(String _Zip) {
        this._Zip = _Zip;
    }

    public String get_Country() {
        return _Country;
    }

    public void set_Country(String _Country) {
        this._Country = _Country;
    }

    public String get_Phone_Number() {
        return _Phone_Number;
    }

    public void set_Phone_Number(String _Phone_Number) {
        this._Phone_Number = _Phone_Number;
    }

    public String get_Email() {
        return _Email;
    }

    public void set_Email(String _Email) {
        this._Email = _Email;
    }

    public Contact(Parcel in) {
        this._id = in.readInt();
        this._Last_Name = in.readString();
        this._First_Name = in.readString();
        this._Address_1 = in.readString();
        this._Address_2 = in.readString();
        this._City = in.readString();
        this._State = in.readString();
        this._Zip = in.readString();
        this._Country = in.readString();
        this._Phone_Number = in.readString();
        this._Email = in.readString();;
    }
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(_id);
        out.writeString(_Last_Name);
        out.writeString(_First_Name);
        out.writeString(_Address_1);
        out.writeString(_Address_2);
        out.writeString(_City);
        out.writeString(_State);
        out.writeString(_Zip);
        out.writeString(_Country);
        out.writeString(_Phone_Number);
        out.writeString(_Email);
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {

        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int sum) {
            return new Contact[sum];
        }
    };

    @Override
    public String toString() {

        return this.get_Last_Name()+ ", "+ this.get_First_Name();
    }
}
