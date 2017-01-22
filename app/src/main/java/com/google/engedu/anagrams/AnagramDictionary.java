/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordListOfOne=new ArrayList<String>();
    private Map<String,HashSet<String>> lettersToWord = new HashMap<String,HashSet<String>>();
    private Map<Integer,ArrayList<String>> sizeToWord = new HashMap<Integer, ArrayList<String>>();
    private HashSet wordSet = new HashSet();
    private int wordLength = DEFAULT_WORD_LENGTH;



    public String sortletters(String wordy){
        int l=wordy.length();
        char newwordy[] = new char[l];
        for(int i=0;i<l;i++)
            newwordy[i] = wordy.charAt(i);
        char t;
        for(int j=0;j<l-1;j++){
            for(int k=0;k<l-1-j;k++){
                if(newwordy[k]>newwordy[k+1]){
                    t=newwordy[k];
                    newwordy[k]=newwordy[k+1];
                    newwordy[k+1] = t;
                }
            }
        }
        String sortedString="";
        for(int i=0;i<l;i++){
            sortedString += newwordy[i];
        }
        return sortedString;
    }
    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            String sortedword = sortletters(word);
            if (!lettersToWord.containsKey(sortedword)) {
                lettersToWord.put(sortedword, new HashSet<String>());
            }
            lettersToWord.get(sortedword).add(word);

        }
        Iterator it2 = wordSet.iterator();
        while(it2.hasNext()){
            String currentWord1 = (String) it2.next();
            HashSet<String> result = new HashSet<String>();
            result = getAnagramsWithOneMoreLetter(currentWord1);
            if(result.size()>=MIN_NUM_ANAGRAMS){
                wordListOfOne.add(currentWord1);
            }
        }
        Iterator it3 = wordListOfOne.iterator();
        while(it3.hasNext()) {
            String currentWord2 = (String) it3.next();
            if (!sizeToWord.containsKey(currentWord2.length())) {
                sizeToWord.put(currentWord2.length(), new ArrayList<String>());
            }
            sizeToWord.get(currentWord2.length()).add(currentWord2);
        }
    }


    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word)){
            if(!word.toLowerCase().contains(base.toLowerCase())){
                return true;
            }
        }
        return false ;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedTargetWord = sortletters(targetWord.toLowerCase());
        Iterator it = wordSet.iterator();
        while(it.hasNext()){
            String nowWord = (String)it.next();
            String sortedNowWord = sortletters(nowWord.toLowerCase());
            if(sortedNowWord==sortedTargetWord){
                result.add(nowWord);
            }
        }
        return result;
    }

    public HashSet<String> getAnagramsWithOneMoreLetter(String word) {
        HashSet<String> result = new HashSet<String>();
        String wordIsSorted = sortletters(word);
        for(char i='a';i<='z';i++){
            String wordIsSorted2 = wordIsSorted + i;
            wordIsSorted2 = sortletters(wordIsSorted2.toLowerCase());
            if(lettersToWord.containsKey(wordIsSorted2)){
                Iterator it = lettersToWord.get(wordIsSorted2).iterator();
                while(it.hasNext()){
                    String current = (String) it.next();
                    if(!current.toLowerCase().contains(word.toLowerCase()))
                        result.add(current);
                }
            }
        }
        return result;
    }

    public HashSet<String> getAnagramsWithTwoMoreLetter(String word) {
        HashSet<String> result = new HashSet<String>();
        String wordIsSorted = sortletters(word);
        for(char i='a';i<='z';i++){
            for(char j='a';j<='z';j++) {
                String wordIsSorted2 = wordIsSorted + i + j;
                wordIsSorted2 = sortletters(wordIsSorted2.toLowerCase());
                if (lettersToWord.containsKey(wordIsSorted2)) {
                    Iterator it = lettersToWord.get(wordIsSorted2).iterator();
                    while (it.hasNext()) {
                        String current = (String) it.next();
                        if(!current.toLowerCase().contains(word.toLowerCase()))
                            result.add(current);
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        String nowWord;
        nowWord = sizeToWord.get(wordLength).get(random.nextInt(sizeToWord.get(wordLength).size()));
        if(wordLength!=MAX_WORD_LENGTH)
            wordLength++;
        return nowWord;
    }
}
