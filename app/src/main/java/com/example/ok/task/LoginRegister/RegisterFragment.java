package com.example.ok.task.LoginRegister;

import android.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ok.task.Fonts.TypefaceUtil;
import com.example.ok.task.LoginRegister.uitilt.AsyncHttpClient;
import com.example.ok.task.Map.MainActivity;
import com.example.ok.task.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    boolean out = true;
    View view = null;
    TextView t1;
    Button inter;
    EditText name, pass, phone, email;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);
        TypefaceUtil.overrideFonts(getActivity(), view);

        Items();
        click();


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
        if (TextUtils.isEmpty(email.getText())|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
            email.setError("ادخل قيمة صحيحة");
            email.requestFocus();
            out = false;
        }
        if (TextUtils.isEmpty( phone.getText().toString())) {
            phone.setError("ادخل قيمة صحيحة");
            phone.requestFocus();
            out = false;
        }


        return out;
    }


    public void Items() {
        t1 = (TextView) view.findViewById(R.id.t1);
        inter = (Button) view.findViewById(R.id.inter);
        name = (EditText) view.findViewById(R.id.name);
        pass = (EditText) view.findViewById(R.id.pass);
        phone = (EditText) view.findViewById(R.id.phone);
        email = (EditText) view.findViewById(R.id.email);
    }

    public void click() {
        inter.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inter:
                if (cheak() != false) {


                    try {
                        RequestParams params = new RequestParams();
                        params.put("choose", "2");
                        params.put("UserNme", name.getText().toString());
                        params.put("PassWord", pass.getText().toString());
                        params.put("email", email.getText().toString());
                        params.put("phone", phone.getText().toString());

                        Register(params);
                    } catch (Exception ex) {
                        Toast.makeText(getActivity().getApplicationContext(), "Exception" + ex, Toast.LENGTH_LONG).show();
                    }
                }

                break;

    }}


    public void Register(RequestParams params) throws JSONException {

        AsyncHttpClient.post("register.php", params, new JsonHttpResponseHandler() {
            ProgressDialog progressDialog;

            @Override
            public void onStart() {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setCancelable(false);
                progressDialog.setMessage("يتم التسجيل...");
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.e("onSuccess", response + "");
                try {
                    if (response.getString("choose").equals("2") && response.getInt("code") == 1) {
                        Toast.makeText(getActivity().getApplicationContext(), "تم التسجيل نجاح", Toast.LENGTH_LONG).show();

                        /////////////////////////Data/////////////
                        SharedPreferences sharedPref = getActivity().getSharedPreferences("Data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("name", name.getText().toString());
                        editor.putString("pass", pass.getText().toString());
                        editor.putString("UserId", response.getString("message"));

                        editor.commit();
                        Intent intent = new Intent(getActivity(), MainActivity.class);


                        startActivity(intent);

                    } else if (response.getInt("code") == 0 && response.getString("choose").equals("0")) {

                        Toast.makeText(getActivity().getApplicationContext(), "هذا الايميل موجود بالفعل استخدم اخر.", Toast.LENGTH_LONG).show();

                    }

                } catch (Exception ex) {

                    Toast.makeText(getActivity().getApplicationContext(), "اشاره النت ضغيفه", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("onFailure", "----------" + responseString);

            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        });


    }

}
