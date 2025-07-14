1. Difference between ArrayList and LinkedList?
The main difference is the underlying structure of these collections. ArrayList is based on array object while LinkedList is based on Node object.
Difference in time complexity is also visible. ArrayList has o(1) for accessing elements, while LinkedList has 
advantages in adding to the beginning and end of the data structure(and also removing and reading). ArrayList has cons in the fact that we have to manage array when adding and removing
elements
2. How HashMap works?
HashMap underlying data structure is a array that holds objects inside. So each array position contains a bucket with object and inside there is a LinkedList with each inserted node.
Calculation of the key hashcode happens when we insert inside DS and position is calculated keeping in mind size of the HashTable. Then each time we write/get/remove we calculate
index to know exact position of the bucket. This gives us an opportunity to access object with o(1) is there is no collision in the bucket.
3. What is Set?
Set is a collection that holds only unique elements based on equals and hashcode method. It does not guarantees that elements will keep order during insertion
4. What is Queue, Deque, TreeMap, LinkedHashMap?
Queue - FIFO DS. 
Deque - collection that supports removal and insertion at boh ends
TreeMap - is a type of map where keys are ordered in a black-red tree manner(natural ordering)
LinkedHashMap - is a type of map where underlying DS is not an array of Object but a LinkedList so it guarantees keeping insertion order