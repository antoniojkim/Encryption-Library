package Encryption_Library;

import Encryption_Library.Tools.Search;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 2017-08-25.
 */
public class Key {

    private char[] characters;
    private char[][] table;

    private String salt = null;

    public Key(BufferedReader br){
        try{
            List<String> lines = new ArrayList<>();
            String line = br.readLine();
            while (line != null){
                lines.add(Search.replace(line, "\uFFFD", "",    " ", ""));
                line = br.readLine();
            }
            characters = lines.get(0).toCharArray();
            table = new char[characters.length][characters.length];
            for (int a = 1; a<lines.size(); a++){
                table[a-1] = lines.get(a).toCharArray();
            }
        }catch (IOException e){}
    }

    public Key(List<char[]> keyTable){
        if (keyTable.size() > 0 && keyTable.size() == keyTable.get(0).length+1){
            characters = keyTable.remove(0);
            table = new char[keyTable.size()][0];
            for (int i = 0; i<keyTable.size(); i++){
                table[i] = keyTable.get(i);
            }
        }
    }

    public boolean isValidKey(){
        return characters != null && table != null && table.length == characters.length && table[0].length == characters.length;
    }

    public String getSalt(){
        if (salt == null || salt.length() == 0){
            salt = String.valueOf(table[0][Search.linearSearch(characters, table[0][0])])+
                    String.valueOf(table[1][Search.linearSearch(characters, table[1][1])])+
                    String.valueOf(table[2][Search.linearSearch(characters, table[2][2])])+
                    String.valueOf(table[3][Search.linearSearch(characters, table[3][3])])+
                    String.valueOf(table[4][Search.linearSearch(characters, table[4][4])]);
        }
        return salt;
    }

    public char[] getCharacters() {
        return characters;
    }
    public List<Character> getCharacterList(){
        List<Character> list = new ArrayList<>();
        for (char c : characters){
            list.add(c);
        }
        return list;
    }

    public void setCharacters(char[] characters) {
        this.characters = characters;
    }

    public char[][] getTable() {
        return table;
    }

    public void setTable(char[][] table) {
        this.table = table;
    }
}
