
//
// Note: You are allowed to add additional methods if you need.
// Coded by Prudence Wong 2020-12-15
//
// Name:HenryBridges
// Student ID:201495180
//
// Time Complexity and explanation: You can use the following variables for easier reference.
// n denotes the number of requests, p denotes the size of the cache
// n and p can be different and there is no assumption which one is larger
//
// noEvict():
//
// evictFIFO():
//
// evictLFU():
//
// evictLFD():
//


class COMP108A1Paging {
    public static int head;
    public static int tail;
    public static int qSize;

    // no eviction
    // Aim:
    // do not evict any page
    // count number of hit and number of miss, and find the hit-miss pattern; return an object COMP108A1Output
    // Input:
    // cArray is an array containing the cache with cSize entries
    // rArray is an array containing the request sequence with rSize entries
    static COMP108A1Output noEvict(int[] cArray, int cSize, int[] rArray, int rSize) {
        COMP108A1Output output = new COMP108A1Output();
        boolean hit = false; // used as a flag to indicate if the element exists in the other array.
        for (int i = 0; i < rSize; i++) { // loops through each index of rArray.
            hit = false; // reset the flag to false after each run through or else will always stay true after its true once.
            for (int j = 0; j < cSize; j++) { // loops through each index of cArray.
                if (rArray[i] == cArray[j]) {
                    hit = true;
                    break; // break here as don't need to check it exists more than once.
                }
            }
            if (hit) {
                output.hitPattern += "h";// if it exists than it is a hit.
                output.hitCount += 1;
            } else {
                output.hitPattern += "m"; // if not then it is a miss.
                output.missCount += 1;
            }
        }
        return output;
    }

    // evict FIFO
    // Aim:
    // evict the number present in cache for longest time if next request is not in cache
    // count number of hit and number of miss, and find the hit-miss pattern; return an object COMP108A1Output
    // Input:
    // cArray is an array containing the cache with cSize entries
    // rArray is an array containing the requested sequence with rSize entries
    static COMP108A1Output evictFIFO(int[] cArray, int cSize, int[] rArray, int rSize) {
        COMP108A1Output output = new COMP108A1Output();
        head = 0;
        qSize = cSize - 1;
        tail = (qSize + 1);
        boolean hit = false; // used as a flag to indicate if the element exists in the other array.
        for (int i = 0; i < rSize; i++) { // loops through each index of rArray.
            hit = false; // reset the flag to false after each run through or else will always stay true after its true once.
            for (int j = 0; j < cSize; j++) { // loops through each index of cArray.
                if (rArray[i] == cArray[j]) {
                    hit = true;
                    break; // break here as don't need to check it exists more than once.
                }
            }
            if (hit) { // Nothing needs to happen as it is already in cache.
                output.hitPattern += "h";
                output.hitCount += 1;
            } else { // dequeue (FIFO) and insert the value.
                output.hitPattern += "m";
                output.missCount += 1;
                // Dequeue
                cArray = dequeue(cArray);
                // Enqueue
                cArray = enqueue(rArray[i], cArray);
            }
        }
        return output;
    }

    // evict LFU
    // Aim:
    // evict the number that is least frequently used so far if next request is not in cache
    // count number of hit and number of miss, and find the hit-miss pattern; return an object COMP108A1Output
    // Input:
    // cArray is an array containing the cache with cSize entries
    // rArray is an array containing the request sequence with rSize entries
    static COMP108A1Output evictLFU(int[] cArray, int cSize, int[] rArray, int rSize) {
        COMP108A1Output output = new COMP108A1Output();
        int[] frequency = new int[cSize];
        for (int n : frequency) {
            n = 1;
        }

        boolean hit = false; // used as a flag to indicate if the element exists in the other array.
        for (int i = 0; i < rSize; i++) { // loops through each index of rArray.
            hit = false; // reset the flag to false after each run through or else will always stay true after its true once.
            for (int j = 0; j < cSize; j++) { // loops through each index of cArray.
                if (rArray[i] == cArray[j]) {
                    hit = true;
                    frequency[j] += 1;
                    break; // break here as don't need to check it exists more than once.
                }
            }

            if (hit) {
                output.hitPattern += "h";// if it exists than it is a hit.
                output.hitCount += 1;
            } else {
                output.hitPattern += "m"; // if not then it is a miss.
                output.missCount += 1;
                int place = minIndex(frequency);
                cArray[place] = rArray[i];
            }
        }
        return output;
    }

    // evict LFD
    // Aim:
    // evict the number whose next request is the latest
    // count number of hit and number of miss, and find the hit-miss pattern; return an object COMP108A1Output
    // Input:
    // cArray is an array containing the cache with cSize entries
    // rArray is an array containing the request sequence with rSize entries
    static COMP108A1Output evictLFD(int[] cArray, int cSize, int[] rArray, int rSize) {
        COMP108A1Output output = new COMP108A1Output();
        int rIndex = 0; // counter for rIndex to be passed to lfd to know where we are in this method's loop in there.
        boolean hit = false; // used as a flag to indicate if the element exists in the other array.
        for (int i = 0; i < rSize; i++) { // loops through each index of rArray.
            hit = false; // reset the flag to false after each run through or else will always stay true after its true once.
            for (int j = 0; j < cSize; j++) { // loops through each index of cArray.
                if (rArray[i] == cArray[j]) {
                    hit = true;
                    break; // break here as don't need to check it exists more than once.
                }
            }
            if (hit) {
                rIndex++;
                output.hitPattern += "h";// if it exists than it is a hit.
                output.hitCount += 1;
            } else {
                int[] lfd = LongestForwardDistance(rIndex, rArray, cArray);
                int indexToEvictFrom = getMaxIndex(lfd);
                cArray[indexToEvictFrom] = rArray[i];
                output.hitPattern += "m"; // if not then it is a miss.
                output.missCount += 1;
            }
        }

        return output;
    }

    // Implement a queue with a circular array system. Enqueue adds a value to old tail position.
    // Increments the tail position with the circular system.
    static int[] enqueue(int value, int[] array) {
        if (array.length == tail) {
            System.out.println("Queue is Full");
        } else {
            array[tail] = value; // sets the empty tail space to value given.
            tail = (tail + 1) % qSize; // moves the tail along one to next space (wraps around if needed, hence %).
        }
        return array;
    }

    // Dequeue sets the front of the queue value to 0/null so it can be used and increments the head along by one
    // in the circular queue system.
    static int[] dequeue(int[] array) {
        if (tail == head) {
            System.out.println("Queue is empty");
        } else {
            array[head] = 0; // gets rid of the value that was in there.
            tail = head; // sets the tail to the empty value as that is now the next free space.
            head = (head + 1) % qSize; // head is moved along one (wraps around if needed, hence %).
        }
        return array;
    }

    // Method for getting the place of the minimum value in an array.
    public static int minIndex(int[] array) {
        int minValue = array[0];
        int minValuePlace = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] < minValue) {
                minValue = array[i];
                minValuePlace = i;
            }
        }
        return minValuePlace;
    }

    // Method to build an array of the furthest distance of each element in cArray, in rArray.
    static int[] LongestForwardDistance(int rIndexOutside, int[] request, int[] cache) {
        int rIndexInside = 1; // always 1 ahead of where we start from outside loop going through request.
        int[] lfd = new int[cache.length];
        for (int i = 0; i < cache.length; i++) {
            for (int j = rIndexOutside; j < request.length; j++) { // enters loop from where we are in evictLFD method.
                if (cache[i] == request[j]) {
                    break;
                } else {
                    rIndexInside++; // if they aren't equal we go along another one (maintains 1 ahead of rIndexOutside).
                }
            }
            lfd[i] = rIndexOutside + rIndexInside; // the longest forward distance is rIndexInside ahead of where we began outside.
            rIndexInside = 1; // reset the index to go back through for the next cache element.
        }
        for (int i = 0; i < lfd.length; i++) { // loops through the longest forward distance.

            if (lfd[i] > request.length) { // have to check that the value was actually in there.
                // If the LFD[i] value is greater than requests this is because it went through every element
                // and was never satisfied and then when we add 1 ahead to it it becomes greater than requests length.

                lfd[i] = Integer.MAX_VALUE; // if it wasn't, then we set this as 'infinite' as seen in lecture 9.
                // so that this index is chosen everytime as the one to evict as it is not in requests again.
            }

        }
        return lfd;
    }

    // Loops through an array and returns the index at which the value of the element is the greatest.
    static int getMaxIndex(int[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }

        }
        return maxIndex;
    }
}
