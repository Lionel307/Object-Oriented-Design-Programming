package splitter;

import java.util.Scanner;

public class Splitter {

    public static void main(String[] args) {
        System.out.println("Enter a sentence specified by spaces only: ");
        // user input
        Scanner sc = new Scanner(System.in);
        String sentence = sc.nextLine();
        // split the sentence at " "
        String[] splitWord = sentence.split(" ");

        for (int i = 0; i < splitWord.length; i++) {
            System.out.println(splitWord[i]);
        }
        sc.close();

    }
}
