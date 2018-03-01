package Tools;/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

import java.util.ArrayList;
import java.util.List;

/**

@author Antonio
*/
public class Search {

    public static int linearSearch(byte[] array, byte item){
        for (int i = 0; i<array.length; i++) {
            if (array[i] == item) {
                return i;
            }
        }
        return -1;
    }
    public static int linearSearch(char[] array, char item){
        for (int i = 0; i<array.length; i++) {
            if (array[i] == item) {
                return i;
            }
        }
        return -1;
    }
    public static<E> int linearSearch(E[] array, E item){
        for (int i = 0; i<array.length; i++) {
            if (array[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

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

    public static boolean contains(char[] array, char item){
        return binarySearch(array, item) != -1;
    }
    public static int binarySearch(char[] array, char item){
        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = array[mid]-item;
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
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
    public static int count = 0;
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
            count++;
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

    public static String[] split(String text, String delim){
        while (text.endsWith(delim)){
            text = text.substring(0, text.length()-delim.length());
        }
        List<String> split = new ArrayList<>();
        int previous = 0;
        int index = text.indexOf(delim);
        while (index != -1){
            split.add(text.substring(previous, index));
            previous = index + delim.length();
            index = text.indexOf(delim, previous);
        }
        split.add(text.substring(previous, text.length()));
        String[] results = new String[split.size()];
        for (int i = 0; i<results.length; i++){
            results[i] = split.get(i);
        }
        return results;
    }
    
}
