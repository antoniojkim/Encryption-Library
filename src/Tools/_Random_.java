package Tools;

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
    public static byte randomByte(){
        return (byte)((Byte.MAX_VALUE-Byte.MIN_VALUE)*Math.random()+Byte.MIN_VALUE);
    }

    public static int randomint(int low, int high){
        return (int)((high-low+1)*Math.random()+low);
    }

    public static<E> E random (E[] array){
        return array[randomint(0, array.length-1)];
    }
    public static<E> E random (List<E> list){
        return list.get(randomint(0, list.size()-1));
    }

    public static<E> E pop (List<E> list){
        return list.remove(randomint(0, list.size()-1));
    }

    public static<E> List<E> random (List<E> list, int numElements){
        numElements = Math.abs(numElements);
        if (numElements <= list.size()){
            List<E> listcopy = new ArrayList<>();
            listcopy.addAll(list);
            List<E> random = new ArrayList<>();
            for (int i = 0; i<numElements; i++){
                random.add(pop(listcopy));
            }
            return random;
        }
        return null;
    }

}
