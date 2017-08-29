package Encryption_Library.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 2017-07-11.
 */
public class _Random_ {

    public static double random(){
        return Math.random();
    }

    public static double random(double low, double high){
        return (high-low)*Math.random()+low;
    }

    public static int randomint(int low, int high){
        return (int)((high-low+1)*Math.random()+low);
    }

    public static String random (String[] array){
        return array[randomint(0, array.length-1)];
    }
    public static String random (List<String> list){
        return list.get(randomint(0, list.size()-1));
    }

    public static String pop (List<String> list){
        return list.remove(randomint(0, list.size()-1));
    }

    public static List<String> random (List<String> list, int numElements){
        numElements = Math.abs(numElements);
        if (numElements <= list.size()){
            List<String> listcopy = new ArrayList<>();
            listcopy.addAll(list);
            List<String> random = new ArrayList<>();
            for (int i = 0; i<numElements; i++){
                random.add(pop(listcopy));
            }
            return random;
        }
        return null;
    }

    public static String[] common = {"cbrt", "arcsin", "sinh", "sin", "arccos", "cosh", "cos",
            "arctan", "tanh", "tan", "arccsc", "csch", "csc", "arcsec", "sech", "sec", "arccot", "coth", "cot", "ln", "lp", "log", "aexp"};
    public static String randomf(){
        int numTerms = randomint(1, 3);
        String function = "";
        String[] operators = {"+","-","*", "/"};
        for (int a = 0; a<numTerms; a++){
            if (a > 0){
                function += random(operators);
            }
            function += common[randomint(0, common.length-1)];
            if (Math.random()>0.5){
                function += "("+common[randomint(0, common.length-1)];
            }
            else{
                function+="x";
            }
        }
        return function;
    }

}
