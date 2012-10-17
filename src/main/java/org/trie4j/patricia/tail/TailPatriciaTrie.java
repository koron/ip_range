/*
 * Copyright 2012 Takao Nakaguchi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.trie4j.patricia.tail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.trie4j.AbstractTrie;
import org.trie4j.Trie;
import org.trie4j.tail.SuffixTrieTailBuilder;
import org.trie4j.tail.TailBuilder;
import org.trie4j.tail.TailCharIterator;

public class TailPatriciaTrie extends AbstractTrie implements Trie{
	public TailPatriciaTrie() {
		this.tailBuilder = new SuffixTrieTailBuilder();
		this.tails = tailBuilder.getTails();
	}

	public TailPatriciaTrie(TailBuilder builder){
		this.tailBuilder = builder;
		this.tails = builder.getTails();
	}

	@Override
	public org.trie4j.Node getRoot() {
		return new NodeAdapter(root, tails);
	}

	@Override
	public boolean contains(String text) {
		Node node = root;
		TailCharIterator it = new TailCharIterator(tails, -1);
		int n = text.length();
		for(int i = 0; i < n; i++){
			node = node.getChild(text.charAt(i));
			if(node == null) return false;
			it.setIndex(node.getTailIndex());
			while(it.hasNext()){
				i++;
				if(i == n) return false;
				if(text.charAt(i) != it.next()) return false;
			}
		}
		return node.isTerminate();
	}

	@Override
	public int findWord(CharSequence chars, int start, int end, StringBuilder word){
		TailCharIterator it = new TailCharIterator(tails, -1);
		for(int i = start; i < end; i++){
			Node node = root;
			for(int j = i; j < end; j++){
				node = node.getChild(chars.charAt(j));
				if(node == null) break;
				boolean matched = true;
				it.setIndex(node.getTailIndex());
				while(it.hasNext()){
					j++;
					if(j == end || chars.charAt(j) != it.next()){
						matched = false;
						break;
					}
				}
				if(matched){
					if(node.isTerminate()){
						if(word != null) word.append(chars, i, j + 1);
						return i;
					}
				} else{
					break;
				}
			}
		}
		return -1;
	}
	
	@Override
	public Iterable<String> commonPrefixSearch(final String query) {
		if(query.length() == 0) return new ArrayList<String>(0);
		return new Iterable<String>(){
			{
				this.queryChars = query.toCharArray();
			}
			private char[] queryChars;
			@Override
			public Iterator<String> iterator() {
				return new Iterator<String>() {
					private int cur;
					private StringBuilder currentChars = new StringBuilder();
					private Node current = root;
					private String next;
					{
						cur = 0;
						findNext();
					}
					private void findNext(){
						next = null;
						while(next == null){
							if(queryChars.length <= cur) return;
							Node child = current.getChild(queryChars[cur]);
							if(child == null) return;
							int rest = queryChars.length - cur;
							char[] letters = child.getLetters(tails);
							int len = letters.length;
							if(rest < len) return;
							for(int i = 1; i < len; i++){
								int c = letters[i] - queryChars[cur + i];
								if(c != 0) return;
							}

							String b = new String(queryChars, cur, len);
							if(child.isTerminate()){
								next = currentChars + b;
							}
							cur += len;
							currentChars.append(b);
							current = child;
						}
					}
					@Override
					public boolean hasNext() {
						return next != null;
					}
					@Override
					public String next() {
						String ret = next;
						if(ret == null){
							throw new NoSuchElementException();
						}
						findNext();
						return ret;
					}
					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	private void enumLetters(Node node, String prefix, List<String> letters){
		Node[] children = node.getChildren();
		if(children == null) return;
		for(Node child : children){
			String text = prefix + new String(child.getLetters(tails));
			if(child.isTerminate()) letters.add(text);
			enumLetters(child, text, letters);
		}
	}

	@Override
	public Iterable<String> predictiveSearch(String prefix) {
		char[] queryChars = prefix.toCharArray();
		int cur = 0;
		Node node = root;
		while(node != null){
			char[] letters = node.getLetters(tails);
			int n = Math.min(letters.length, queryChars.length - cur);
			for(int i = 0; i < n; i++){
				if(letters[i] != queryChars[cur + i]){
					return Collections.emptyList();
				}
			}
			cur += n;
			if(queryChars.length == cur){
				List<String> ret = new ArrayList<String>();
				prefix += new String(letters, n, letters.length - n);
				if(node.isTerminate()) ret.add(prefix);
				enumLetters(node, prefix, ret);
				return ret;
			}
			node = node.getChild(queryChars[cur]);
		}
		return Collections.emptyList();
	}

	@Override
	public void insert(String text){
		char[] letters = text.toCharArray();
		if(letters.length == 0){
			root.setTerminate(true);
		} else{
			root = root.insertChild(letters,  0, tails, tailBuilder);
		}
	}

	@Override
	public void trimToSize() {
		((StringBuilder)tails).trimToSize();
	}

	public void pack(){
		trimToSize();
		tailBuilder = null;
	}

	public TailBuilder getTailBuilder(){
		return tailBuilder;
	}

	private Node root = new Node();
	private TailBuilder tailBuilder;
	private CharSequence tails;
}
