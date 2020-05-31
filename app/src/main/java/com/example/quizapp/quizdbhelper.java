package com.example.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.quizapp.contract.*;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class quizdbhelper extends SQLiteOpenHelper {

    private static final String database_name = "thequiz.db";
    private static final int database_version = 1;
    private SQLiteDatabase db;

    public quizdbhelper(@Nullable Context context) {
        super(context, database_name, null , database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;
        final String SQL_create_questions_table = " CREATE TABLE "+
                questiontable.table_name + " ( " +
                questiontable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                questiontable.column_question + " TEXT," +
                questiontable.column_option1 + " TEXT," +
                questiontable.column_option2 + " TEXT," +
                questiontable.column_option3 + " TEXT," +
                questiontable.column_answer + " INTEGER " +
                ")";

        db.execSQL(SQL_create_questions_table);
        fillquestionstable();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        db.execSQL(" DROP TABLE IF EXISTS " + questiontable.table_name);
        onCreate(db);

    }
    private void fillquestionstable() {
        question q1 = new question("An octopus has", "3 hearts", "2 hearts", "5 hearts",1);
        addquestion(q1);
        question q2 = new question("This animal takes the most number of lives in a year", "Shark", "Tiger", "Mosquito",3);
        addquestion(q2);
        question q3 = new question("Hawaiian Pizza was first made in", "France", "Italy", "Canada",3);
        addquestion(q3);
        question q4 = new question("The most visited country in the world is", "France", "USA", "Spain",1);
        addquestion(q4);
        question q5 = new question("This country has the most islands ", "Greece", "Sweden", "Indonesia",2);
        addquestion(q5);

    }
    private void addquestion(question question){
        ContentValues cv = new ContentValues();
        cv.put(questiontable.column_question, question.getQuestion());
        cv.put(questiontable.column_option1, question.getOption1());
        cv.put(questiontable.column_option2, question.getOption2());
        cv.put(questiontable.column_option3, question.getOption3());
        cv.put(questiontable.column_answer, question.getAnswer());
        db.insert(questiontable.table_name,null , cv );


    }

    public List<question> getallquestions(){
        List<question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + questiontable.table_name , null);

        if (c.moveToFirst()){
            do {
                question question = new question();
                question.setQuestion(c.getString(c.getColumnIndex(questiontable.column_question)));
                question.setOption1(c.getString(c.getColumnIndex(questiontable.column_option1)));
                question.setOption2(c.getString(c.getColumnIndex(questiontable.column_option2)));
                question.setOption3(c.getString(c.getColumnIndex(questiontable.column_option3)));
                question.setAnswer(c.getInt(c.getColumnIndex(questiontable.column_answer)));
                questionList.add(question);


            } while (c.moveToNext());

        }
        c.close();
        return questionList;
    }
}
