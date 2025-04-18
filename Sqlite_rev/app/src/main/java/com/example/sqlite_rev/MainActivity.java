package com.example.sqlite_rev;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText userName, password, getUserName;

    Button save_btn ;

    Button view_btn, get_btn , upd_btn, del_btn;
    sqlDataBaseAdapter sqlHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName=(EditText) findViewById(R.id.userNameValue);
        password=(EditText) findViewById(R.id.passwordValue);
        getUserName = findViewById(R.id.edt_name);
        save_btn=(Button) findViewById(R.id.button);
        view_btn=(Button) findViewById(R.id.button2);
        get_btn = findViewById(R.id.button3);
        upd_btn = (Button) findViewById(R.id.ntn_upd);
        del_btn = (Button) findViewById(R.id.button7);



        sqlHelper=new sqlDataBaseAdapter(this);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser(v);
            }
        });

        view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewDetails(v);
            }
        });

        get_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetails(v);
            }
        });

        upd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(v);
            }
        });

        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(v);
            }
        });
    }

    private void delete(View v) {
        sqlHelper.deleteName("awadin");

    }

    public void addUser(View view)
    {
        String user=userName.getText().toString();
        String pass=password.getText().toString();

        long id=sqlHelper.insertData(user, pass);
        if(id<0)
        {
            Message.message(this, "Unsuccessfull");
        }
        else
        {
            Message.message(this, "Successfully Inserted A Row");
        }
    }

    public void viewDetails(View v)
    {
        String data = sqlHelper.getAllData();
        Message.message(this,data);
    }

//    public void getDetails(){
//        String s1= getUserName.getText().toString();
//        String s2 = sqlHelper.getData(s1);
//        Message.message(this,s2);
//    }

    public void getDetails(View v){
        String s1= getUserName.getText().toString();
        String sub1 = s1.substring(0,s1.indexOf(" "));
        String sub2 = s1.substring(s1.indexOf(" ")+1);
        String s3 = sqlHelper.getData(sub1,sub2);
        Message.message(this,s3);
    }

    public void update(View view){
        sqlHelper.updateName("aaaa", "awadin");
    }

}