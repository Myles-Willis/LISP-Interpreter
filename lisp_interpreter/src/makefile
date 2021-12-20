
JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES = \
        Interpreter.java \
        Operator.java \
        Parser.java \
        Token.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class