package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static com.example.quizapp.MainActivity.key_list;

public class highscorelist extends AppCompatActivity {

    private String data = "";
    private TextView show;



    private FirebaseFirestore fdb = FirebaseFirestore.getInstance();
    private DocumentReference noteref = fdb.document("notebook/firstnote");
    private CollectionReference notebookref = fdb.collection("notebook");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscorelist);



        loadlist();





    }

    @Override
    public void onBackPressed(){
        show.setText("");
        finish();
    }


    public void loadlist(){

        notebookref.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        data = "";
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            list list = documentSnapshot.toObject(com.example.quizapp.list.class);

                            String name = list.getName();
                            String score = list.getScore();


                            data += name + "              " + score + "\n\n";

                        }
                        show = findViewById(R.id.scorename);
                        show.setText(data);


                    }

                });
    }
}
