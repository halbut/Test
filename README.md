Test
====

Performance Tests, etc.

I wanted to see if Scala performance is in par with Java
and if one can quickly come up to speed in writing efficient
programs, without a few month’s experience.

I implemented a simple number crunching algorithm in Java
that does Nimber arithmetic in calculating number of 
different winning starts for a board game. Then implemented 
the same algorithm almost verbatim in Scala.  No magic data
structure is used, just simple array creation and computing 
of the values. I compiled the Scala version to avoid any
interpreter runs. I turned on optimizations to both algorithms
and ran both on using the same JVM a few times.

Results?

Java Runtime: 55sec 
Scala Runtime: 4hr 7min 5sec = 247min 5sec = 14825sec 

That is about a 270x speed up. 

I am pretty sure there might be faster ways to implement
it in Scala. I am worried that it takes much longer to learn 
these efficient ways and Scala compilers are not smart 
enough to optimize even that primitive code.

Please feel free to play around and let me know if you can 
get to make it work faster.

Update: It turns out one of the major problems was using List instead of an Array.
The code was updated to use Arrays everywhere and the new numbers are:

Java Runtime: 55sec 
Scala Runtime: 2min 33sec = 153sec

So that is about a 3x speed up, now.
