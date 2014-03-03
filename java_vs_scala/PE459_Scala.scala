// To run:
// scalac -optimise PE459_Scala.scala
// date;java -Xms4g -Xmx12g -cp scala-library.jar:. PE459_Scala;date
//
// Result:
// Start:   Mon Mar  3 13:20:12 PST 2014
// End:     Mon Mar  3 13:22:45 PST 2014
// Runtime: 2min 33sec = 153sec


import scala.util.control.Breaks._

object PE459_Scala {

  var debug = false

   def generate(limit:Int) = {
     var W:List[Int] = Nil
     var H:List[Int] = Nil

     breakable { 
       for (i <- 1 to 2*limit) {
	 val w = i*i
	 if (w <= limit)
	   W = w :: W
	 val h = (i*(i+1))/2
	 if (h > limit) 
	   break
	 H = h :: H
      }
    }
 
    Array(W.reverse.toArray,H.reverse.toArray)
  }


  def row(limit:Int,V:Array[Int]) = {

    var R:Array[Int] = Array.fill(limit)(0)
    R(0) = 1
    var last:Array[Int] = Array.fill(1)(1)
    for(i <- 1 to limit-1) {
      var cache:Array[Int]  = Array.fill(V.length)(0)
      var mexList:List[Int] = Nil
      mexList = 0 :: mexList
      breakable {
        for(k <- 0 to V.length-1) {
          if (V(k) > i+1)
            break
          var tmp_num = 0
          if (V(k) == i+1) {               // starting a new size                     
             tmp_num = cache(k-1)          // use the last longest entry             
             for(t <- (i-V(k)+1) to (i-V(k-1)))
                tmp_num ^= R(t)
          } else if (V(k) > 1) {
             tmp_num = last(k)
             tmp_num ^= R(i-V(k))           // remove the first entry from last time    
             tmp_num ^= R(i-1)              // add the new entry                        
          }
          mexList = tmp_num :: mexList
          cache(k) = tmp_num
        }
      }
      var r = mex(mexList.toArray)
      if (debug) println("nim_mult_str="+mexList+"  R("+i+") = "+r)
      R(i) = r
      last = cache
    }
    R
  }

   def nim_sum(a:Int, b:Int) = a^b // Computes binary XOR

   def mex(input:Array[Int]) = {
      // This function takes an input list of numbers and returns the mex value of those numbers
      // That is, the function returns the minimal exluded value (the smallest number, from 0, not in the list)
      // e.g. mex(List(0, 1, 3)) = 2   while   mex(List(0, 1, 2)) = 3 and mex(List(1, 2, 3)) = 0

      // use the array like a hash
      var numArr = new Array[Int](input.length)

      for (i <- input) {
        if (i < input.length)  // no need to store large numbers
          numArr(i) = 1
      }

      // Define n to be the largest number in our array
      var mexValue = -1

      // Look for the numbers 0, 1, ..., n in the list of numbers
      // If any of those numbers are not in there, that is the mex value
      // If every number is contained in there, then the mex value is n+1
      breakable {
         for(i <- 0 to numArr.length-1) {
            if (numArr(i) != 1) {
	       mexValue = i
	       break
            } else {
               mexValue = i+1
            }
         }
      }
      mexValue
   }

    var nim_mult_cache:Array[Array[Long]] = Array.fill(10000, 10000)(-1)
    var max_queried_nim_mult_cache:Long = -2;

    def nim_mult(a:Long,b:Long):Long = {
        max_queried_nim_mult_cache = Math.max(max_queried_nim_mult_cache, Math.max(a,b))

        if ((a < nim_mult_cache.length) && (b < nim_mult_cache(0).length)) {
            if (nim_mult_cache(a.toInt)(b.toInt) != -1)
                return nim_mult_cache(a.toInt)(b.toInt)
        }

        var result:Long = nim_mult_fast(a.toInt,b.toInt)
        if ((a < nim_mult_cache.length) && (b < nim_mult_cache(0).length)) {
            nim_mult_cache(a.toInt)(b.toInt) = result.toInt
        }

        result
   }

    // Fermat two-powers (2^(2^n))
    var ftp:Array[Long] = Array(2L, 4L, 16L, 256L, 65536L, 4294967296L)

   def nim_mult_fast(n1:Long,n2:Long):Long = {
      var a:Long = n1
      var b:Long = n2
      if (a>b) { // enforce a<=b                                                                                                         
         var t:Long=a
         a=b
         b=t
      }

      if (a==0) return 0;
      if (a==1) return b;

      var ftpp:Long = 0; // Fermat two-power previous to b

      for ( i <- 0 to ftp.length-1) {
            if (ftp(i)==b) { // if b is a Fermat two-power use a rule                                                                      
                if (a==b)
                    return 3*a/2
                else
                    return a*b
            } else if (ftp(i)<b) {
                ftpp=ftp(i)
            }
        }
        var bb:Long = 1
        while (bb < b) {
            if ((b&bb) != 0) { // if b isn't a power of two reduce by distributive law
                return nim_mult(bb,a)^nim_mult(a, b-bb);
            }
            bb *=2
        }

        if (a%ftpp != 0)  // avoid infinite recursion
            return nim_mult(ftpp, nim_mult(a, b/ftpp));

        return nim_mult((ftpp*3)/2, nim_mult(a/ftpp, b/ftpp));
    }

   def getRanges(S:Array[Int], V:Array[Int]):Array[Long] = {
      var nRanges:Array[Long] =  Array.fill(V.length+1)(0)
      var ranges:Array[Long] =  Array.fill(V.length+1)(0)
      ranges(0) = 0;
      for (n <- 1 to V.length) {
         ranges(n) = ranges(n-1)^V(n-1)
         for (s <- S) {
            if (s<=n)
	       nRanges((ranges(n)^ranges(n-s)).toInt) += 1
         }
	 if (debug) {
	    for (i<-0 to ranges.length-1) {
               if (ranges(i) != 0)
                  println("ranges["+i+"]="+ ranges(i))
            }
         }
      }
      if (debug) {
         for (i<-0 to nRanges.length-1) {
            if (nRanges(i) != 0)
               println("nRanges["+i+"]="+ nRanges(i))
         }
      }

      nRanges
    }


   def findCount(limit:Int, tot:Long, A:Array[Array[Int]], R:Array[Int], C:Array[Int]) = {

      var nCol:Array[Long] = getRanges(A(0), R)
      var nRow:Array[Long] = getRanges(A(1), C)

      var count:Long = 0
      println("Calculating Solutions")
      for (i <- 1 to limit-1) {
         breakable {  
            if (nRow(i) == 0)
               break
            for (j <- 1 to limit-1) {
               breakable {  
                  if (nCol(j) == 0)
                     break
                  if (tot == nim_mult(i,j)) {
                     var prod = nRow(i)*nCol(j)
                     println("i="+i+" j="+j+"  "+nRow(i)+" * "+ nCol(j) + " = " + prod + "   (total="+(count+prod)+")");
                     if (prod > 0)
                        count += prod
                  }
               }
            }
         }
      }
      count
   } 
   

  def PE_459(limit:Int) = {
    println("======= Started 459 for limit=" + limit + " ========")
    println("Generating squares and triangle numbers");
    val A = generate(limit)
    println("Generating main row and column vectors")
    var R = row(limit,A(0))
    var C = row(limit,A(1))

    println("Calculating the score of the full matrix")
    var totRow = 0;
    for (r <- R)
      totRow ^= r;
    var totCol = 0;
    for (c <- C)
      totCol ^= c;
    var tot:Long = nim_mult(totRow, totCol)
    println("nim_sum(M)="+tot)

    findCount(limit, tot, A, R, C)
  }

  def main(args: Array[String]) {
     var limit= 1000000
     println("W("+limit+")="+PE_459(limit))
  }
}
