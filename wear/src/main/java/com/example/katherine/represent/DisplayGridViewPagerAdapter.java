package com.example.katherine.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.katherine.mylibrary.Person;

import java.util.ArrayList;

public class DisplayGridViewPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private ArrayList<Person> listOfPeople;
    private String location;
    private String obamaVote;
    private String romneyVote;

    public DisplayGridViewPagerAdapter(Context context, FragmentManager fm, ArrayList<Person> listOfPeople, String location) {
        super(fm);
        this.mContext = context;
        this.listOfPeople = listOfPeople;
        this.location = location;
        setVotes();
    }

    public Fragment getFragment(int row, int col) {
        if(col < listOfPeople.size()) {
            PersonFragment fragment = new PersonFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("person", listOfPeople.get(col));
            fragment.setArguments(bundle);
            return fragment;
        }
        else { // election view
            VoteViewFragment electionView = new VoteViewFragment();
            Bundle bundle = new Bundle();
            bundle.putString("location", location);
            bundle.putString("obamaVote", obamaVote);
            bundle.putString("romneyVote", romneyVote);
            electionView.setArguments(bundle);
            return electionView;
        }

    }

    private void setVotes() {
        if(location.equals("Berkeley")) {
            obamaVote = "75";
            romneyVote = "25";
        }

        else if(location.equals("95757")) {
            obamaVote = "40";
            romneyVote = "60";
        }

        else {
            obamaVote = "50";
            romneyVote = "50";
        }
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int i) {
        return listOfPeople.size() + 1;
    }



    public static class PersonFragment extends CardFragment {
        @Override
        public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = (View) inflater.inflate(R.layout.person_card, container, false);
            TextView textView = (TextView) view.findViewById(R.id.name_card);
            final Context context = container.getContext();

            final Person person = this.getArguments().getParcelable("person");
            textView.setText(person.getFirstName() + " " + person.getLastName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, WatchToPhoneService.class);
                    i.putExtra("person", person);
                    context.startService(i);
                }
            });
            return view;
        }
    }



    public static class VoteViewFragment extends CardFragment {
        @Override
        public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = (View) inflater.inflate(R.layout.election_results, container, false);
            final Context context = container.getContext();
            Bundle data = this.getArguments();

            TextView locationTextView = (TextView) view.findViewById(R.id.location_election);
            locationTextView.setText(data.getString("location"));

            TextView obamaTextView = (TextView) view.findViewById(R.id.vote_obama);
            obamaTextView.setText(data.getString("obamaVote") + "% Obama");

            TextView romneyTextView = (TextView) view.findViewById(R.id.vote_romney);
            romneyTextView.setText(data.getString("romneyVote") + "% Romney");

            return view;
        }
    }
}