title1="Libro+Te+Amo+Pero+Soy+Feliz+Sin+Ti.+Jaime+Jaramillo"
title2="El+Secreto+Rhonda+Byrne+Lvbp13"
title3="Libro+De+Italiano+Forza+2"
title4="Libro+La+Magia+Rhonda+Byrne+El+Secreto+Lvbp13"

# incremental_samePivotes - 4
# load data
pivotStrategy="incremental_differentPivotes"
pivotsQty="128"

echo "init ... $pivotStrategy $pivotsQty"

curl "http://localhost:8080/TesisFullGroovy/basicImplementation/initIndex?initMode=load&pivotStrategy=${pivotStrategy}&pivotsQty=${pivotsQty}"

echo ".. done"

LISTA="$title1 $title2 $title3 $title4"

for var in $LISTA 
do 
    curl "http://localhost:8080/TesisFullGroovy/basicImplementation/knnSearch?method=knnSearch&categ=1227Libros_Otros&neighbors=32&itemTitle=$var"
	curl "http://localhost:8080/TesisFullGroovy/basicImplementation/knnSearch?method=knnSearch&categ=1227Libros_Otros&neighbors=316&itemTitle=$var"
	curl "http://localhost:8080/TesisFullGroovy/basicImplementation/knnSearch?method=knnSearch&categ=1227Libros_Otros&neighbors=3163&itemTitle=$var"
done

