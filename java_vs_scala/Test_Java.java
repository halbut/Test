// To run (unoptimized):
// javac Test_Java.java
// date;java -Xms4g -Xmx12g Test_Java;date
// 
// Output (runtime = 2min 49sec):
// Thu Feb 27 15:34:11 PST 2014
// Result=19553937120
// Thu Feb 27 15:37:00 PST 2014
//
public class Test_Java {

    static int limit = 50000;
    static int[][] numbers = new int[limit][limit];

    public static int GCD(int n, int m) {
	int a = n;
	int b = m;
	int c = 0;
	while(a!=0 && b!=0) {
	    c = b;
	    b = a%b;
	    a = c;
	}
	if (a==0)
	    return b;
	else
	    return a;
    }

    public static void main(String[] args) {
	long sum = 0;
	for(int i=0; i < numbers.length; i++) {
	    for(int j=0; j < numbers[0].length; j++) {
		numbers[i][j] = GCD(i,j);
		sum += numbers[i][j];
	    }
	}

	System.out.println("Result="+sum);
    }
}
