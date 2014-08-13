BUILD=~/gem/cedar/build
CP=.:$BUILD/product/lib/gemfire.jar:$BUILD/product/lib/antlr.jar:./lib/stream.jar:./classes
rm -rf classes
mkdir classes
javac -cp $CP *.java -d classes

echo "## Starting Server..."
java -Xms1500m -Xmx1500m -cp $CP Server
