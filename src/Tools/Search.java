package Tools;/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**

@author Antonio
*/
public class Search {
    
    public static int binarySearch(String[] array, String item){
        if (array.length > 1){
            if (array[1].compareTo(array[0]) > 0){
                return binarySearch(array, 0, array.length-1, item, true);
            }
            return binarySearch(array, 0, array.length-1, item, false);
        }
        return 0;
    }
    private static int binarySearch(String[] array, int low, int high, String item, boolean ascending){
//        count++;
        if (high < low){
            return -1;
        }
        int mid = low+(high-low)/2;
        if (array[mid].equals(item)){
            return mid;
        }
        else if (array[mid].compareTo(item) < 0){
            if (ascending){
                return binarySearch(array, mid+1, high, item, true);
            }
            else{
                return binarySearch(array, low, mid-1, item, false);
            }
        }
        else{
            if (ascending){
                return binarySearch(array, low, mid-1, item, true);
            }
            else{
                return binarySearch(array, mid+1, high, item, false);
            }
        }
    }


    public static String replace(String text, String... searchReplace) {
        for (int i = 1; i<searchReplace.length; i+=2){
            text = replace(text, searchReplace[i-1], searchReplace[i]);
        }
        return text;
    }
    public static String replace(String text, String[] searchString, String[] replacement) {
        int length = Math.min(searchString.length, replacement.length);
        for (int i = 0; i<length; i++){
            text = replace(text, searchString[i], replacement[i]);
        }
        return text;
    }
    public static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, -1);
    }
    public static String replace(String text, String searchString, String replacement, int max) {
        if (text.length() == 0 || searchString.length() == 0 || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
        StringBuffer buf = new StringBuffer(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }
    
}
