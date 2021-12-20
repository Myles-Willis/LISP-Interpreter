
To run the lisp interpreter, execute the makefile (in src folder) then execute the Interpreter.java file 
(javac Interpreter.java) on the CS lab machines

I was not able to implement the defun construct

Program Bugs:


1) There is a bug that arises in part of the #18 test case using car and cdr. 
The program seems to use car and car properly but for some reason a error exception I implemented is called.

2) I feel I have implemented the quote construct correctly overall, however there is minor output error where the quote symbol is printed out when called directly on an atom. 

Such as 'a vs '(a)






