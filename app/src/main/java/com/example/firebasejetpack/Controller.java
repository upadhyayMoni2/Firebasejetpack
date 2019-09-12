package com.example.firebasejetpack;

import android.app.Activity;
import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class Controller {

    public void navigateToFragment(int frgID , Activity cutAct , Bundle bundle){

        NavController navController = Navigation.findNavController(cutAct , R.id.host_frag);
        navController.navigate(frgID , bundle);
    }

}
