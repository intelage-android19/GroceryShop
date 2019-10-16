package com.efunhub.groceryshop.validator;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidatorHelper {


    public boolean isValidEmail(String eamailId){
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(eamailId);
        return matcher.matches();
    }

    public boolean isValidPassword(String password, boolean allowSpecialChars){
        String PATTERN;
        if(allowSpecialChars){
            //PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
            PATTERN = "^[a-zA-Z@#$%]\\w{5,19}$";
        }else{
            //PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
            PATTERN = "^[a-zA-Z]\\w{5,19}$";
        }



        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean isValidMobile(String mobileNo){

        //String phoneRegex = "^[6-9][0-9]{9}$";
        String phoneRegex = "^[7-9][0-9]{9}$";

        Pattern pattern = Pattern.compile(phoneRegex);

        Matcher matcher = pattern.matcher(mobileNo);
        return matcher.matches();

    }

    public boolean isValidMobileNoLength(String mobileNo){


        if( mobileNo.length()<10){
            return false;
        }

        return true;
    }

    public boolean isNullOrEmpty(String string){
        return TextUtils.isEmpty(string);
    }

    public boolean isNumeric(String string){
        return TextUtils.isDigitsOnly(string);
    }

}
