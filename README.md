README
======

Program Specifications:
-----------------------
DarwinCode creates LDPC codes using a simplified genetic algorithm. Working LDPC codes can be stored and their performance can be compared to a Hamming code and other LDPC codes. These codes will be manipulated through the extensive manipulation of two dimensional arrays, and each object can be saved to a file and re-initialized later. This program does not use a graphics library, instead it will be displayed through the command line. The user can create an initial parity-check matrix for the algorithm to start with or allow the usage of a randomly generated matrix. Different parameters of the generator algorithm can be chosen. DarwinCode will then evaluate the performance of the code and its offspring, and stop once a specified number of acceptable solutions have been found, a set limit is reached, or it is manually terminated.

DarwinCode includes:
--------------------
+ A generator for a random LDPC code
+ A pseudo-genetic algorithm to find good codes
+ A rudimentary bit-flipping channel implementation
+ A method for saving/loading codes
+ Ability to export a code as plaintext
x+ A hard-decision decoder implementation

## Contact ##
+ email: manueljosh3[at]gmail.com
+ twitter: @QuiteRather
