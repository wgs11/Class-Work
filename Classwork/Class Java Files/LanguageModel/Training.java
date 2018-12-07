/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package languagemodel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author William
 */
public class Training {

    public static HashMap<String, Double> indies = new HashMap<String, Double>();
    public static HashMap<String, Double> bigrams = new HashMap<String, Double>();
    public static HashMap<String, Double> trigrams = new HashMap<String, Double>();
    public static HashMap<String, Double> unigrams = new HashMap<String, Double>();

    public static HashMap<String, Double> getIndies(String file) {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("\\,", " ,");
                String[] words = line.split("\\s+");
                for (String word : words) {

                    if (!indies.containsKey(word)) {
                        //      System.out.println(word);
                        indies.put(word, 1.0);

                    } else {
                        indies.put(word, indies.get(word) + 1);
                    }

                }
            }
        } catch (Exception e) {
        }

        return indies;
    }

    public static HashMap<String, Double> buildUnigram(String file, HashMap<String, Double> indies) {
        //    HashMap<String, Double> unigrams = new HashMap<String, Double>();
        unigrams.put("<UNK>", 0.0);
        double wordCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("\\,", " ,");
                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (!(word.equals("</s>"))) {
                        if (indies.get(word) <= 1.0) {
                            unigrams.put("<UNK>", unigrams.get("<UNK>") + 1);

                        } else {
                            unigrams.put(word, indies.get(word));
                        }
                        wordCount++;
                    }
                }

            }
        } catch (Exception e) {
        }

        unigrams.put("__unigram_count", wordCount);
        return unigrams;
    }

    public static HashMap<String, Double> buildBigram(String file, HashMap<String, Double> indies) {
        double bigramCount = 0;
        HashMap<String, Double> bigrams = new HashMap<String, Double>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] words = line.split("\\s+");
                String prev = null;
                for (String word : words) {
                    if (!(word.equals("</s>"))) {
                        String curr = word;
                        if (indies.get(curr) <= 1) {
                            curr = "<UNK>";
                        }
                        if (prev == null) {
                            prev = curr;
                        } else if (!bigrams.containsKey(prev + " " + curr)) {
                            bigrams.put(prev + " " + curr, 1.0);
                        } else {
                            bigrams.put(prev + " " + curr, bigrams.get(prev + " " + curr) + 1);
                        }
                        prev = curr;
                        bigramCount++;
                    }
                }
            }
        } catch (Exception e) {
        }
        bigrams.put("__bigram_count", bigramCount);
//        System.out.println(bigramCount);
//        System.out.println("Printing Bigrams");
        for (Map.Entry<String, Double> entry : bigrams.entrySet()) {
            double times = entry.getValue();
            String key = entry.getKey();
            //           System.out.println(key + ": " + times);

        }
        return bigrams;
    }

    public static HashMap<String, Double> buildTrigram(String file, HashMap<String, Double> indies) {
        double trigramCount = 0;
        HashMap<String, Double> trigrams = new HashMap<String, Double>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("\\,", " ,");
                String[] words = line.split("\\s+");
                String first = null;
                String prev = null;
                for (String word : words) {

                    String curr = word;
                    if (indies.get(curr) <= 1) {
                        curr = "<UNK>";
                    }
                    if (first == null) {
                        first = curr;
                    }
                    if (prev == null) {
                        prev = curr;
                    } else if (!trigrams.containsKey(first + " " + prev + " " + curr)) {
                        trigrams.put(first + " " + prev + " " + curr, 1.0);
                    } else {
                        trigrams.put(first + " " + prev + " " + curr, trigrams.get(first + " " + prev + " " + curr) + 1);
                    }
                    first = prev;
                    prev = curr;
                    trigramCount++;
                }
            }
        } catch (Exception e) {
        }
        trigrams.put("__trigram_count", trigramCount);
//        System.out.println(trigramCount);
//        System.out.println("Printing Trigrams");
        for (Map.Entry<String, Double> entry : trigrams.entrySet()) {
            double times = entry.getValue();
            String key = entry.getKey();
//            System.out.println(key + ": " + times);

        }
        return trigrams;
    }

    public static nGram getVocab(String file) {

        HashMap<String, Double> indies = Training.getIndies("valtozok.txt");
        HashMap<String, Double> unigrams = buildUnigram("valtozok.txt", indies);
        //  System.out.println(unigrams.size());
        HashMap<String, Double> bigrams = buildBigram("valtozok.txt", indies);
        //   System.out.println(bigrams.size());
        HashMap<String, Double> trigrams = buildTrigram("valtozok.txt", indies);
        //   System.out.println(trigrams.size());
        nGram trained = new nGram(unigrams, bigrams, trigrams);
        return trained;
    }

}
