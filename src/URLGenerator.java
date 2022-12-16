import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class URLGenerator implements Runnable {

    // for random urls reading from file per second
    private static Queue<String> randomUrls=new LinkedList<>();

    // just for print in terminal
    private static Queue<String> results=new LinkedList<>();

    // file path we read from it
    private final String FILE_PATH="links.txt";

    // maximum number of reading url per second
    private static final int MAX_NUM_OF_READ_URL_PER_SECOND=200;

    @Override
   public void run()
     {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                // file with 10000 urls
                readRandomLine(FILE_PATH);

            }
        } , 0 , 1000);

    }

    private static void readRandomLine(String path) {
        // all available urls from file
        List<String> lines;

        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        }

        Random random = new Random();
        // read 0 or 1 or .. to (for example) 200 random url from file
        int numOfRandomUrls=random.nextInt(MAX_NUM_OF_READ_URL_PER_SECOND);

        for (int i=0;i<numOfRandomUrls;i++) {

            try {
                String randomLine = lines.get(random.nextInt(lines.size()));
                randomUrls.add(randomLine);
                results.add(randomLine);
            }
            catch (Exception e){
                System.out.println("Error occurred!! ");
                e.printStackTrace();
            }

        }
        // print urls and times reading from file
        System.out.println("random  urls ---> "+results);
        results.clear();
        System.out.println("time of reading ---> "+new Date()+"\n\n");

    }

    static Queue<String> getUrls(){
       return randomUrls;
    }

}
