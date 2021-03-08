
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
			if (hit == true) {
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
			if (hit == true) { // Nothing needs to happen as it is already in cache.
				output.hitPattern += "h";
				output.hitCount += 1;
			} else { // dequeue (FIFO) and insert the value.
				output.hitPattern += "m";
				output.missCount += 1;
				// Dequeue
				dequeue(cArray);
				// Enqueue
				enqueue(rArray[i], cArray);
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

			if (hit == true) {
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

		return output;
	}


	// Implement a queue with a circular array system. Enqueue adds a value to old tail position.
	// Increments the tail position with the circular system.
	static void enqueue(int value, int[] array) {
		if (array.length == tail) {
			System.out.println("Queue is Full");
		} else {
			array[tail] = value;
			tail = (tail + 1) % qSize;
		}
	}

	// Dequeue sets the front of the queue value to 0/null so it can be used and increments the head along by one
	// in the circular queue system.
	static void dequeue(int[] array) {
		if (tail == head) {
			System.out.println("Queue is empty");
		} else {
			array[head] = 0;
			head = (head + 1) % qSize;
		}
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

}

