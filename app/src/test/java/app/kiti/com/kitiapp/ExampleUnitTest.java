package app.kiti.com.kitiapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private final static int wordsPerMinute = 40;
    @Test
    public void addition_isCorrect() throws Exception {
        String text = "Recently, I took a leaf out of Medium's book and decided to add the estimated reading time to my blog posts. This was so that people could decide whether they had enough time to commit to the post before reading it.";
            int noOfWords = text.split(" ").length;
            int secToRead = noOfWords*60 / wordsPerMinute;
            System.out.println(secToRead);

    }
}