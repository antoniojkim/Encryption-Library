package Encryption_Library.Tools;

/**
 * Created by Antonio on 2017-07-22.
 */
public class Print {

    public static void print(String[] array){
        if (array.length > 0) {
            System.out.print("["+array[0]);
            for (int i = 1; i < array.length; i++){
                System.out.print(", "+array[i]);
            }
            System.out.print("]");
        }
    }
    public static void println(String[][] array){
        if (array.length > 0) {
            System.out.print("[");
            if (array.length > 1){
                print(array[0]);
                System.out.println(",");
                for (int i = 1; i < array.length-1; i++){
                    System.out.print(" ");
                    print(array[i]);
                    System.out.println(",");
                }
                System.out.print(" ");
                print(array[array.length-1]);
            }
            else{
                print(array[0]);
            }
            System.out.println("]");
        }
    }
    public static void println(char[] array){
        if (array.length > 0) {
            System.out.print("["+array[0]);
            for (int i = 1; i < array.length; i++){
                System.out.print(", "+array[i]);
            }
            System.out.println("]");
        }
    }
    public static void println(char[][] array){
        if (array.length > 0) {
            System.out.print("[");
            if (array.length > 1){
                print(array[0]);
                System.out.println(",");
                for (int i = 1; i < array.length-1; i++){
                    System.out.print(" ");
                    print(array[i]);
                    System.out.println(",");
                }
                System.out.print(" ");
                print(array[array.length-1]);
            }
            else{
                print(array[0]);
            }
            System.out.println("]");
        }
    }
    public static void print(char[] array){
        if (array.length > 0) {
            System.out.print("["+array[0]);
            for (int i = 1; i < array.length; i++){
                System.out.print(", "+array[i]);
            }
            System.out.print("]");
        }
    }
    public static void println(String[] array){
        if (array.length > 0) {
            System.out.print("["+array[0]);
            for (int i = 1; i < array.length; i++){
                System.out.print(", "+array[i]);
            }
            System.out.println("]");
        }
    }
    public static void println(int[] array){
        if (array.length > 0) {
            System.out.print("["+array[0]);
            for (int i = 1; i < array.length; i++){
                System.out.print(", "+array[i]);
            }
            System.out.println("]");
        }
    }

}
