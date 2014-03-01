// To run:
// javac -O PE459_Java.java
// date;java -Xms4g -Xmx12g -cp scala-library.jar:. PE459_Java;date
//
// Result:
// Start:   Thu Feb 27 23:18:33 PST 2014
// End:     Thu Feb 27 23:19:28 PST 2014
// Runtime: 55sec


import java.util.ArrayList;
import java.util.Arrays;

class PE459_Java {

    public static boolean debug = false;

    static ArrayList<ArrayList<Integer>> generate(int limit) {

       ArrayList<Integer> W = new ArrayList<Integer>();
       ArrayList<Integer> H = new ArrayList<Integer>();

       for (int i=1; i <= 2*limit; i++) {
	   int w = i*i;
	   if (w <= limit)
	       W.add(w);
	   int h = (i+w)/2;
	   if (h > limit) 
	       break;
	   H.add(h);
       }
 
       ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
       result.add(W);
       result.add(H);

       return result;
    }

    static int[] row(int limit, ArrayList<Integer> V) {

	int[] R = new int[limit];
        R[0] = 1;
        int[] last = {1};
	for(int i=1; i < limit; i++) {
	    int[] cache = new int[V.size()];
	    ArrayList<Integer> mexList = new ArrayList<Integer>();
	    mexList.add(0);
	    for (int k=0; k < V.size(); k++) {
		if (V.get(k) > i+1)
		    break;
		int tmp_num = 0;
		if (V.get(k) == i+1) {               // starting a new size 
                    tmp_num = cache[k-1];             // use the last longest entry
		    for(int t=i-V.get(k)+1; t < i-V.get(k-1)+1; t++) {
			tmp_num ^= R[t];
		    }
		} else if (V.get(k) > 1) {
		    tmp_num = last[k];
		    tmp_num ^= R[i-V.get(k)];       // remove the first entry from last time
		    tmp_num ^= R[i-1];              // add the new entry
		}
		mexList.add(tmp_num);
		cache[k] = tmp_num;
	    }
	    int r = mex(mexList);
	    if (debug) System.out.println("nim_mult_str="+mexList+"  R("+i+") = "+r);
	    R[i] = r;
	    last = cache;
	}
	return R;
    }

    static int nim_sum(int a, int b) {  return a^b; }   // Computes binary XOR

    static int mex(ArrayList<Integer> input) {
	// This function takes an input list of numbers and returns the mex value of those numbers
	// That is, the function returns the minimal exluded value (the smallest number, from 0, not in the list)
	// e.g. mex(List(0, 1, 3)) = 2   while   mex(List(0, 1, 2)) = 3 and mex(List(1, 2, 3)) = 0

	// use the array like a hash
	int[] numArr = new int[input.size()];

	//	System.out.print("Mex input List =");
	for (int i=0; i < input.size(); i++) {
	    //            System.out.print(input.get(i));
	    if (input.get(i) < input.size())     // no need to store large numbers
		numArr[input.get(i)] = 1;
	    //        print(numArr[i]+" ")
	}
	//	System.out.println();

	// Define n to be the largest number in our array
	int mexValue = -1;

	// Look for the numbers 0, 1, ..., n in the list of numbers
	// If any of those numbers are not in there, that is the mex value
	// If every number is contained in there, then the mex value is n+1
	for(int i=0; i < numArr.length; i++) {
	    if(numArr[i] != 1) {
		mexValue = i;
		break;
	    } else {
		mexValue = i+1;
	    }
	}
	return mexValue;
    }

    static long[][] nim_mult_cache;
    static long max_queried_nim_mult_cache = -2;

    static long nim_mult(long a, long b) {
        max_queried_nim_mult_cache = Math.max(max_queried_nim_mult_cache, Math.max(a,b));

        if ((a < nim_mult_cache.length) && (b < nim_mult_cache[0].length)) {
	    if (nim_mult_cache[(int)a][(int)b] != -1)
		return nim_mult_cache[(int)a][(int)b];
	}
	    
	long result = nim_mult_fast(a,b);
        if ((a < nim_mult_cache.length) && (b < nim_mult_cache[0].length)) {
	    nim_mult_cache[(int)a][(int)b] = result;
	}

	return result;
    }

    // Fermat two-powers (2^(2^n))
    static final long[] ftp = { 2L, 4L, 16L, 256L, 65536L, 4294967296L };

    static long nim_mult_fast(long a, long b) {
	if (a>b) { // enforce a<=b
	    long t=a;
	    a=b;
	    b=t;
	}

	if (a==0) return 0;
	if (a==1) return b;

	long ftpp=0; // Fermat two-power previous to b

	for (int i=0; i < ftp.length; i++) {
	    if (ftp[i]==b) { // if b is a Fermat two-power use a rule
		if (a==b)
		    return 3*a/2;
		else
		    return a*b;
	    } else if (ftp[i]<b)
		ftpp=ftp[i];
	}
	for (long bb=1; bb<b; bb*=2) {
	    if ((b&bb) != 0) // if b isn't a power of two reduce by distributive law
		return nim_mult(bb,a)^nim_mult(a, b-bb);
	}
	
	if (a%ftpp != 0) // avoid infinite recursion
	    return nim_mult(ftpp, nim_mult(a, b/ftpp));

	return nim_mult(ftpp*3/2, nim_mult(a/ftpp, b/ftpp));
    }

    static long[] getRanges(ArrayList<Integer> S, int[] V)  {
	long[] nRanges = new long[V.length+1];
	long[] ranges = new long[V.length+1];
	ranges[0] = 0;
	for (int n=1; n <= V.length; n++) {
	    ranges[n] = ranges[n-1]^V[n-1];
	    for (int s : S){
		if (s<=n) 
		    nRanges[(int)(ranges[n]^ranges[n-s])]++;
	    }
	    if (debug) {
		for (int i=0; i < ranges.length; i++) {
		    if (ranges[i] != 0)
			System.out.println("ranges["+i+"]="+ ranges[i]);
		}
	    }
	}
	if (debug) {
	    for (int i=0; i < nRanges.length; i++) {
		if (nRanges[i] != 0)
		    System.out.println("nRanges["+i+"]="+ nRanges[i]);
	    }
	}

	return nRanges;
    }

    static long findCount(int limit, long tot, ArrayList<ArrayList<Integer>> A, int[] R, int[] C)  {

	long[] nRow = getRanges(A.get(0), C);
	long[] nCol = getRanges(A.get(1), R);

	long count = 0;
	System.out.println("Calculating Solutions");
	for (int i=1; i <= limit; i++) {
	    if (nRow[i]==0)
		continue;
	    for (int j=1; j < limit; j++) {
		if (nCol[j] == 0) {
		    continue;
		}
		if (tot == (int)nim_mult(i,j)) {
		    long prod = nRow[i]*(long)nCol[j];
		    System.out.println("i="+i+" j="+j+"  "+nRow[i]+" * "+ nCol[j] + " = " + prod + "   (total="+(count+prod)+")");
		    if (prod > 0) {
			count += prod;
		    }
		}
	    }
	}

	return count;
    }

    public static long PE_459(int limit) {
	System.out.println("======= Started 459 for limit=" + limit + " ========");
	nim_mult_cache = new long[10000+1][10000+1];
	for (long[] row : nim_mult_cache)
	    Arrays.fill(row, -1);

	System.out.println("Generating squares and triangle numbers");
	ArrayList<ArrayList<Integer>> A = generate(limit);

	System.out.println("Generating main row and column vectors");
	int[] C = row(limit,A.get(0));
	int[] R = row(limit,A.get(1));

        System.out.println("Calculating the score of the full matrix");

	long totRow = 0;
	for (int r : R)
	    totRow ^= r;
	long totCol = 0;
	for (int c : C)
	    totCol ^= c;
	long tot = nim_mult(totRow, totCol);
	System.out.println("nim_sum["+limit+"]="+tot);

        return findCount(limit, tot, A, R, C);
    }

    public static void main(String[] args) {
	int limit= 1000000;
	System.out.println("W("+limit+")="+PE_459(limit));
    }
}
