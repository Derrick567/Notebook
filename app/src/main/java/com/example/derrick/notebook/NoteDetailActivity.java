package com.example.derrick.notebook;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NoteDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        createAndAddFragment();
    }


    private void createAndAddFragment(){

        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        setTitle(R.string.viewFragmentTitle);
        NoteViewFragment fragment = new NoteViewFragment();
        fragmentTransaction.add(R.id.note_container,fragment,"NOTE_VIEW_FRAGMENT");
        fragmentTransaction.commit();


    }
}