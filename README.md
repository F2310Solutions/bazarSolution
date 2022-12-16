# Reading URLs, counting and saving them if they repeated specific times in an interval
## Code function

This code save the input URLs which repeated **“NUMBER_FOR_REPETITIONS”** time in a **“TIME_FOR_REPETITION_TIME”** time.

Just set **“NUMBER_FOR_REPETITIONS”** and **“TIME_FOR_REPETITION_TIME”** for your target.
```
    // number of repetitions in the interval
    private final static int NUMBER_FOR_REPETITIONS = 3;

    // time interval for repetitions in millis
    private final static int TIME_FOR_REPETITION = 600000 ;
```

## About each service 

**First we read links from the file.**
```
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
```
  
  
  
**Then we count it in an interval.**
```
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
```



**Whenever the number of repeating becomes **"NUMBER_FOR_REPETITIONS"**, we save it with writing the URL and last time repeated in a **"results.txt"** file.**
```
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
```
    
    
    
Eventually this java source code offers us two services :
>	First one is the class named **“URLGenerator”** that read random lines from the **“links.txt”** file per second and save them to the queue that named “randomUrls”. For example in this code number of random URLs per second can be from 0 to 200.

>	Second one is the class named **“URLCounter”** that get random URLs from **"URLGenerator"** queue and save each URL with its time of read in a map named **“urlsWaitingForUpdate”**. Actually **“urlsWaitingForUpdate”** is a map with URL key and the value of each key is the list of times that URL read until now. The size of this list show the number that this URL repeated until now. If the size reaches to **“NUMBER_FOR_REPETITIONS”** we save it with its time.
After **“TIME_FOR_REPETITION_TIME”** time from first time the URL read, map removes the object of this URL and Resumes counting...

## Hints

* Both classes are executed simultaneously by Multithread.

* Reading from "links.txt" file and writing to "results.txt" are repeated every second.

* If no URL need to save just remove it after the interval.

* We save URL immediately after the NUMBER_FOR_REPETITIONS" time and if each URL repeat more than the specified value, we ignore them until the next time period arrives.


That’s it!

Thank you.
