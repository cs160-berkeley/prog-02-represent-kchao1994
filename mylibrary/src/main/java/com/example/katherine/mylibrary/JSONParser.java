package com.example.katherine.mylibrary;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    private static final String SUNLIGHT_API_KEY = "a453ee25cbc24eafa7688c060e801306";
    private static final String GOOGLE_MAPS_API_KEY = "AIzaSyDghfgouOGfPShm6ObTWPj1iWGVFIOv7hg";

    public String randomLocation(Context context) throws IOException {
        String jsonString = null;
        JSONArray json = null;
        String result = null;
        try {
            InputStream is2 = context.getResources().getAssets().open("election-county-2012.json");
            int size = is2.available();
            byte[] buffer = new byte[size];
            is2.read(buffer);
            is2.close();
            jsonString = new String(buffer, "UTF-8");
            json = new JSONArray(jsonString);

            int Min = 0;
            int Max = json.length();
            int randomInt = Min + (int)(Math.random() * ((Max - Min) + 1));
            result = json.getJSONObject(randomInt).getString("county-name");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Double> getLatLongFromRandom(String county) {
        ArrayList<Double> result = new ArrayList<Double>();
        try {
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + county + ",USA" +
                    "&key=" + GOOGLE_MAPS_API_KEY;
            Log.d("url:", url);

            HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
            try {
                InputStream in = c.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String mapsString = sb.toString();
                JSONObject json = new JSONObject(mapsString);
                Log.d("random json", json.toString());

                JSONObject location = json.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                result.add(location.getDouble("lat"));
                result.add(location.getDouble("lng"));
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
            } finally {
                c.disconnect();
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
        }
        return result;
    }

    public JSONArray get2012Data(Context context) {
        String jsonString = null;
        JSONArray json = null;
        try {
            InputStream is2 = context.getResources().getAssets().open("election-county-2012.json");
            int size = is2.available();
            byte[] buffer = new byte[size];
            is2.read(buffer);
            is2.close();
            jsonString = new String(buffer, "UTF-8");
            json = new JSONArray(jsonString);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }



    public ArrayList<String> getCountyFromLocation(String zipCode, Double latitude, Double longitude, Context context) throws IOException {
        ArrayList<String> result = null;
        String url = "";
        try {
            if(zipCode != null) {
                url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + zipCode +
                        "&key=" + GOOGLE_MAPS_API_KEY;
            } else {
                url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude +
                        "&key=" + GOOGLE_MAPS_API_KEY;
            }
            Log.d("url:", url);

            HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
            try {
                InputStream in=c.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String mapsString = sb.toString();
                JSONObject json = new JSONObject(mapsString);
                JSONArray addressComponents = json.getJSONArray("results").getJSONObject(0).getJSONArray("address_components");
                String county = null;
                for(int i = 0; i < addressComponents.length(); i++) {
                    JSONObject curr = addressComponents.getJSONObject(i);
                    JSONArray types = curr.getJSONArray("types");
                    for(int k = 0; k < types.length(); k++) {
                        if(types.getString(k).equals("administrative_area_level_2")) {
                            county = curr.getString("short_name");
                        }
                    }
                }
                county = county.replace(" County", "");

                JSONArray countyData = get2012Data(context);
                JSONObject countyObject = null;
                ArrayList<JSONObject> countyDataArrayList = new ArrayList<JSONObject>();
                for(int i = 0; i < countyData.length(); i++) {
                    JSONObject curr = countyData.getJSONObject(i);
                    if(curr.getString("county-name").equals(county)) {
                        countyDataArrayList.add(curr);
                    }
                }
                if(countyDataArrayList.get(0) != null) {
                    countyObject = countyDataArrayList.get(0);
                }

                result = getVotes(countyObject);


                return result;

            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
            } finally {
                c.disconnect();
            }
        } catch(Exception e) {
            Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
        }
        return result;
    }

    public ArrayList<String> getVotes(JSONObject county) {
        ArrayList<String> result = new ArrayList<String>();
        try {
            result.add(Long.toString(county.getLong("obama-percentage")));
            result.add(Long.toString(county.getLong("romney-percentage")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Person> run(String url) throws IOException {
        ArrayList<Person> results = new ArrayList<Person>();
        try {
            HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
            try {
                InputStream in=c.getInputStream();
                results = readJsonPersonStream(in);
            }
            catch (IOException e) {
                Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
            }
            finally {
                c.disconnect();
            }
        }
        catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
        }

        // get committees
        for(int i = 0; i < results.size(); i++) {
            ArrayList<String> committeeList = new ArrayList<String>();
            Person currPerson = results.get(i);
            String bioId = currPerson.getId();
            String committeeRequestUrl = "http://congress.api.sunlightfoundation.com/committees?member_ids=" +
                    bioId + "&apikey=" + SUNLIGHT_API_KEY;
            try {
                HttpURLConnection c = (HttpURLConnection) new URL(committeeRequestUrl).openConnection();
                try {
                    InputStream in=c.getInputStream();
                    committeeList = readJsonCommitteeStream(in);
                }
                catch (IOException e) {
                    Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
                }
                finally {
                    c.disconnect();

                }
            }
            catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
            }

            currPerson.setCommittees(committeeList);
        }

        // get bills
        for(int i = 0; i < results.size(); i++) {
            ArrayList<String> billList = new ArrayList<String>();
            Person currPerson = results.get(i);
            String bioId = currPerson.getId();
            String billRequestUrl = "http://congress.api.sunlightfoundation.com/bills?sponsor_id=" +
                    bioId + "&apikey=" + SUNLIGHT_API_KEY;
            try {
                HttpURLConnection c = (HttpURLConnection) new URL(billRequestUrl).openConnection();
                try {
                    InputStream in=c.getInputStream();
                    billList = readJsonBillStream(in);
                }
                catch (IOException e) {
                    Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
                }
                finally {
                    c.disconnect();

                }
            }
            catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
            }

            currPerson.setBills(billList);
        }
        return results;
    }

    public ArrayList<Person> readJsonPersonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        ArrayList<Person> results = new ArrayList<Person>();
        reader.beginObject(); // results
        reader.nextName(); // results

        try {
            reader.beginArray();
            while(reader.hasNext()) {
                Person person = readPeople(reader);
                results.add(person);
            }
            reader.endArray();

            return results;
        }
        finally {
            reader.close();
        }
    }

    public Person readPeople(JsonReader reader) throws IOException {
        Person result = new Person();

        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals("bioguide_id")) {
                result.setId(reader.nextString());
            }
            else if(name.equals("first_name")) {
                result.setFirstName(reader.nextString());
            }
            else if(name.equals("last_name")) {
                result.setLastName(reader.nextString());
            }
            else if(name.equals("party")) {
                result.setParty(reader.nextString());
            }
            else if(name.equals("title")) {
                result.setSenOrRep(reader.nextString());
            }
            else if(name.equals("oc_email")) {
                result.setEmail(reader.nextString());
            }
            else if(name.equals("website")) {
                result.setWebsite(reader.nextString());
            }
            else if(name.equals("twitter_id")) {
                result.setTwitterHandle(reader.nextString());
            }
            else if(name.equals("term_start")) {
                result.setTermStart(convertToBetterDate(reader.nextString()));
            }
            else if(name.equals("term_end")) {
                result.setTermEnd(convertToBetterDate(reader.nextString()));
            }

            else {
                reader.skipValue();
            }
        }

        reader.endObject();

        return result;
    }

    public String convertToBetterDate(String strDate) {
        String result = "";
        try {
            SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdfSource.parse(strDate);
            SimpleDateFormat sdfDestination = new SimpleDateFormat("MMM d, yyyy");
            result = sdfDestination.format(date);
        } catch(ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public ArrayList<String> readJsonCommitteeStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        ArrayList<String> results = new ArrayList<String>();
        reader.beginObject(); // results
        reader.nextName(); // results

        try {
            reader.beginArray();
            while(reader.hasNext()) {
                String committee = readCommittees(reader);
                results.add(committee);
            }
            reader.endArray();

            return results;
        }
        finally {
            reader.close();
        }
    }

    public String readCommittees (JsonReader reader) throws IOException {
        String committee = null;

        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals("name")) {
                committee = reader.nextString();
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return committee;
    }

    public ArrayList<String> readJsonBillStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        ArrayList<String> results = new ArrayList<String>();
        reader.beginObject(); // results
        reader.nextName(); // results

        try {
            reader.beginArray();
            while(reader.hasNext()) {
                String bill = readBills(reader);
                if(bill != null) {
                    results.add(bill);
                }
            }
            reader.endArray();

            return results;
        }
        finally {
            reader.close();
        }
    }

    public String readBills (JsonReader reader) throws IOException {
        String bill = null;

        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            if(name.equals("introduced_on")) {
                String strDate = reader.nextString();
                bill = convertToBetterDate(strDate);
            }
            else if(name.equals("short_title")) {
                if(reader.peek().equals(JsonToken.STRING)) {
                    bill += " " + reader.nextString();
                } else {
                    bill = null;
                    reader.skipValue();
                }
            }

            else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return bill;
    }



    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}