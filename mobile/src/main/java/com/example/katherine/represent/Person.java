package com.example.katherine.represent;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.wearable.DataMap;

import java.util.ArrayList;

/**
 * Created by katherine on 01/03/2016.
 */
@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Person implements Parcelable {
    private Boolean senOrRep; // 0 for rep, 1 for senator
    private String firstName;
    private String lastName;
    private Boolean party; // 0 for democrat, 1 for republican
    private String email;
    private String website;
    private String latestTweet;
    private String termStart;
    private String termEnd;
    private ArrayList<String> bills;
    private ArrayList<String> committees;

    public Person() {
        this.bills = new ArrayList<String>();
        this.committees = new ArrayList<String>();
    }


    public Boolean getSenOrRep() {
        return senOrRep;
    }

    public void setSenOrRep(Boolean senOrRep) {
        this.senOrRep = senOrRep;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getParty() {
        return party;
    }

    public void setParty(Boolean party) {
        this.party = party;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLatestTweet() {
        return latestTweet;
    }

    public void setLatestTweet(String latestTweet) {
        this.latestTweet = latestTweet;
    }

    public String getTermStart() {
        return termStart;
    }

    public void setTermStart(String termStart) {
        this.termStart = termStart;
    }

    public String getTermEnd() {
        return termEnd;
    }

    public void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
    }

    public ArrayList<String> getBills() {
        return bills;
    }

    public void setBills(ArrayList<String> bills) {
        this.bills = bills;
    }

    public ArrayList<String> getCommittees() {
        return committees;
    }

    public void setCommittees(ArrayList<String> committees) {
        this.committees = committees;
    }




    protected Person(Parcel in) {
        byte senOrRepVal = in.readByte();
        senOrRep = senOrRepVal == 0x02 ? null : senOrRepVal != 0x00;
        firstName = in.readString();
        lastName = in.readString();
        byte partyVal = in.readByte();
        party = partyVal == 0x02 ? null : partyVal != 0x00;
        email = in.readString();
        website = in.readString();
        latestTweet = in.readString();
        termStart = in.readString();
        termEnd = in.readString();
        if (in.readByte() == 0x01) {
            bills = new ArrayList<String>();
            in.readList(bills, String.class.getClassLoader());
        } else {
            bills = null;
        }
        if (in.readByte() == 0x01) {
            committees = new ArrayList<String>();
            in.readList(committees, String.class.getClassLoader());
        } else {
            committees = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (senOrRep == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (senOrRep ? 0x01 : 0x00));
        }
        dest.writeString(firstName);
        dest.writeString(lastName);
        if (party == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (party ? 0x01 : 0x00));
        }
        dest.writeString(email);
        dest.writeString(website);
        dest.writeString(latestTweet);
        dest.writeString(termStart);
        dest.writeString(termEnd);
        if (bills == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(bills);
        }
        if (committees == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(committees);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public DataMap putToDataMap(DataMap map) {
        map.putString("firstName", firstName);
        map.putString("lastName", lastName);
        return map;
    }
}