package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    public static  final String key_name = "Name";
    private EditText editTextname;

    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    private DocumentReference noteref = fdb.document("notebook/firstnote");
    private CollectionReference notebookref = fdb.collection("notebook");

    private  static final int request_code_quiz = 1;

    public static final String shared_prefs = "sharedprefs";
    public static final String key_highscore = "highscore";

    public static final String key_list = "list";

    private TextView textViewhighscore;
    private int highscore;


    public String data = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextname = findViewById(R.id.editname);

        textViewhighscore = findViewById(R.id.highscore);
        loadhighscore();



        Button Buttonstart = findViewById(R.id.button_start);
        Buttonstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startquiz();
            }
        });
    }
    private void startquiz(){
        Intent intent = new Intent(MainActivity.this, quizactivity.class);
        startActivityForResult(intent, request_code_quiz);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == request_code_quiz);
        if(resultCode == RESULT_OK){
            int score = data.getIntExtra(quizactivity.extra_score,0);
            if(score > highscore){
                updatehighscore(score);
            }
        }







    }

    private void loadhighscore(){
        SharedPreferences prefs = getSharedPreferences(shared_prefs, MODE_PRIVATE);
        highscore = prefs.getInt(key_highscore, 0 );
        textViewhighscore.setText("Highscore:" + highscore);

    }

    private void updatehighscore(int highscorenew){

        highscore = highscorenew;
        textViewhighscore.setText("Highscore:" + highscore);

        SharedPreferences prefs = getSharedPreferences(shared_prefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key_highscore,highscore);
        editor.apply();

    }


    public void savenote(View v){
        String name = editTextname.getText().toString();
        String stringscore = String.valueOf(highscore);
        list list = new list(name , stringscore);

        notebookref.add(list)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "failed to update", Toast.LENGTH_SHORT).show();
                    }
                });


    }





    public void loadscorelist(View v){

        Intent intent = new Intent(MainActivity.this, highscorelist.class);

        startActivity(intent);



    }


    public void refresh(View v){
        updatehighscore(0);
    }
}
