package Tools;

/**
 * Created by Antonio on 2017-07-22.
 */
public class Print {

    public static<E> void print(E[] array){
        if (array.length > 0) {
            System.out.print("["+array[0]);
            for (int i = 1; i < array.length; i++){
                System.out.print(", "+array[i]);
            }
            System.out.print("]");
        }
    }
    public static<E> void println(E[] array){
        if (array.length > 0) {
            print(array);
            System.out.println();
        }
    }
    public static<E> void println(E[][] array){
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


    public static void print(byte[] array){
        if (array.length > 0) {
            System.out.print("["+array[0]);
            for (int i = 1; i < array.length; i++){
                System.out.print(", "+array[i]);
            }
            System.out.print("]");
        }
    }
    public static void println(byte[] array){
        if (array.length > 0) {
            print(array);
            System.out.println();
        }
    }
    public static void println(byte[][] array){
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

}
