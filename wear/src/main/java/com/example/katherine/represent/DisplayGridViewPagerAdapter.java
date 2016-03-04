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

    public DisplayGridViewPagerAdapter(Context context, FragmentManager fm, ArrayList<Person> listOfPeople) {
        super(fm);
        this.mContext = context;
        this.listOfPeople = listOfPeople;
    }

    public Fragment getFragment(int row, int col) {
        //PersonFragment fragment = CardFragment.create("Card", listOfPeople.get(col).getFirstName());
        PersonFragment fragment = new PersonFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("person", listOfPeople.get(col));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int i) {
        return listOfPeople.size();
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
}