package com.example.ok.task.LoginRegister;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;


import com.example.ok.task.R;


public class LoginRegActivity
        extends AppCompatActivity  {

    LoginFragment login;
    RegisterFragment register;

    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    String Go="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg);

        //////////////////////////////////
        login = new LoginFragment();
        register = new RegisterFragment();

        fragmentTransaction.add(R.id.fragment_container, login).commit();
    }


}


