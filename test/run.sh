BUILD=~/gem/cedar/build
CP=.:$BUILD/product/lib/gemfire.jar:$BUILD/product/lib/antlr.jar:$BUILD/tests/classes
javac -cp $CP TurbineData.java
javac -cp $CP AvgTurbinePower.java
javac -cp $CP AvgPowerCalculator.java
javac -cp $CP TurbineAnalyzer.java
javac -cp $CP Server.java

if [ $1 == "server" ]; then
 echo "Starting Server..."
 java -Xms1500m -Xmx1500m -cp $CP Server
else
 echo "Starting Client..."
# java -cp $CP Client
fi
