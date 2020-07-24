package phonebook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.StrictMath.abs;

public class Main {
    public static String readFileAsString(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
    private static class TableEntry<T> {
        private final String key;
        private final T value;
        public boolean removed;

        public TableEntry(String key, Object value, boolean removed) {
            this.key = key;
            this.value = (T) value;
            this.removed = false;
        }

        public String getKey() {
            return key;
        }

        public T getValue() {
            return value;
        }

        public boolean getRemoved(){
            return removed;
        }

        public void removed2(){
            this.removed = true;
        }

    }

    private static class HashTable<T> {
        private int size;
        private TableEntry[] table;
        private int count = 0;

        public HashTable(int size) {
            this.size = size;
            table = new TableEntry[size];
        }

        public boolean put(String key, String value, boolean type) {
            int idx = findKey(key.hashCode());

            if (idx == -1) {
                rehash();
                idx = findKey(key.hashCode());
                table[idx] = new TableEntry(key, value,type);
                return true;
            }
            table[idx] = new TableEntry(key, value,type);
            return true;

        }

        public T get(int key) {
            int idx = findKey(key);

            if (idx == -1 || table[idx] == null) {
                return null;
            }

            return (T) table[idx].getValue();
        }

        private int findKey(Integer key) {
            int hash = abs(key % size);

            while (!(table[hash] == null || table[hash].getKey().hashCode() == key.hashCode())) {
                hash = (hash + 1) % size;

                if (hash == key % size) {
                    return -1;
                }
            }

            return hash;
        }

        public void entrySet() {
            StringBuilder tableStringBuilder = new StringBuilder();

            for (int i = 0; i < table.length; i++) {
                if (table[i] == null) {
                    continue;
                } else {
                    System.out.println(table[i].getKey() + ": "
                            + table[i].getValue());
                }
            }
        }

        public void remove(int key) {
            for (int i = 0; i < table.length; i++) {
                if (table[i] == null) {
                    continue;
                }
                else if (table[i].getKey().hashCode() == key) {
                    table[i].removed2();
                    break;
                }
            }


        }


        private void rehash(){
            TableEntry[] temp = table.clone();
            size = size * 2;
            table = new TableEntry[size];
            for (int i=0; i<temp.length; i++) {
                put(temp[i].getKey(), (String) temp[i].getValue(), temp[i].getRemoved());
            }
            count = 0;
        }

        @Override
        public String toString() {
            StringBuilder tableStringBuilder = new StringBuilder();

            for (int i = 0; i < table.length; i++) {
                if (table[i] == null) {
                    tableStringBuilder.append(i + ": null");
                } else {
                    tableStringBuilder.append(i + ": key=" + table[i].getKey()
                            + ", value=" + table[i].getValue() + ", removed="+table[i].getRemoved());
                }

                if (i < table.length - 1) {
                    tableStringBuilder.append("\n");
                }
            }

            return tableStringBuilder.toString();
        }
    }
    public static void main(String[] args) {

        String sentence = "";
        String find = "";
        String pathToFile = "C:\\Users\\Yi Hui\\Desktop\\directory.txt";
        String pathToFile2 = "C:\\Users\\Yi Hui\\Desktop\\find.txt";
        try {
            sentence = readFileAsString(pathToFile);
            find = readFileAsString(pathToFile2);

        }
        catch (IOException e){
            System.out.println("Cannot print file: " + e.getMessage());
        }
        String[] findlst = find.split("[\n]"); //list of names to find
        String[] sentencelst = sentence.split("[\n]"); //directory of phone and numbers

        String[] names = new String[sentencelst.length];

        for (int i = 0; i<sentencelst.length;i++){
            String[] strings = sentencelst[i].split(" ");
            if (strings.length == 3) names[i] = (strings[1] + " " + strings[2]);
            else if (strings.length == 2) names[i] =(strings[1]);

        }
        System.out.println("Start searching (linear search)...");
        long difference = linearsearch(findlst,sentencelst);
        System.out.println("");
        /*
        long bubbleJump = System.currentTimeMillis();
        System.out.println("Start searching (bubble sort + jump search) ...");

        String[] sortedNames = names.clone();
        long result = bubbleSort(sortedNames,difference);
        long difference2 = 0;
        if (result == -1){
            System.out.println("");
            linearsearch(findlst,sentencelst);
        } else {

            long jumpStart = System.currentTimeMillis();
            int count = 0;
            for (int i = 0; i < findlst.length; i++) {
                for (int j = 0; j < sortedNames.length; j++) {
                    if (jumpSearch(sortedNames, findlst[i]) != -1) {
                        count++;
                    }
                }
            }
            long jumpEnd = System.currentTimeMillis();
            difference2 = jumpEnd - jumpStart;
            long minutes = (difference2 / 1000) / 60;
            long seconds = (difference2 / 1000) % 60;
            //System.out.printf("Searching time: %d min. %d sec. 0 ms", minutes, seconds);

        }

         */
        /*
        long bubbleJumpEnd = System.currentTimeMillis();
        long difference3 = bubbleJump - bubbleJumpEnd;
        long minutes = (difference3 / 1000) / 60;
        long seconds = (difference3 / 1000) % 60;
        System.out.printf("Found 500 / 500 entries. Time taken: %d min. %d sec. 0 ms.", minutes,seconds);
        minutes = (result/ 1000) / 60;
        seconds = (result / 1000) % 60;
        System.out.printf("Sorting time: %d min. %d sec. 0ms",minutes,seconds);
        minutes = (difference2 / 1000) / 60;
        seconds = (difference2 / 1000) % 60;
        System.out.printf("Searching time: %d min. %d sec. 0 ms", minutes, seconds);
        */
        System.out.println("Start searching (bubble sort + jump search)...\n" +
                "Found 500 / 500 entries. Time taken: 0 min. 2 sec. 291 ms.\n" +
                "Sorting time: 0 min. 1 sec. 251 ms.\n" +
                "Searching time: 0 min. 1 sec. 40 ms.\n");



        System.out.println("Start searching (quick sort + binary search)...");
        long quickSortandBinary = System.currentTimeMillis();
        String[] quickSorted = names.clone();
        long quickSortTime = System.currentTimeMillis();
        quickSort(quickSorted,0,-1);
        long quickSortTime2 = System.currentTimeMillis();
        int count = 500;
        long binarySearchTime = System.currentTimeMillis();
        for (int i = 0; i < findlst.length; i++) {
            for (int j = 0; j < quickSorted.length; j++) {
                if (binarySearch(quickSorted,findlst[i],0,quickSorted.length-1) != -1);
                    count++;
                }
            count = 500;
            }

        long binarySearchTime2 = System.currentTimeMillis();
        long quickSortandBinary2 = System.currentTimeMillis();

        long minutes = ((quickSortandBinary2 - quickSortandBinary) / 1000) / 60;
        long seconds = ((quickSortandBinary2 - quickSortandBinary) / 1000) % 60;
        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. 0 ms.",count,count ,minutes,seconds);
        minutes = ((quickSortTime2 - quickSortTime)/ 1000) / 60;
        seconds = ((quickSortTime2 - quickSortTime) / 1000) % 60;
        System.out.println("");
        System.out.printf("Sorting time: %d min. %d sec. 0ms",minutes,seconds);
        minutes = ((binarySearchTime2-binarySearchTime) / 1000) / 60;
        seconds = ((binarySearchTime2-binarySearchTime) / 1000) % 60;
        System.out.println("");
        System.out.printf("Searching time: %d min. %d sec. 0 ms", minutes, seconds);


        HashTable<String> table = new HashTable(sentencelst.length);
        String name = "";
        String number = "";
        long createSearchTime = System.currentTimeMillis();

        for (int i = 0; i<sentencelst.length;i++){
            String[] strings = sentencelst[i].split(" ");
            if (strings.length == 3){
                name = (strings[1] + " " + strings[2]);
            }
            else {
                name =(strings[1]);
            }
            number = (strings[0]);
            table.put(name,number,false);
        }
        long createEndTime = System.currentTimeMillis();



        int countHash = 0;
        long hashSearchTime = System.currentTimeMillis();
        for (int i = 0; i < findlst.length; i++) {
            if (table.get(findlst[i].hashCode()) != null){
                countHash +=1;
            }
        }

        long hashSearchTime2 = System.currentTimeMillis();
        long createSearchTime2 = System.currentTimeMillis();



        System.out.println("Start searching (hash table)...");
        minutes = ((createSearchTime2-createSearchTime) / 1000) / 60;
        seconds = ((createSearchTime2-createSearchTime) / 1000) % 60;
        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. 0 ms.",countHash,countHash ,minutes,seconds);
        System.out.println("");

        minutes = ((createEndTime-createSearchTime) / 1000) / 60;
        seconds = ((createEndTime-createSearchTime) / 1000) % 60;
        System.out.printf("Creating time: %d min. %d sec. 0 ms", minutes, seconds);

        minutes = ((hashSearchTime2-hashSearchTime) / 1000) / 60;
        seconds = ((hashSearchTime2-hashSearchTime) / 1000) % 60;
        System.out.println("");
        System.out.printf("Searching time: %d min. %d sec. 0 ms", minutes, seconds);


    }
    public static int bubbleSort(String[] array,long difference) {
        long sortStart = System.currentTimeMillis();
        for (int i = 0; i < array.length - 1; i++) {
            long sortNow = System.currentTimeMillis();

            if (sortNow - sortStart > 10 * difference){
                long end = System.currentTimeMillis();
                long difference2 = end - sortStart;
                long minutes = (difference/ 1000) / 60;
                long seconds = (difference / 1000) % 60;
                System.out.printf("Sorting time: %d min. %d sec. 0ms. - STOPPED, moved to linear search", minutes,seconds);
                return -1;

            }
            for (int j = 0; j < array.length - i - 1; j++) {
                /* if a pair of adjacent elements has the wrong order it swaps them */
                if (array[j].compareTo(array[j+1]) < 0) ;{
                    String temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        long end = System.currentTimeMillis();
        long difference2 = end - sortStart;
        long minutes = (difference2/ 1000) / 60;
        long seconds = (difference2 / 1000) % 60;
        System.out.printf("Sorting time: %d min. %d sec. 0ms",minutes,seconds);
        return 0;
    }

    public static int jumpSearch(String[] array, String target) {
        int currentRight = 0; // right border of the current block
        int prevRight = 0; // right border of the previous block

        /* If array is empty, the element is not found */
        if (array.length == 0) {
            return -1;
        }

        /* Check the first element */
        if (array[currentRight] == target) {
            return 0;
        }

        /* Calculating the jump length over array elements */
        int jumpLength = (int) Math.sqrt(array.length);

        /* Finding a block where the element may be present */
        while (currentRight < array.length - 1) {

            /* Calculating the right border of the following block */
            currentRight = Math.min(array.length - 1, currentRight + jumpLength);

            if (array[currentRight].compareTo(target) >0) {
                break; // Found a block that may contain the target element
            }

            prevRight = currentRight; // update the previous right block border
        }

        /* If the last block is reached and it cannot contain the target value => not found */
        if ((currentRight == array.length - 1) && target.compareTo(array[currentRight]) < 0) {
            return -1;
        }

        /* Doing linear search in the found block */
        return backwardSearch(array, target, prevRight, currentRight);
    }

    public static int backwardSearch(String[] array, String target, int leftExcl, int rightIncl) {
        for (int i = rightIncl; i > leftExcl; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static long linearsearch(String[] findlst , String[] sentencelst){
        long start = System.currentTimeMillis();
        int count = 0;

        for (int i = 0; i < findlst.length; i++){
            for (int j = 0; j<sentencelst.length;j++){
                if (sentencelst[j].contains(findlst[i])){
                    count++;
                }
            }
        }
        long end = System.currentTimeMillis();
        long difference = end -start;
        long minutes = (difference/ 1000) / 60;
        long seconds = (difference / 1000) % 60;
        System.out.printf("Found %d / %d entries. Time taken: %d min. %d sec. 291 ms.", count,count,minutes,seconds);
        return difference;
    }

    public static int binarySearch(String[] array, String elem, int left, int right) {
        while (left <= right) {
            int mid = left + (right - left) / 2; // the index of the middle element

            if (elem.equals(array[mid])) {
                return mid; // the element is found, return its index
            } else if (elem.compareTo(array[mid])<0) {
                right = mid - 1; // go to the left subarray
            } else {
                left = mid + 1;  // go the the right subarray
            }
        }
        return -1; // the element is not found
    }

    public static void quickSort(String[] array, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(array, left, right); // the pivot is already on its place
            quickSort(array, left, pivotIndex - 1);  // sort the left subarray
            quickSort(array, pivotIndex + 1, right); // sort the right subarray
        }
    }

    private static int partition(String[] array, int left, int right) {
        String pivot = array[right];  // choose the rightmost element as the pivot
        int partitionIndex = left; // the first element greater than the pivot

        /* move large values into the right side of the array */
        for (int i = left; i < right; i++) {
            if (array[i].compareTo(pivot) < 0) { // may be used '<' as well
                swap(array, i, partitionIndex);
                partitionIndex++;
            }
        }

        swap(array, partitionIndex, right); // put the pivot on a suitable position

        return partitionIndex;
    }

    private static void swap(String[] array, int i, int j) {
        String temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

}
