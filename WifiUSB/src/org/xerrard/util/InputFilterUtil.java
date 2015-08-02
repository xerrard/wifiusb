package org.xerrard.util;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterUtil {
    
    public static class InputFilterSpace implements InputFilter{

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                Spanned dest, int dstart, int dend) {   

            if(source.length()<1)
            {
                 return null;
            }
            else
            {
                char temp [] = (source.toString()).toCharArray();
                char result [] = new char[temp.length];
                for(int i = 0,j=0; i< temp.length; i++){
                    if(temp[i] == ' '){
                        continue;
                    }else{
                        result[j++] = temp[i];
                    }
                }
                return String.valueOf(result).trim();
            }
           
        }
    }
}
