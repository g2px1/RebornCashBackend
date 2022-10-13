class Queue {

    private static int front, rear, capacity;
    private static String[] queue;

    Queue(int size) {
        front = rear = 0;
        capacity = size;
        queue = new String[capacity];
    }

    static int size() {
        int count = 0;
        int i;
        for (i = front; i < rear; i++) {
                count++;
        }
        return count;
    }
    // insert an element into the queue  
    static void add(String item)  {
        // check if the queue is full  
        if (capacity == rear) {
            System.out.print("\nQueue is full\n");
        }
        // insert element at the rear   
        else {
            queue[rear] = item;
            rear++;
        }
    }
    //remove an element from the queue  
    static void remove()  {
        // check if queue is empty   
        if (front == rear) {
            System.out.print("\nQueue is empty\n");
        }
        // shift elements to the right by one place uptil rear   
        else {
            if (rear - 1 >= 0) System.arraycopy(queue, 1, queue, 0, rear - 1);
            // set queue[rear] to 0
            if (rear < capacity)
                queue[rear] = "";

            // decrement rear   
            rear--;
        }
    }

    // print queue elements   
    static void queueDisplay()
    {
        int i;
        if (front == rear) {
            System.out.print("Queue is Empty\n");
            return;
        }

        // traverse front to rear and print elements   
        for (i = front; i < rear; i++) {
            if (i == rear - 1) {
                System.out.printf("%s", queue[i]);
                return;
            }
            System.out.printf("%s, ", queue[i]);
        }
    }
    static String pop(){
        if (front == rear) {
            System.out.print("Queue is Empty\n");
            return "";
        }
        String out = queue[front];
        remove();

        return out;
    }

    // print front of queue   
    static void queueFront()
    {
        if (front == rear) {
            System.out.print("Queue is Empty\n");
            return;
        }
        System.out.printf("\nFront Element of the queue: %s", queue[front]);
    }
}   
   