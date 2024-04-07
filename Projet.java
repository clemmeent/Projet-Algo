import java.util.Arrays;
import java.util.Random;

public class Projet{
    public static void main(String[] args){
        //Q1
        int[][] tab = {
                {2,2,3,4,2},
                {6,5,1,2,8},
                {1,1,10,1,1}
        };//création du tableauui correspondra à G
        System.out.println("Grille G:");
        for (int i=2; i>-1; i--){
            System.out.printf("G[%x] : %s \n", i, Arrays.toString(tab[i]));//affiche chaque ligne du tableau
        }
        //Q2
        System.out.println("Valeurs des chemins gloutons depuis les cases (0,d) : Ng = " + Arrays.toString(glouton(tab)) + '\n');//nombre de pucerons mangés glouton en partant de chaque colonne

        //Q3 à 6
        int [][][] MA = calculerMA(tab,0);//création du tableau MA en commancant à la colonne 0
        int[][] M = MA[0];//tableau crée à partir de MA et a comme 1er indice 0 qui regroupe les nombres de pucerons
        int[][] A = MA[1];//tableau crée à partir de MA et a comme 1er indice 1 qui regroupe les indices
        System.out.println("Programmation dynamique, case de d ́epart (0, 0)"+'\n'+"M :");
        for (int i=2; i>-1; i--){
            System.out.printf("M[%x] : %s \n", i, Arrays.toString(M[i]));//affiche les 3 premières lignes de M
        }
        acnpm(M,A);//affiche le chemin suivit

        System.out.println("Programmation dynamique, case de d ́epart (0, 1)"+'\n'+"M :");
        MA = calculerMA(tab,1);//création du tableau MA en commancant à la colonne 1
        M = MA[0];//tableau crée à partir de MA et a comme 1er indice 0 qui regroupe les nombres de pucerons
        A = MA[1];//tableau crée à partir de MA et a comme 1er indice 1 qui regroupe les indices
        for (int i=2; i>-1; i--){
            System.out.printf("M[%x] : %s \n", i, Arrays.toString(M[i]));//affiche les 3 premières lignes de M
        }
        acnpm(M,A);//affiche le chemin suivit

        //Q6 et 7
        System.out.println("\nChemins max. depuis toutes les cases de d ́epart (0,d)");
        for (int i=0; i<tab[0].length;i++){
            MA = calculerMA(tab,i);
            M = MA[0];//tableau crée à partir de MA et a comme 1er indice 0 qui regroupe les nombres de pucerons
            A = MA[1];//tableau crée à partir de MA et a comme 1er indice 1 qui regroupe les indices
            acnpm(M,A);//affiche le chemin suivit
        }

        //Q8
        int[] Ng   = glouton(tab);
        int[] Nmax = optimal(tab);
        System.out.printf("\nNg = %s \nNmax = %s \nGains relatifs = %s", Arrays.toString(Ng),Arrays.toString(Nmax),Arrays.toString(gainRelatif(Nmax,Ng)));

        //Q8 gain relatif du tableau
        System.out.println("\n");
        System.out.println("Gain relatif du tableau de la question 8");
        int[][] tab8 = { {2,4,3,9,6}, {1,10,15,1,2},{2,4,11,26,66},{36,34,1,13,30},{46,2,8,7,15},{89,27,10,12,3},{1,72,3,6,6},{3,1,2,4,5} };
        int[] Ng8 = glouton(tab8);//nombre pucerons mangés en glouton
        int[] Nmax8 = optimal(tab8);//nombre pucerons mangés en optimal
        System.out.println("Gain relatif du tableau 8 : "+ Arrays.toString(gainRelatif(Nmax8,Ng8)));
        System.out.printf("Ng = %s\nNmax = %s\n ", Arrays.toString(Ng),Arrays.toString(Nmax));

        //Q9
        Random Ran = new Random();
        System.out.println("\nVALIDATION STATISTIQUE :");
        int nruns = 10;//10 au lieu de 10000 pour mieux visualiser la trace d'éxécution du programme
        System.out.println("nRuns = "+nruns+" (possibilité d'en avoir 10^4)");
        System.out.println("L au hasard dans [5:16]");
        System.out.println("G au hasard dans [5:16]");
        System.out.println("Nb. de pucerons / case au hasard dans [0:L*C]");

        //Codes de la grille aléatoire
        for(int n=0; n<10;n++){
            int[][] T = new int[Ran.nextInt(11) + 5][Ran.nextInt(11) + 5];//tableau de taille L et C avec L et C un nombre aléatoire entre (0 et 11)+5
            int L = T.length;//nombre de lignes
            int C = T[0].length;//nombre de colonnes
            for (int i = 0; i < L; i++) {
                for (int j = 0; j < C; j++) {
                    T[i][j] = Ran.nextInt(L * C); //rempli chaque case par un nombre entre 0 et L*C
                }
                T[i] = permutationAleatoire(T[i]);//mélange les cases d'une même ligne
            }
            int[] NgRan= glouton(T);
            int[] NmaxRan = optimal(T);
            System.out.printf("\nrun %d / %d, (L,C) = (%d,%d) :\n",n+1,nruns,L,C);
            System.out.printf("Ng[%d] = %s \nNmax[%d] = %s \nGain[%d] = %s \n",n+1, Arrays.toString(NgRan), n,Arrays.toString(NmaxRan),n,Arrays.toString(gainRelatif(NmaxRan,NgRan)));
        }
    }
    //Q1
    public static int glouton(int[][]G, int d){
        int n = G.length;
        int c = G[0].length;
        int nbPuMa = G[0][d];
        int p = d;
        for (int i=1; i < n; i++){
            int nPuc = G[i][d];
            if (p-1>=0){
                nPuc = max(nPuc, G[i][p-1]);
            }
            if(p+1<c){
                nPuc = max(nPuc,G[i][p+1]);
            }
            if (p-1>=0 && nPuc == G[i][p-1]){
                p--;
            }else if (p+1<c && nPuc == G[i][p+1]){
                p++;
            }
            nbPuMa = nbPuMa+nPuc;
        }
        return nbPuMa;
    }
    public static int max(int x, int y) {
        if(x>y){
            return x;
        }
        return y;
    }


    //Q2
    public static int[] glouton(int[][]G){
        int n=G[0].length;
        int[] N = new int [n];
        for(int i=0; i<n; i++){
            N[i] = glouton (G,i);
        }
        return N;
    }

    //Q3 et Q4
    public static int[][][] calculerMA(int[][] G, int d){
        int n=G.length; //lignes
        int c = G[0].length;//colonnes
        int[][] M = new int[n][c];//tab stock val max
        int[][] A = new int[n][c];//tab stock indices

        //Base pour la case (0,d)
        for (int i=0; i<c; i++){
            if (i==d){
                M[0][i] = G[0][d];
                A[0][i] = 0;
            }
            else {
                M[0][i] = -1;
                A[0][i] = -1;
            }
        }

        //Heredite
        for (int i=1; i<n; i++){
            for (int j=0; j<c;j++){
                int val = M[i-1][j];
                if (j-1>=0){
                    val = max ( val, M[i-1][j-1] );
                }
                if (j+1<c){
                    val = max ( val, M[i-1][j+1] );
                }

                if (val==-1){
                    M[i][j]=-1;
                    A[i][j]=-1;
                }else{
                    M[i][j] = G[i][j] + val;
                    if(j-1>=0 && val==M[i-1][j-1]){
                        A[i][j] = j-1;
                    }
                    else if(j+1<c && val==M[i-1][j+1]){
                        A[i][j] = j+1;
                    }
                    else{
                        A[i][j] = j;
                    }
                }
            }
        }
        return new int[][][]{M,A};
    }

    //Q5
    public static void acnpm(int[][] M, int[][] A){
        int L = M.length;
        int cStar = argMax(M[L-1]) ; // colonne d’arriv ́ee du chemin max. d’origine (0,d)
        acnpm(A, L-1, cStar); // affichage du chemin maximum de (0,d) à (L-1, cStar)
        System.out.println("Valeur : "+ valMa(M[L-1]));//affiche le nombre de pucerons mangés
    }

    public static void acnpm(int[][] A, int l, int c) {
        if (l == 0) {
            System.out.printf("un chemin maximum : (%d,%d)", 0, c);//affiche les indices de la case où on se situe
            return;//on s'arrète quand on arrive à la dernière ligne
        }
        acnpm(A, l - 1, A[l][c]);//recommence en avançant d'une ligne
        System.out.printf("(%d,%d) ", l, c);//affiche la case où on est passé
    }
    public static int argMax(int[] T){
        int valMax = T[0];//initialise le max avec la première valeur du tableau
        int indColonne = 0;//initialise l'argument du max
        for (int i=0; i<T.length; i++){
            if(valMax<=T[i]){
                valMax = T[i];//met à jour le max du tab
                indColonne = i;//met à jour la colonne du max
            }
        }
        return indColonne;
    }
    public static int valMa(int[] tab){
        int valMa = tab[0];//initialise le max avec la première valeur du tableau
        for (int i=1; i<tab.length; i++){
            valMa = max(valMa,tab[i]);//met à jour le max du tab
        }
        return valMa;
    }
    //Q6
    public static int optimal(int[][]G, int d){
        int [][]M = calculerMA(G,d)[0];//tableau crée à partir de MA et a comme 1er indice 0 regroupe les nombres de pucerons
        return valMa(M[M.length-1]);//nombre max mangé à partir de la colonne d
    }

    //Q7
    public static int[] optimal(int [][]G){
        int C = G[0].length;//nombre de colonne de G
        int[] N = new int [C];//tableau de taille du nombre de colonne

        for(int i=0; i<C; i++){
            N[i] = optimal (G,i);//nombre de pucerons mangés optimal à partir de la colonne i
        }
        return N;
    }

    //Q8
    public static float[] gainRelatif(int[] Nmax, int[] Ng){
        int n = Nmax.length;
        float[] gain = new float[n];
        for (int i=0; i<n;i++){
            gain[i] = (float)(Nmax[i] - Ng[i]) / (float) Ng[i];
        }
        return gain;
    }

    //Q9
    static int[] permutationAleatoire(int[] T){ int n = T.length;
        // Calcule dans T une permutation al ́eatoire de T et retourne T
        Random rand = new Random(); // biblioth`eque java.util.Random
        for (int i = n; i > 0; i--){
            int r = rand.nextInt(i); // r est au hasard dans [0:i]
            permuter(T,r,i-1);
        }
        return T;
    }
    static void permuter(int[] T, int i, int j){
        int ti = T[i];
        T[i] = T[j];
        T[j] = ti;
    }

}