package com.example.firebasejetpack;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class registerFragment extends Fragment {


     EditText edt_name ,edt_email ,edt_rpass ,edt_pass;
     Button btn_reg;
     FirebaseAuth auth;


    public registerFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        edt_name = view.findViewById(R.id.edt_rname);
        edt_pass = view.findViewById(R.id.edt_rpass);
        edt_rpass = view.findViewById(R.id.edt_rcpass);
        edt_email= view.findViewById(R.id.edt_remail);
        btn_reg =view.findViewById(R.id.btn_reg);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isCheckEmptyFiled()){

                 if(edt_pass.getText().toString().length()<6) {
                     edt_pass.setError("Invalid password ! Password should be at least  6 characters!");
                     edt_pass.requestFocus();

                 }else
                 {
                     if(!edt_pass.getText().toString().equals(edt_rpass.getText().toString())){

                         edt_rpass.setError("Password does not match");
                         edt_rpass.requestFocus();
                     }else
                     {
                         String name = edt_name.getText().toString();
                         String email = edt_email.getText().toString();
                         String pass = edt_pass.getText().toString();
                         createUser(name,email,pass);

                     }
                 }
                }else{



                }
            }
        });

    }


    public boolean  isCheckEmptyFiled(){

        if(TextUtils.isEmpty(edt_name.getText().toString())){

            edt_name.setError("Name can not be blank");
            edt_name.requestFocus();
            return true;

        }else if(TextUtils.isEmpty(edt_email.getText().toString())){
          //  System.out.print(edt_email);
            edt_email.setError("email can not be blank");
            edt_email.requestFocus();
            return true;

        }else if(TextUtils.isEmpty(edt_pass.getText().toString())){

            edt_pass.setError("pass can not be blank");
            edt_pass.requestFocus();
            return true;

        }else if(TextUtils.isEmpty(edt_rpass.getText().toString())) {

            edt_rpass.setError("confirm pass can not be blank");
            edt_rpass.requestFocus();
            return true;

        }
            return false;
    }

    public boolean isEmptyFiled(EditText edt_txt){

        if(TextUtils.isEmpty(edt_txt.toString())){

            edt_txt.setError("This filed can not be blank");
            edt_txt.requestFocus();
            return true;
        }


        return  false;
    }

    public void createUser(final String name ,final String email, final String password){

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    FirebaseUser user = auth.getCurrentUser();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Map<String, String> hashmap = new HashMap<>();

                    hashmap.put("name",name);
                    hashmap.put("email",email);

                     db.collection("users").document(user.getUid()).set(hashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void aVoid) {

                             Toast.makeText(getActivity().getApplicationContext(),"Register sucessfull",Toast.LENGTH_LONG).show();
                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Log.d("From registration:",e.getMessage());
                             Toast.makeText(getActivity().getApplicationContext(),"Register fail",Toast.LENGTH_LONG).show();
                         }
                     });

                }else
                {
                    Log.d("From reg", task.getException().toString());
                    Toast.makeText(getActivity().getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        });

        FirebaseAuth.getInstance().signOut();
        NavController navController = Navigation.findNavController(getActivity(), R.id.host_frag);
        navController.navigate(R.id.loginFragment);

    }
}

