class Main {
    public static void main(String[] args) {

        URLGenerator generator = new URLGenerator();
        URLCounter counter = new URLCounter();

        Thread t1 = new Thread(generator,"GENERATOR");
        Thread t2 = new Thread(counter,"COUNTER");

        t1.start();
        t2.start();
        t1.setPriority(8);

    }
}
