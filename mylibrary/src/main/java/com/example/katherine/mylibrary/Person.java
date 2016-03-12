package com.example.katherine.mylibrary;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.wearable.DataMap;

import java.util.ArrayList;

/**
 * Created by katherine on 01/03/2016.
 */
@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Person implements Parcelable {
    private String id;
    private String senOrRep; // 0 for rep, 1 for senator
    private String firstName;
    private String lastName;
    private String party; // 0 for democrat, 1 for republican
    private String email;
    private String website;



    private String twitterHandle;
    private String latestTweet;
    private String termStart;
    private String termEnd;
    private ArrayList<String> bills;
    private ArrayList<String> committees;

    public Person() {
        this.bills = new ArrayList<String>();
        this.committees = new ArrayList<String>();
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getSenOrRep() {
        return senOrRep;
    }

    public void setSenOrRep(String senOrRep) {
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

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
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

    public String getTwitterHandle() { return twitterHandle; }

    public void setTwitterHandle(String twitterHandle) { this.twitterHandle = twitterHandle; }

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
        id = in.readString();
        senOrRep = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        party = in.readString();
        email = in.readString();
        website = in.readString();
        twitterHandle = in.readString();
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
        dest.writeString(id);
        dest.writeString(senOrRep);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(party);
        dest.writeString(email);
        dest.writeString(website);
        dest.writeString(twitterHandle);
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
        map.putString("id", id);
        map.putString("firstName", firstName);
        map.putString("lastName", lastName);
        map.putString("party", party);
        map.putString("senOrRep", senOrRep);
        map.putString("email", email);
        map.putString("website", website);
        map.putString("twitterHandle", latestTweet);
        map.putString("latestTweet", latestTweet);
        map.putString("termStart", termStart);
        map.putString("termEnd", termEnd);
        map.putStringArrayList("bills", bills);
        map.putStringArrayList("committees", committees);

        return map;
    }

    public void getFromDataMap(DataMap map) {
        this.setId(map.getString("id"));
        this.setFirstName(map.getString("firstName"));
        this.setLastName(map.getString("lastName"));
        this.setParty(map.getString("party"));
        this.setSenOrRep(map.getString("senOrRep"));
        this.setEmail(map.getString("email"));
        this.setWebsite(map.getString("website"));
        this.setTwitterHandle(map.getString("twitterHandle"));
        this.setLatestTweet(map.getString("latestTweet"));
        this.setTermStart(map.getString("termStart"));
        this.setTermEnd(map.getString("termEnd"));
        this.setCommittees(map.getStringArrayList("committees"));
        this.setBills(map.getStringArrayList("bills"));

    }

    public String toString() {
        String result = new String();
        result = result + "ID: " + id + "\n";
        result = result + "First Name: " + firstName + "\n";
        result = result + "Last Name: " + lastName + "\n";
        result = result + "Party: " + party + "\n";
        result = result + "senOrRep: " + senOrRep + "\n";
        result = result + "Email: " + email + "\n";
        result = result + "Website: " + website + "\n";
        result = result + "Twitter Handle: " + twitterHandle + "\n";
        result = result + "Latest Tweet: " + latestTweet + "\n";
        result = result + "Term Start: " + termStart + "\n";
        result = result + "Term End: " + termEnd + "\n";
        result = result + "Committees: " + "\n";
        for(int i = 0; i < committees.size(); i++) {
            result = result + "     " + committees.get(i) + "\n";
        }
        result = result + "Bills: " + "\n";
        for(int i = 0; i < bills.size(); i++) {
            result = result + "     " + bills.get(i) + "\n";
        }
        return result;
    }
}