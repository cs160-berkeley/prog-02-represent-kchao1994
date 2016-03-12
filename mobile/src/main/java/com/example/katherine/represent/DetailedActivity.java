package com.example.katherine.represent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.katherine.mylibrary.ImageLoader;
import com.example.katherine.mylibrary.Person;

import java.util.ArrayList;


/**
 * Created by katherine on 29/02/2016.
 */
public class DetailedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed);

        // get person data
        Intent receivedIntent = getIntent();
        Bundle data = receivedIntent.getExtras();
        final Person person = (Person) data.getParcelable("person");

        // get layout objects
        TextView nameText = (TextView) findViewById(R.id.name_detailed);
        TextView partyText = (TextView) findViewById(R.id.party_detailed);
        TextView termText = (TextView) findViewById(R.id.term_detailed);
        TextView websiteText = (TextView) findViewById(R.id.website_detailed);
        TextView emailText = (TextView) findViewById(R.id.email_detailed);
        ListView billsList = (ListView) findViewById(R.id.bills_detailed);
        ListView committeesList = (ListView) findViewById(R.id.committees_detailed);


        if (person != null) {
            nameText.setText(person.getSenOrRep() + ". " + person.getFirstName() + " " + person.getLastName());

            // party
            String partyTextDisplay;
            if(person.getParty().equals("D")) {
                partyTextDisplay = "Democratic Party";
                nameText.setBackgroundColor(Color.parseColor(getApplication().getString(R.string.democrat_bg_color)));
            } else {
                partyTextDisplay = "Republican Party";
                nameText.setBackgroundColor(Color.parseColor(getApplication().getString(R.string.republican_bg_color)));
            }
            partyText.setText(partyTextDisplay);

            // term
            termText.setText("In office from " + person.getTermStart() + " to " + person.getTermEnd());

            // website
            websiteText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(person.getWebsite()));
                    startActivity(browserIntent);
                }
            });
            SpannableString content = new SpannableString(person.getWebsite());
            content.setSpan(new UnderlineSpan(), 0, person.getWebsite().length(), 0);
            websiteText.setText(content);

            // email
            emailText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{person.getEmail()});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "mail body");
                    startActivity(Intent.createChooser(emailIntent, ""));
                }
            });
            SpannableString contentEmail = new SpannableString(person.getEmail());
            contentEmail.setSpan(new UnderlineSpan(), 0, person.getEmail().length(), 0);
            emailText.setText(contentEmail);


            // committees
            ArrayList<String> committeesListData = person.getCommittees();
            ArrayAdapter<String> committeesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, committeesListData);
            committeesList.setAdapter(committeesAdapter);
            committeesList.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            committeesList.requestLayout();

            // bills
            ArrayList<String> billsListData = person.getBills();
            ArrayAdapter<String> billsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, billsListData);
            billsList.setAdapter(billsAdapter);


            // image
            String imgUrl = "https://theunitedstates.io/images/congress/225x275/" + person.getId() + ".jpg";
            final ImageView imageView = (ImageView) findViewById(R.id.image_detailed);
            new ImageLoader(imageView).execute(imgUrl);

        }

    }

    public void justifyListView(ListView listView) {

        ListAdapter adapter = (ListAdapter) listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }
}
