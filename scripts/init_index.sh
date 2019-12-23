pivotStrategy=$1 # random_differentPivotes
pivotsQty=$2 # 4

echo "init index... $pivotStrategy $pivotsQty"

curl -sS "http://localhost:8090/TesisFullGroovy/basicImplementation/initIndex?initMode=load&pivotStrategy=${pivotStrategy}&pivotsQty=${pivotsQty}"

echo "init index... done"