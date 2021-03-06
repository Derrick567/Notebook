package com.example.derrick.notebook;



import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;



/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {

    private ImageButton noteCatButton;
    private EditText title;
    private EditText message;
    private Note.Category savedButtonCategory;
    private AlertDialog categoryDialogObject;
    private AlertDialog confirmDialogObject;
    private static final String MODIFIED_CATEGORY="Modified Category";

    private boolean newNote=false;
    private long id =0;


    public NoteEditFragment() {
        // Required empty public constructor
    }

    //exucute every single time while change orientation
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if(bundle!=null){
            newNote=bundle.getBoolean(NoteDetailActivity.NEW_NOTE_EXTRA,false);
        }

        if(savedInstanceState!=null){
            savedButtonCategory=(Note.Category)savedInstanceState.getSerializable(MODIFIED_CATEGORY);
        }

        View layout = inflater.inflate(R.layout.fragment_note_edit, container, false);
        title = (EditText) layout.findViewById(R.id.editNoteTitle);
        message = (EditText) layout.findViewById(R.id.editNoteMessage);
        noteCatButton = (ImageButton) layout.findViewById(R.id.editNoteButton);
        Button saveButton = (Button) layout.findViewById(R.id.saveNote);
        Intent intent = getActivity().getIntent();

        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE_EXTRA,""));
        message.setText(intent.getExtras().getString(MainActivity.NOTE_MESSAGE_EXTRA,""));
        id=intent.getExtras().getLong(MainActivity.NOTE_ID_EXTRA,0);
        if(newNote){
            title.setHint(getString(R.string.title_hint));
            message.setHint(getString(R.string.content_hint));
        }

        if(savedButtonCategory!=null){
            noteCatButton.setImageResource(Note.categoryToDrawable(savedButtonCategory));
        }else if(!newNote){
        Note.Category noteCat = (Note.Category) intent.getSerializableExtra(MainActivity.NOTE_CATEGORY_EXTRA);
        savedButtonCategory = noteCat;
        noteCatButton.setImageResource(Note.categoryToDrawable(noteCat));
        }

        buildCategoryDialog();
        buildConfirmDialog();
        noteCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialogObject.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogObject.show();
            }
        });

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MODIFIED_CATEGORY,savedButtonCategory);
    }

    private void buildCategoryDialog() {
        final String[] categories = new String[]{"Personal", "Technical", "Quote", "FINACIAL"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Note Type")
                .setSingleChoiceItems(categories, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss our dialog window;
                        categoryDialogObject.cancel();
                        switch (which) {
                            case 0:
                                savedButtonCategory = Note.Category.PERSONAL;
                                noteCatButton.setImageResource(R.mipmap.p);
                                break;
                            case 1:
                                savedButtonCategory = Note.Category.TECHNICAL;
                                noteCatButton.setImageResource(R.mipmap.t);
                                break;
                            case 2:
                                savedButtonCategory = Note.Category.QUOTE;
                                noteCatButton.setImageResource(R.mipmap.q);
                                break;
                            case 3:
                                savedButtonCategory = Note.Category.FINANCE;
                                noteCatButton.setImageResource(R.mipmap.f);
                                break;
                        }
                    }
                });
        categoryDialogObject = builder.create();

    }

    private void buildConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure?")
                .setMessage("Are you sure you want to save the note?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Save note", "note title = " + title.getText() + "Note message :" +
                                "" + message.getText() + "Note category =" + savedButtonCategory);

                        NotebookAdapter dbAdapter =new NotebookAdapter(getActivity());
                        dbAdapter.open();
                        if(newNote){
                            dbAdapter.createNote(title.getText().toString(),message.getText().toString(),
                                    (savedButtonCategory==null)?Note.Category.PERSONAL:savedButtonCategory);
                        }else{
                            dbAdapter.updateNote(id,title.getText().toString(),message.getText().toString(),savedButtonCategory);
                        }
                        dbAdapter.close();

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing here;
                    }
                });
        confirmDialogObject = builder.create();
    }
}
