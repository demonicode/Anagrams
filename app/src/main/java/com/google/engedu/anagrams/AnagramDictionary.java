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
    private ArrayList<String> wordList=new ArrayList<String>();
    private Map<String,ArrayList<String>> lettersToWord = new HashMap<String,ArrayList<String>>();
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
            wordList.add(word);
            wordSet.add(word);
            String sortedword = sortletters(word);
            if (!lettersToWord.containsKey(sortedword)) {
                lettersToWord.put(sortedword, new ArrayList<String>());
            }
            lettersToWord.get(sortedword).add(word);
            if (!sizeToWord.containsKey(word.length())) {
                sizeToWord.put(word.length(), new ArrayList<String>());
            }
            sizeToWord.get(word.length()).add(word);
        }
    }


    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word)){
            if(!word.toLowerCase().contains(base.toLowerCase()));{
                return true;
            }

        }
        return false ;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedTargetWord = sortletters(targetWord.toLowerCase());
        Iterator it = wordList.iterator();
        while(it.hasNext()){
            String nowWord = (String)it.next();
            String sortedNowWord = sortletters(nowWord.toLowerCase());
            if(sortedNowWord==sortedTargetWord){
                result.add(nowWord);
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String wordIsSorted = sortletters(word);
        for(char i='a';i<='z';i++){
            String wordIsSorted2 = wordIsSorted + i;
            wordIsSorted2 = sortletters(wordIsSorted2.toLowerCase());
            if(lettersToWord.containsKey(wordIsSorted2)){
                Iterator it = lettersToWord.get(wordIsSorted2).iterator();
                while(it.hasNext()){
                    result.add((String) it.next());
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        String nowWord;
        while(true) {
            nowWord = sizeToWord.get(wordLength).get(random.nextInt(sizeToWord.get(wordLength).size()));
            List<String> result = new ArrayList<String>();
            result = getAnagramsWithOneMoreLetter(nowWord);
            if(result.size()>MIN_NUM_ANAGRAMS){
                break;
            }
        }

        if(wordLength!=MAX_WORD_LENGTH)
            wordLength++;
        return nowWord;
    }
}
