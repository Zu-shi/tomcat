package SonoranCellular.utils;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
    
}

