/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package languagemodel;

import java.util.*;

/**
 *
 * @author William
 */
public class nGram {

    private HashMap<String, Double> unigram;
    private HashMap<String, Double> bigram;
    private HashMap<String, Double> trigram;

    public nGram(HashMap<String, Double> unigram, HashMap<String, Double> bigram, HashMap<String, Double> trigram) {
        this.unigram = unigram;
        this.bigram = bigram;
        this.trigram = trigram;

    }

    public static HashMap<String, Double> getUnigram(nGram ralph) {
        return ralph.unigram;
    }

    public static HashMap<String, Double> getBigram(nGram ralph) {
        return ralph.bigram;
    }

    public static HashMap<String, Double> getTrigram(nGram ralph) {
        return ralph.trigram;
    }

}
