// To run:
// scalac Test_Scala.scala
// date;java -Xms4g -Xmx12g -cp /Users/alper/.sbt/boot/scala-2.10.3/lib/scala-library.jar:. Test_Scala;date
//
// Output (runtime = 24sec):
// Thu Feb 27 14:52:30 PST 2014
// Result=2905630712
// Thu Feb 27 14:52:54 PST 2014
//  
object Test_Scala {

    var limit = 50000;
    var numbers:Array[Array[Int]] = Array.fill(limit,limit)(0)

    def GCD(n:Int, m:Int):Int = {
        var a = n
	var b = m
	var c = 0
	while (a!=0 && b!=0) {
	    c = b
	    b = a%b
	    a = c
	}
	if (a==0)
	   return b
	else 
  	   return a
    }

    def main(args: Array[String]) {
	var sum:Long = 0;
	for(i <- 0 to numbers.length-1) {
	    for(j <- 0 to numbers(0).length-1) {
		numbers(i)(j) = GCD(i,j);
		sum += numbers(i)(j);
	    }
	}

	System.out.println("Scala Result="+sum);
    }
}
