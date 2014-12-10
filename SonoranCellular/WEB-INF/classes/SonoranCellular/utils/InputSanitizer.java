package SonoranCellular.utils;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

public class InputSanitizer{
    
    /*
     The check_____AndReturnErrorString methods return a
     generated error message if the string fails the regex 
     parse, returns empty string otherwise.
     */
    public static String checkPhoneNumAndReturnErrorString(String field, String input){
        String s = "";
        if(checkPhoneNum(input)){
            return s;
        }else{
            return "Parse error: Mobile Number must be in the format (xxx) xxx-xxxx";
        }
    }
    
    public static String checkStringAlphaAndReturnErrorString(String field, String input, int min, int max){
        String s = "";
        if(checkStringAlpha(input, min, max)){
            return s;
        }else{
            return "Parse error: " + field + " must be " + min + " to " + max + " characters long without numbers or symbols.";
        }
    }
    
    public static String checkStringAlphanumericAndReturnErrorString(String field, String input, int min, int max){
        String s = "";
        if(checkStringAlphanumeric(input, min, max)){
            return s;
        }else{
            return "Parse error: " + field + " must be " + min + " to " + max + " characters long without symbols.";
        }
    }
    
    public static String checkStringNumericAndReturnErrorString(String field, String input, int min, int max){
        String s = "";
        if(checkStringNumeric(input, min, max)){
            return s;
        }else{
            return "Parse error: " + field + " must be a " + min + " to " + max + " digits long without symbols, space, or letters.";
        }
    }
    
    public static boolean checkPhoneNum(String input){
        Pattern p = Pattern.compile("\\([0-9]{3}\\)\\ [0-9]{3}-[0-9]{4}");
        Matcher m = p.matcher(input);
        return(m.matches());
    }

    public static boolean checkStringAlpha(String input, int min, int max){
        Pattern p = Pattern.compile("[a-zA-Z ]" + "{" + min + "," + max + "}");
        Matcher m = p.matcher(input);
        return(m.matches());
    }
    
    public static boolean checkStringAlphanumeric(String input, int min, int max){
        Pattern p = Pattern.compile("[a-zA-Z0-9 ]" + "{" + min + "," + max + "}");
        Matcher m = p.matcher(input);
        return(m.matches());
    }
    
    public static boolean checkStringNumeric(String input, int min, int max){
        Pattern p = Pattern.compile("[0-9]" + "{" + min + "," + max + "}");
        Matcher m = p.matcher(input);
        return(m.matches());
    }
    
    public static String checkDateValidAndReturnErrorString(String field, String input){
        try{
            System.out.println("HW8: Validating string called");
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            inputFormat.setLenient(false);
            Date date = inputFormat.parse(input);
            System.out.println((date==null));
            if(date == null){
                return "Parse error: for " + field + " the date provided is invalid. Please provide date in YYYY-MM-DD format.";
            }else{
                return "";
            }
        }
        catch (Exception ex)
        {
    		ex.printStackTrace();
            return  "Parse error: for " + field + " the date provided is invalid. Please provide date in YYYY-MM-DD format.";
        }
    }
    
    /*
    public static String convertDate(String input){
        try{
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = simpleDateFormat.parse(input);
            if(Date == null){
                return "";
            }else{
                return
            }
        }
        catch (ParseException ex)
        {
    		ex.printStackTrace();
        }
        
    }*/
    
    /*
     String s = "03/24/2013 21:54";
     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
     try
     {
     Date date = simpleDateFormat.parse(s);
     
     System.out.println("date : "+simpleDateFormat.format(date));
     }
     catch (ParseException ex)
     {
     System.out.println("Exception "+ex);
     }
     */
}

