package Tools;

/**
 * Created by Antonio on 2017-12-25.
 */
public class NumberTools {

    public static int floor2(int x){
        if (x < 0)  return 0;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return (x+1) >> 1;
    }

}
