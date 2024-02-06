import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        try {
            list = Files.readAllLines(Path.of("train.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> names=names(list);
        List<List<String>> fin=new ArrayList<>();
        for (String s:list){
            fin.add(List.of(s.split(",")));
        }
        Scanner sc=new Scanner(System.in);
        System.out.println("Input number of clusters:");
        int k=sc.nextInt();

        List<List<List<String>>> clusters=new ArrayList<>();
        for(int i=0;i<k;i++){
            List<List<String>> tmp=new ArrayList<>();
            clusters.add(tmp);
        }
        int length=fin.size();
        int j=0;
        while(j<length){
            int randomNum = ThreadLocalRandom.current().nextInt(0, k );
            clusters.get(randomNum).add(fin.get(j));
            j++;
        }

        int cl=clusters.get(0).get(0).size() - 1;//used for centroid
        List<List<List<String>>> newclusters=new ArrayList<>();
        while(!newclusters.equals(clusters)) {
            if(!newclusters.isEmpty()){
                clusters=newclusters;
            }
            List<double[]> centroids = new ArrayList<>();
            for (int i = 0; i < clusters.size(); i++) {
                double[] centroid = new double[cl];
                Arrays.fill(centroid, 0);
                if(!clusters.get(i).isEmpty()) {
                    for (int h = 0; h < clusters.get(i).size(); h++) {
                        for (int l = 0; l < clusters.get(i).get(h).size() - 1; l++) {
                            centroid[l] = Double.parseDouble(clusters.get(i).get(h).get(l)) + centroid[l];
                        }
                    }
                }
                centroids.add(centroid);
            }
            for (int i = 0; i < centroids.size(); i++) {
                if(!clusters.get(i).isEmpty()) {
                    for (int h = 0; h < centroids.get(i).length; h++) {
                        centroids.get(i)[h] = centroids.get(i)[h] / clusters.get(i).size();
                    }
                }
            }
            newclusters = new ArrayList<>();
            for (int c = 0; c < k; c++) {
                List<List<String>> tmp = new ArrayList<>();
                newclusters.add(tmp);
            }
            double d = 0;
            for (int i = 0; i < clusters.size(); i++) {
                if(!clusters.get(i).isEmpty()) {
                    for (int h = 0; h < clusters.get(i).size(); h++) {
                        List<Double> distances = new ArrayList<>();
                        for (int c = 0; c < centroids.size(); c++) {
                            double distance = 0;
                            for (int l = 0; l < clusters.get(i).get(h).size() - 1; l++) {
                                distance = distance + (centroids.get(c)[l] - Double.parseDouble(clusters.get(i).get(h).get(l))) * (centroids.get(c)[l] - Double.parseDouble(clusters.get(i).get(h).get(l)));
                            }
                            distances.add(distance);
                        }
                        d+=Collections.min(distances);
                        int index = distances.indexOf(Collections.min(distances));
                        //change clusters
                        newclusters.get(index).add(clusters.get(i).get(h));
                    }
                }
            }

            System.out.println("Sum of distances between observations and centroids: " + d);
            for (int i = 0; i < newclusters.size(); i++) {
                System.out.println("Cluster" + (i + 1));
                if(!newclusters.get(i).isEmpty()) {
                    for (int c = 0; c < names.size(); c++) {
                        int p = 0;
                        for (int l = 0; l < newclusters.get(i).size(); l++) {
                            if (newclusters.get(i).get(l).get(newclusters.get(i).get(l).size() - 1).equals(names.get(c))) {
                                p++;
                            }
                        }
                        System.out.println(names.get(c) + " " + (double) p / (double) newclusters.get(i).size() * 100 + "%");
                    }
                    System.out.println();
                }
                else{
                    for (int c = 0; c < names.size(); c++) {
                        System.out.println(names.get(c) + " 0%");
                    }
                    System.out.println();

                }
            }
        }

    }
    public static List<String> names(List<String> train){
        List<String> tmp=new ArrayList<>();
        for(int i=0;i<train.size();i++){
            List<String> trn = List.of(train.get(i).split(","));
            if(!tmp.contains(trn.get(trn.size()-1))){
                tmp.add(trn.get(trn.size()-1));
            }
        }
        Collections.sort(tmp);
        return tmp;
    }
}
