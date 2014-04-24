package QueryEvaluation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class BoyerMooreAlg {

	public List<Integer> match(String query, String line) {
		List<Integer> matches = new Vector<Integer>();
		
		//We match a query of length n in a line of length m:
		int m = line.length();
		int n = query.length();

		//Preprocess the query for the right-to-left-scan and bad-character-shift rules
		//by finding the right-most positions of all characters in the query:
		Map<Character, Integer> rightMostIndexes = preprocessForBadCharacterShift(query);
	
		//We align q and l, starting on index 0 
		//(meaning the beginning of the query is aligned with position 0, i.e. the beginning of the line), 
		//and shift p to the left, until we reach the end of t:
		int alignedAt = 0;
		while (alignedAt + (n - 1) < m) {
			
			//On each aligned position, we scan the query from right to left, 
			//comparing the aligned characters at the current position in the line x 
			//and at the current position in the query y:
			for (int indexInquery = n - 1; indexInquery >= 0; indexInquery--) {
				int indexInline = alignedAt + indexInquery;
				char x = line.charAt(indexInline);
				char y = query.charAt(indexInquery);

				//If the query is longer than the line, 
				//we have no match here:
				if (indexInline >= m)
					break;

				//In the case of a mismatch, we do the shifting:
				if (x != y) {
					//We first retrieve the right-most index of the mismatching line-character in the query:
					Integer r = rightMostIndexes.get(x);

					//If the mismatching character in the line is not in the query 
					//we can shift until we are aligned behind the mismatch-position, 
					if (r == null) {
						alignedAt = indexInline + 1;
					}
					else {
						//Else we shift the query to the right 
						//until the right-most occurrence of x 
						//in the query is under the mismatch position in the line 
						//(if this shift is a forward shift, i.e. to the right), 
						//as this is the next possible place where an occurrence 
						//of the query can begin in the line:
						int shift = indexInline - (alignedAt + r);
						alignedAt += shift > 0 ? shift : alignedAt + 1;
					}

					break;
				}
				else if (indexInquery == 0) {
					//If the characters are equal and 
					//the query has been scanned completely from right to left,
					//we have a match at the currently aligned position in the line. 
					//We store the match and shift the query one position to the right:
					matches.add(alignedAt);
					alignedAt++;
				}
			}
		}
		return matches;
	}
	
	/*
	 * For each character in the string to preprocess,
	 * we store its right-most position by scanning the string from right to left, 
	 * storing the character as a key and its position as a value in a hash-map, 
	 * if it is not in the map already:
	 */
	private Map<Character, Integer> preprocessForBadCharacterShift(String query) {
		Map<Character, Integer> map = new HashMap<Character, Integer>();
		for (int i = query.length() - 1; i >= 0; i--) {
			char c = query.charAt(i);
			if (!map.containsKey(c)) map.put(c, i);
		}
		return map;
	}
	
	public static void main(String[] args) {
		//match ana in bananas, 
		List<Integer> matches = new BoyerMooreAlg().match("ana", "bananas");
		
		//print the matches found and simulate a simple unit test.
		for (Integer integer : matches) 
			System.out.println("Match at: " + integer);
			System.out.println((matches.equals(Arrays.asList(1, 3)) ? "OK" : "Failed"));
	}

}
