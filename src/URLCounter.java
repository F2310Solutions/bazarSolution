import java.io.*;
import java.util.*;

class URLCounter implements Runnable {

    // number of repetitions in the interval
    private final static int NUMBER_FOR_REPETITIONS = 3;

    // time interval for repetitions in millis
    private final static int TIME_FOR_REPETITION= 600000 ;

    // urls waiting for update with their times of reading
    private static Map<String , List<Date>> urlsWaitingForUpdate = new HashMap<>();

    @Override
    public void run(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                while (URLGenerator.getUrls().size()>0){
                    try {
                        count(URLGenerator.getUrls().poll());
                    }
                    catch (Exception e){
                        System.out.println("Exception here occurred ... ");
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 1000);
    }

    private void count(String url) throws Exception {

        Date timeOfReadUrl = new Date();

        if (!urlsWaitingForUpdate.containsKey(url)){

            // add first time of reading this url to the list of its times
            List <Date> times = new LinkedList<>();
            times.add(timeOfReadUrl);
            urlsWaitingForUpdate.put(url,times);

            // after "TIME_FOR_REPETITION" time, data for this url must be delete
            Date dateForRemove = new Date(timeOfReadUrl.getTime()+TIME_FOR_REPETITION);
            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                        urlsWaitingForUpdate.remove(url);
                }
            }, dateForRemove);

        }

        // if the url was already read and the desired number has not yet been reached
        else if (urlsWaitingForUpdate.get(url).size()<NUMBER_FOR_REPETITIONS)
          {
              // add the last time of read this url
              List <Date> times = urlsWaitingForUpdate.get(url);
              times.add(timeOfReadUrl);
              urlsWaitingForUpdate.put(url,times);

              // save url if it read for "NUMBER_FOR_REPETITIONS" times in an specific interval
              if (urlsWaitingForUpdate.get(url).size()==NUMBER_FOR_REPETITIONS){
                  save(url,timeOfReadUrl);
              }
          }
    }

    private void save(String url,Date date){
        System.out.println("try to saving . . .");
        writeToFile(url,date);
    }

    private void writeToFile(String url,Date date){
        try {
            BufferedWriter myWriter = new BufferedWriter(new FileWriter("results.txt", true));
            myWriter.write(url +" , "+date.toString());
            myWriter.newLine();
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            System.out.println(url + " , "+date+"\n\n");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
