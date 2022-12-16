import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class URLGenerator implements Runnable {

    // for random urls reading from file per second
    private static Queue<String> randomUrls=new LinkedList<>();

    // just for print in terminal
    private static Queue<String> results=new LinkedList<>();

    private  static final String fileName="links.txt";
   @Override
   public void run()
     {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                // file with 10000 urls
                readRandomLine(fileName);

            }
        } , 0 , 1000);

    }

    private static void readRandomLine(String path) {
        // all lines of urls from file
        List<String> lines;

        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        }

        Random random = new Random();
        // read 0 or 1 or .. to (for example) 200 random url from file
        int numOfRandomUrls=random.nextInt(200);

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
