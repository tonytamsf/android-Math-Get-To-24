package com.wordpress.tonytam.mathgetto24;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import com.wordpress.tonytam.model.cards.*;

/**
 * Created by tonytam on 6/16/14.
 */
public class AnswerPackage {
    public String stringAnswer;

// The strFormat for the answer

    String stringFormat;

// the list of operators used
    Method operators[];

// The list of cards for the answer, order is important
    PlayingCard cards[];

// The final answer
    BigDecimal answer;

    public String toString() {
        return stringAnswer + " = " + String.valueOf(answer);
    }
}
