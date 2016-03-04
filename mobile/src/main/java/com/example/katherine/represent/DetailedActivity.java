package com.example.katherine.represent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.katherine.mylibrary.Person;


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
        TextView billsText = (TextView) findViewById(R.id.billsList);
        TextView committeesText = (TextView) findViewById(R.id.commiteesList);


        if (person != null) {
            nameText.setText(person.getFirstName() + " " + person.getLastName());

            // party
            String partyTextDisplay;
            if(!person.getParty()) {
                partyTextDisplay = "Democratic Party";
            } else {
                partyTextDisplay = "Republican Party";
            }
            partyText.setText(partyTextDisplay);

            termText.setText("Term: " + person.getTermStart() + " - " + person.getTermEnd());

            // website
            websiteText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(person.getWebsite()));
                    startActivity(browserIntent);
                }
            });

            // TODO: email
            // TODO: bills
            // TODO: committees

        }

    }
}
