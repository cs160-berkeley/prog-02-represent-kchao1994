package com.example.katherine.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.katherine.mylibrary.ImageLoader;
import com.example.katherine.mylibrary.Person;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.ArrayList;

/**
 * Created by katherine on 01/03/2016.
 */
public class ListAdapter extends ArrayAdapter<Person> {
    private Context context;
    int layoutResource;

    public ListAdapter(Context context, int resource, ArrayList<Person> arrayOfPeople) {
        super(context, 0, arrayOfPeople);
        this.context = context;
        layoutResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Person person = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layoutResource, parent, false);
        }

        // Lookup view for data population
        TextView nameText = (TextView) convertView.findViewById(R.id.name_list);
        TextView websiteText = (TextView) convertView.findViewById(R.id.website_list);
        TextView senOrRepText = (TextView) convertView.findViewById(R.id.senOrRep_list);
        final LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.tweet_linear);

        // set text
        if (person != null) {
            //office
            if(person.getSenOrRep().equals("Rep")) {
                senOrRepText.setText("Representative");
            } else {
                senOrRepText.setText("Senator");
            }

            //name
            String partyTextDisplay = person.getParty();
            nameText.setText(person.getFirstName() + " " + person.getLastName() + " (" + partyTextDisplay + ")");

            // website
            websiteText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(person.getWebsite()));
                    context.startActivity(browserIntent);
                }
            });
            SpannableString content = new SpannableString(person.getWebsite());
            content.setSpan(new UnderlineSpan(), 0, person.getWebsite().length(), 0);
            websiteText.setText(content);

            // email
            TextView emailText = (TextView) convertView.findViewById(R.id.email_list);
            emailText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{person.getEmail()});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "mail body");
                    context.startActivity(Intent.createChooser(emailIntent, ""));
                }
            });
            SpannableString contentEmail = new SpannableString(person.getEmail());
            contentEmail.setSpan(new UnderlineSpan(), 0, person.getEmail().length(), 0);
            emailText.setText(contentEmail);

            //tweet
            ListView tweetList = (ListView) convertView.findViewById(R.id.tweet_list);
            final UserTimeline userTimeline = new UserTimeline.Builder()
                    .screenName(person.getTwitterHandle())
                    .maxItemsPerRequest(1)
                    .build();
            final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(context)
                    .setTimeline(userTimeline)
                    .build();
            tweetList.setAdapter(adapter);


            // image
            String imgUrl = "https://theunitedstates.io/images/congress/225x275/" + person.getId() + ".jpg";
            final ImageView imageView = (ImageView) convertView.findViewById(R.id.image_list);
            new ImageLoader(imageView)
                    .execute(imgUrl);

        }

        String buttonColor = "#000";
        // rep button
        if(person.getParty().equals("R")) {
            buttonColor = context.getString(R.string.republican_bg_color);
        } else {
            buttonColor = context.getString(R.string.democrat_bg_color);
        }
        Button repButton = (Button) convertView.findViewById(R.id.more_info);
        repButton.setBackgroundColor(Color.parseColor(buttonColor));

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