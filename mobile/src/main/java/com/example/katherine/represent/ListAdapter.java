package com.example.katherine.represent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.katherine.mylibrary.Person;

import java.util.ArrayList;

/**
 * Created by katherine on 01/03/2016.
 */
public class ListAdapter extends ArrayAdapter<Person> {
    private Context context;

    public ListAdapter(Context context, ArrayList<Person> arrayOfPeople) {
        super(context, 0, arrayOfPeople);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Person person = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_single, parent, false);
        }

        // Lookup view for data population
        TextView nameText = (TextView) convertView.findViewById(R.id.name_list);
        TextView websiteText = (TextView) convertView.findViewById(R.id.website_list);
        TextView tweet = (TextView) convertView.findViewById(R.id.latest_tweet);

        // set text
        if (person != null) {
            //party
            String partyTextDisplay;
            if(person.getParty()) {
                partyTextDisplay = "R";
            } else {
                partyTextDisplay = "D";
            }

            // senator or representative
            String officeTypeDisplay;
            if(person.getSenOrRep()) {
                officeTypeDisplay = "Sen.";
            } else {
                officeTypeDisplay = "Rep.";
            }
            nameText.setText(officeTypeDisplay + " " + person.getFirstName() + " " + person.getLastName() + " (" + partyTextDisplay + ")");

            // website
            websiteText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(person.getWebsite()));
                    context.startActivity(browserIntent);
                }
            });

            // TODO: email

            //tweet
            tweet.setText(person.getLatestTweet());

        }


        Button repButton = (Button) convertView.findViewById(R.id.more_info);
        repButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // transition to list view
                Intent detailedIntent = new Intent(context, DetailedActivity.class);
                detailedIntent.putExtra("person", person);
                context.startActivity(detailedIntent);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}