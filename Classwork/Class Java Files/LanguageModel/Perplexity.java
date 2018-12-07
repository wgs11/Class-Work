/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package languagemodel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 *
 * @author William
 */
public class Perplexity {

    public static double perWordPerplexity(String[] words, nGram grams, String type) {
        double corpusEntropy = 0;
        int wordCount = 0;
        double sentenceEntropy = 1;
        //    wordCount += words.length - 1;
        if (type.equals("1")) {
            //    System.out.println("Unigram Model:");
            sentenceEntropy = computeUnigramProbability(words, nGram.getUnigram(grams));
            sentenceEntropy = Math.log(sentenceEntropy);
            sentenceEntropy = -sentenceEntropy / Math.log(2);

        } else if (type.equals("2")) {
            //     System.out.println("Unsmoothed Bigram");
            sentenceEntropy = computeBigramProbability(words, nGram.getUnigram(grams), nGram.getBigram(grams), 1);
            if (sentenceEntropy == 0) {
                System.out.println("Infinite");
            } else {
                sentenceEntropy = Math.log(sentenceEntropy);
                sentenceEntropy = -sentenceEntropy / Math.log(2);
            }

        } else if (type.equals("2s")) {
            // System.out.println("Smoothed Bigram");
            sentenceEntropy = computeBigramProbability(words, nGram.getUnigram(grams), nGram.getBigram(grams), 2);
            sentenceEntropy = Math.log(sentenceEntropy);
            sentenceEntropy = -sentenceEntropy / Math.log(2);

        } else if (type.equals("3")) {
            //    System.out.println("Unsmoothed Trigram");
            sentenceEntropy = computeTrigramProbability(words, nGram.getUnigram(grams), nGram.getBigram(grams), nGram.getTrigram(grams), 1);
            if (sentenceEntropy == 0) {
                System.out.println("Infinite");
            } else {
                sentenceEntropy = Math.log(sentenceEntropy);
                sentenceEntropy = -sentenceEntropy / Math.log(2);
            }
        } else if (type.equals("3s")) {
            //    System.out.println("Smoothed Trigram");
            sentenceEntropy = computeTrigramProbability(words, nGram.getUnigram(grams), nGram.getBigram(grams), nGram.getTrigram(grams), 2);
            sentenceEntropy = Math.log(sentenceEntropy);
            sentenceEntropy = -sentenceEntropy / Math.log(2);
        }
        //   System.out.println("Sentence Entropy: " + sentenceEntropy / words.length);
        // System.out.println("Entropy: " + sentenceEntropy);
        return (Math.pow(2, sentenceEntropy / words.length));

        //    System.out.println();
        //    System.out.println();
        //         }
        //      } catch (Exception e) {
        //    }
    }

    private static double computeUnigramProbability(String[] words, HashMap<String, Double> unigrams) {
        double lambda = 0.9;
        double chance = 1;
        double perplexity = 1;
        double tempPerplexity = 1;

        double vocabSize = unigrams.size() - 1;
        for (String word : words) {
            if (!(word.equals("</s>"))) {
                if (unigrams.get(word) != null) {
                    chance = (unigrams.get(word) / unigrams.get("__unigram_count"));
                } else {
                    chance = (0.0);
                }

                tempPerplexity = ((lambda * chance) + ((1 - lambda) * (1 / vocabSize)));
                perplexity *= tempPerplexity;

            }
        }
        return perplexity;
    }

//
    private static double computeBigramProbability(String[] words, HashMap<String, Double> unigrams, HashMap<String, Double> bigrams, int type) {
        String a = null;
        String b = null;
        double countAB = 0;
        double countA = 0;
        double perplexity = 1;
        double tempPerplexity = 1;
        double wordCount = unigrams.get("__unigram_count");
        double lambda = 0.1;
        for (String word : words) {
            if (!(word.equals("</s>"))) {
                if (a == null) {         //set up bigram, can't have one for first word
                    if (unigrams.get(word) != null) {
                        a = word;
                    } else {
                        a = "<UNK>";
                    }
                } else {
                    if (unigrams.get(word) != null) {
                        b = word;
                    } else {
                        b = "<UNK>";
                    }
                    if (bigrams.get(a + " " + b) != null) {
                        //  System.out.println(bigrams.get(a+" "+b));
                        countAB = bigrams.get(a + " " + b);
                        countA = unigrams.get(a);// * wordCount;
                        tempPerplexity = (countAB / countA);
                        //  System.out.println(tempPerplexity);
                    } else {
                        tempPerplexity = 0;
                    }
                    if (type == 2) {
                        double unigramProbability = unigrams.get(b) / wordCount;
                        tempPerplexity = (lambda * tempPerplexity) + ((1 - lambda) * unigramProbability);
                    }
                    a = b;
                    perplexity *= tempPerplexity;
                }
            }
        }
        return perplexity;
    }

//
    private static double computeTrigramProbability(String[] words, HashMap<String, Double> unigrams, HashMap<String, Double> bigrams, HashMap<String, Double> trigrams, int type) {
        String a = null;
        String b = null;
        String c = null;
        double countABC = 0;
        double countBC = 0;
        double countAB = 0;
        double countB = 0;
        double countC = 0;
        double perplexity = 1;
        double tempPerplexity = 1;
        // double wordCount = unigrams.get("__unigram_count");
        double lambdaA = .2;
        double lambdaB = .2;
        for (String word : words) {
            if (!(word.equals("</s>"))) {
                if (a == null) {         //set up bigram, can't have one for first word
                    if (unigrams.get(word) != null) {
                        a = word;
                    } else {
                        a = "<UNK>";
                    }
                } else if (b == null) {
                    if (unigrams.get(word) != null) {
                        b = word;
                    } else {
                        b = "<UNK>";
                    }
                } else {
                    if (unigrams.get(word) != null) {
                        c = word;
                    } else {
                        c = "<UNK>";
                    }
                    // System.out.println(a+" "+b+" "+c);
                    if (trigrams.get(a + " " + b + " " + c) != null) {
                        countAB = bigrams.get(a + " " + b);
                        //  System.out.println(bigrams.get(a+" "+b));                               //Sam given I am: ABC/AB

                        tempPerplexity = (countABC / countAB);
                    } else {
                        tempPerplexity = 0;
                    }
                    double bigramProbability;
                    double unigramProbability;
                    if (type == 2) {
                        //Sam given am: BC/B
                        if (bigrams.get(a + " " + b) != null) {
                            countAB = bigrams.get(a + " " + b);            //Sam: C / wordcount
                        }

                        if (unigrams.get(c) != null) {
                            countC = unigrams.get(c);// * wordCount;
                        }
                        if (bigrams.get(b + " " + c) != null) {
                            countBC = bigrams.get(b + " " + c);
                            countB = unigrams.get(b);
                            //    System.out.println(countBC+" "+countB);
                            bigramProbability = countBC / countB;
                        } else {
                            bigramProbability = 0;
                        }
                        //  System.out.println("Trigram: "+tempPerplexity);
                        unigramProbability = countC / unigrams.get("__unigram_count");
                        tempPerplexity = (lambdaA * tempPerplexity) + ((1 - lambdaA) * bigramProbability) + ((1 - lambdaA - lambdaB) * unigramProbability);
                        //System.out.println("Unigram: "+unigramProbability);
                        //System.out.println("Bigram: "+bigramProbability);

                    }

                    a = b;
                    b = c;
                    perplexity *= tempPerplexity;
                    //  System.out.println("intermediate perplexity: "+perplexity);
                }
            }
        }
        //  System.out.println("Final Perplexity: "+perplexity);
        return perplexity;
    }
}
