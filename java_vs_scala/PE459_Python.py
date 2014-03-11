# date;pypy /Users/alper/git/test/java_vs_scala/PE459_Python.py;date

debug = False

def generate(limit):
    W = []
    H = []
    h = 0
    i = 0
    while (h <= limit):
        i = i + 1
        w = i*i
        if (w <= limit): W.append(w)
        h = (i*(i+1))/2
        if (h <= limit): H.append(int(h))
    return (W, H)

def row(limit, V):
    R  = [0]*limit
    R[0] = 1
    last  = [1]
    for i in range(1, limit): 
      cache = [0] * len(V)
      mexList = []
      mexList += [0]
      vv = V[0]
      k = -1
      while (vv <= i+1 and k < len(V)-1):
        k = k + 1
        vv = V[k]
        if (vv <= i+1):
          tmp_num = 0
          if (V[k] == i+1) :      #         // starting a new size                     
             tmp_num = cache[k-1] #         // use the last longest entry             
#             nprint V[k], V[k-1]
             for t in range(i-V[k]+1, i-V[k-1]+1):
                tmp_num ^= R[t]
          elif (V[k] > 1):
             tmp_num = last[k]
             tmp_num ^= R[i-V[k]] #          // remove the first entry from last time    
             tmp_num ^= R[i-1]    #          // add the new entry                        
          mexList.append(tmp_num)
          cache[k] = tmp_num
      mexList.reverse()
      r = mex(mexList)
      R[i] = r
      last = cache
    return R


def nim_sum(a, b) : return a^b #// Computes binary XOR

def mex(input):
  #// This function takes an input list of numbers and returns the mex value of those numbers
  #// That is, the function returns the minimal exluded value (the smallest number, from 0, not in the list)
  #// e.g. mex(List(0, 1, 3)) = 2   while   mex(List(0, 1, 2)) = 3 and mex(List(1, 2, 3)) = 0

  #// use the array like a hash
  numArr = [0]*len(input)
  input_len = len(input)
  for i in input:
    if (i < input_len):  #// no need to store large numbers
      numArr[i] = 1
  

  #// Define n to be the largest number in our array
  mexValue = 0

  #// Look for the numbers 0, 1, ..., n in the list of numbers
  #// If any of those numbers are not in there, that is the mex value
  #// If every number is contained in there, then the mex value is n+1
  while (1 == numArr[mexValue]): 
        mexValue = mexValue + 1
  return mexValue



def nim_mult(a, b):
    global max_queried_nim_mult_cache
    max_queried_nim_mult_cache = max(max_queried_nim_mult_cache,a,b)

    if ((a < len(nim_mult_cache)) and (b < len(nim_mult_cache[0]))):
        if (nim_mult_cache[int(a)][int(b)] != -1):
            return nim_mult_cache[int(a)][int(b)]

    result = nim_mult_fast(int(a),int(b))
    if ((a < len(nim_mult_cache)) and (b < len(nim_mult_cache[0]))):
        nim_mult_cache[int(a)][int(b)] = int(result)

    return int(result)


#// Fermat two-powers (2^(2^n))
ftp = (2L, 4L, 16L, 256L, 65536L, 4294967296L)

def nim_mult_fast(n1,n2):
    a = int(n1)
    b = int(n2)
    if (a>b):
        a, b = b, a
    if (a==0):
        return 0
    if (a==1):
        return b

    ftpp = 0 # // Fermat two-power previous to b

    for  i in range(0, len(ftp)): 
        if (ftp[i]==b):
            if (a==b):
                return int(3*a/2)
            else:
                return int(a*b)
        elif (ftp[i]<b):
            ftpp=ftp[i]
    bb = 1
    while (bb < b):
        if ((b&bb) != 0): #   if b isn't a power of two reduce by distributive law
            return nim_mult(bb,a)^nim_mult(a, b-bb)
        bb *=2
    if (a%ftpp != 0): #  // avoid infinite recursion
        return nim_mult(ftpp, nim_mult(a, int(b/ftpp)))

    return nim_mult(int((ftpp*3)/2), nim_mult(int(a/ftpp), int(b/ftpp)))


def getRanges(S, V):
    nRanges =  [0]*(len(V)+1)
    ranges =  [0]*(len(V)+1)
    for n in range(1, len(V)+1):
        ranges[n] = ranges[n-1]^V[n-1]
        for s in  S:
            if (s<=n):
                nRanges[int(ranges[n]^ranges[n-s])] += 1
    if (debug):
        for i in range(0, len(ranges)):
            if (ranges[i] != 0):
                print "ranges[", i, "]=", ranges[i]
    if (debug):
        for i in range(0, len(nRanges)):
            if (nRanges[i] != 0):
                print "nRanges[", i, "]=", nRanges[i]
    return nRanges


def findCount(limit, tot, A, R, C):

  nCol = getRanges(A[0], R)
  nRow = getRanges(A[1], C)
  #print nCol
  #print nRow

  count = 0
  print "Calculating Solutions"
  loop_i = True
  i = 0
  while (loop_i and  i < limit-1):
     i = i + 1
     if (nRow[i] == 0):
        loop_i = False
     else:
        loop_j = True
        j = 0
        while (loop_j and j < limit - 1):
            j = j + 1
            if (nCol[j] == 0):
                loop_j = False
            else:
                #print nim_mult(i,j)
                if(tot == nim_mult(i,j)):
                    prod = nRow[i]*nCol[j]
                    print tot, "i=", i, " j=", j, "  ", nRow[i], " * ", nCol[j], " = ", prod, "   (total=", count+prod, ")"
                    if (prod > 0):
                        count += prod
  return count


def PE_459(limit):
    print "======= Started 459 for limit=",  limit, " ========"
    print "Generating squares and triangle numbers"
    A = generate(limit)
    print "Generating main row and column vectors"
    R = row(limit,A[0])
    C = row(limit,A[1])
    #print A[0]
    #print A[1]
    #print R
    #print C
    print "Calculating the score of the full matrix"
    totRow = 0
    for r in  R:
      totRow ^= r
    totCol = 0
    for c in C:
      totCol ^= c
#    print totRow
#    print totCol
    tot = nim_mult(totRow, totCol)
    print "nim_sum(M)=", tot
    return findCount(limit, tot, A, R, C)

#import numpy as np
#nim_mult_cache = -1*np.ones([1000,1000],dtype='int64')
nim_mult_cache = [[-1 for i in range(10000)] for i in range(10000)]
max_queried_nim_mult_cache = -2
limit = 1000000
PE = PE_459(limit)
print "W[", limit, "]=", PE


