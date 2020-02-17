set terminal postscript  eps color "times" 20
set xlabel "Cantidad de Pivotes"
set ylabel "Ratio Cantidad de comparaciones"
set key top
set xrange [16:256]
set yrange [0:1]
set output "efecto-cant-pivotes-incremental.ps"

plot "EP-incremental.different.G1.tsv" using ($1):($2) title "1034" w lp,\
     "EP-incremental.different.G1.tsv" using ($1):($3) title "2943" w lp,\
     "EP-incremental.different.G1.tsv" using ($1):($4) title "12354" w lp,\
     "EP-incremental.different.G1.tsv" using ($1):($5) title "13093" w lp,\
     "EP-incremental.different.G1.tsv" using ($1):($6) title "15796" w lp,\
     "EP-incremental.different.G1.tsv" using ($1):($7) title "15964" w lp 

set xrange [64:1024]

plot "EP-incremental.different.G2.tsv" using ($1):($2) title "19032" w lp,\
     "EP-incremental.different.G2.tsv" using ($1):($3) title "22421" w lp,\
     "EP-incremental.different.G2.tsv" using ($1):($4) title "26903" w lp,\
     "EP-incremental.different.G2.tsv" using ($1):($5) title "27788" w lp,\
     "EP-incremental.different.G2.tsv" using ($1):($6) title "29426" w lp,\
     "EP-incremental.different.G2.tsv" using ($1):($7) title "29475" w lp,\
     "EP-incremental.different.G2.tsv" using ($1):($8) title "30420" w lp,\
     "EP-incremental.different.G2.tsv" using ($1):($9) title "34603" w lp,\
     "EP-incremental.different.G2.tsv" using ($1):($10) title "36280" w lp,\
     "EP-incremental.different.G2.tsv" using ($1):($11) title "36957" w lp,\
     "EP-incremental.different.G2.tsv" using ($1):($12) title "39612" w lp,\
     "EP-incremental.different.G2.tsv" using ($1):($13) title "46530" w lp