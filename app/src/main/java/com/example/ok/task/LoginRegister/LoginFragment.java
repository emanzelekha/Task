package com.example.ok.task.LoginRegister;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.ok.task.Fonts.TypefaceUtil;
import com.example.ok.task.LoginRegister.uitilt.AsyncHttpClient;
import com.example.ok.task.MainActivity;
import com.example.ok.task.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    View view = null;
    Button go, log;
    EditText name, pass;
    boolean out = true;

    public LoginFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        items();
        click();

        TypefaceUtil.overrideFonts(getActivity().getApplicationContext(), view);//font

        SharedPreferences pref = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
        String id = pref.getString("name", "");
        String tests = pref.getString("pass", "");
        name.setText(id);
        pass.setText(tests);


        return view;
    }

    public boolean cheak() {

        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("ادخل قيمة صحيحة");
            name.requestFocus();
            out = false;
        }
        if (TextUtils.isEmpty(pass.getText().toString())) {
            pass.setError("ادخل قيمة صحيحة");
            pass.requestFocus();
            out = false;
        }

        return out;
    }

    public void click() {
        go.setOnClickListener(this);
        log.setOnClickListener(this);
    }

    public void items() {
        go = (Button) view.findViewById(R.id.reg);
        log = (Button) view.findViewById(R.id.reg1);
        name = (EditText) view.findViewById(R.id.name);
        pass = (EditText) view.findViewById(R.id.pass);

    }

    public void AskLogin(RequestParams params) throws JSONException {

        AsyncHttpClient.post("login.php", params, new JsonHttpResponseHandler() {
            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("جارى البحث...");
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.e("onSuccess", response + "");
                try {
                    if (response.getInt("code") == 1) {
                        SharedPreferences sharedPref = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("name", name.getText().toString());
                        editor.putString("pass", pass.getText().toString());
                        editor.putString("UserId", response.getString("message"));
                        editor.commit();
                        Intent intent = new Intent(getActivity().getApplication(), MainActivity.class);
                        intent.putExtra("id", response.getString("message"));

                        startActivity(intent);

                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), "الاسم او كلمة المرور خظأ", Toast.LENGTH_LONG).show();

                    }
                    // String[] items = response.getString("message").split(",");

                } catch (Exception ex) {

                    Toast.makeText(getActivity().getApplicationContext(), "اشاره النت ضغيفه", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getActivity().getApplicationContext(), "onFailure", Toast.LENGTH_LONG).show();
                Log.e("onFailure", "----------" + responseString);

            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg:
                RegisterFragment register = new RegisterFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, register).commit();

                break;
            case R.id.reg1:
                if (cheak() != false) {

                    try {
                        RequestParams params = new RequestParams();
                        params.put("UserNme", name.getText().toString());
                        params.put("PassWord", pass.getText().toString());

                        AskLogin(params);
                    } catch (Exception ex) {
                        Toast.makeText(getActivity().getApplicationContext(), "Exception" + ex, Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }

    }
}





