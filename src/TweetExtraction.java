package tweetextraction;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class TweetExtraction 
{

    private final static String CONSUMER_KEY = "QQfJdaaQxE2GduoxuPT4yCu19";

    private final static String CONSUMER_KEY_SECRET = "tOmjZ5WKeqZ3uzt6pRMsiZ486FqUOhPAWxRkfC3c2T6HoBnHo0";

    private final static String AccessToken = "2419344601-yEkWROjGUOizegyW7Y8uoThwT91J9V7bH6SyzmP";

   private final static String AccessTokenSecret = "S17y93PtU9EGrkeSyPowzVMPkQ35rnrbijRFy1XNSwk98";
   
   static Twitter twitter;
   
   private static TweetProcessor tp=new TweetProcessor();
   
   /**
    * @throws TwitterException
    * @throws IOException 
    */
    public void start() throws TwitterException, IOException 
    {
         twitter = new TwitterFactory().getInstance();

        twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);


        // Authorisation Process Not to be Deleted
        /*RequestToken requestToken = twitter.getOAuthRequestToken();

        System.out.println("Authorization URL: \n"+ requestToken.getAuthorizationURL());

        AccessToken accessToken = null;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (null == accessToken) 
        {

            try 
            {

                  System.out.print("Input PIN here: ");

                  String pin = br.readLine();
                  
                  accessToken = twitter.getOAuthAccessToken(requestToken, pin);

            } 
            catch (TwitterException te) 
            {

                  System.out.println("Failed to get access token, caused by: "+ te.getMessage());

                  System.out.println("Retry input PIN");

             }

        }  
        System.out.println("Access Token: " + accessToken.getToken());

        System.out.println("Access Token Secret: "+ accessToken.getTokenSecret());  */

        AccessToken oathAccessToken = new AccessToken(AccessToken,AccessTokenSecret);

        twitter.setOAuthAccessToken(oathAccessToken);
        
        //twitter.updateStatus("hi.. im updating this using Namex Tweet for Demo");

        //ResponseList list = twitter.getHomeTimeline();
        
    }

    /**
    * 
    * @param args
    * @throws Exception 
    */
    public static void main(String[] args) throws Exception 
    {

        new TweetExtraction().start();// run the Twitter client
        MaxentTagger tagger=new MaxentTagger("G:\\Semantic Orientation\\stanford-postagger-2014-01-04\\stanford-postagger-2014-01-04\\models\\english-left3words-distsim.tagger");
        Polarity pol=new Polarity();
        int cnt=0;
        int cnt_neg=0,cnt_pos=0,cnt_neu=0;
        Scanner kb=new Scanner(System.in);
        System.out.println("Enter the keyword");
        String keyword=kb.next();
        Query query = new Query(keyword)
                      .count(100)
                       //.since("yesterday")
                       .lang("en");
        QueryResult result;
        end :
        do 
        {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) 
                {
                    //System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
                    //System.out.println(tweet.getText());
                    String original="";
                    String S1=tweet.getText();
                    original=S1;
                    S1=tp.getCleanTweet(S1);
                    System.out.println(S1);
                    cnt++;
                    int resultscore=Polarity.getPolarity(S1,tagger);
                    if(resultscore<0)
                    {
                        System.out.println(original+" :"+ " "+resultscore);
                        cnt_neg++;
                    }
                    else if(resultscore>0)
                    {
                        System.out.println(original+" :"+ " "+resultscore);
                        cnt_pos++;
                    }
                    else
                    {
                        System.out.println(original+" :"+ " "+resultscore);
                        cnt_neu++;
                    }
                    //System.out.println(generalised);
                    if(cnt_neg+cnt_pos+cnt_neu>=100)
                    {
                        break end;
                    }
                }
        } while ((query = result.nextQuery()) != null);
        
        System.out.println("Total tweets related to the product "+(cnt_pos+cnt_neg+cnt_neu));
        System.out.println("positive tweets "+cnt_pos);
        System.out.println("negative tweets "+cnt_neg);
        System.out.println("neutral tweets "+cnt_neu);
       /* List <Status> list=twitter.getHomeTimeline();
        for (Status each : list) 
        {

           System.out.println("Sent by: @" + each.getUser().getScreenName()
                               + " - " + each.getUser().getName() + "\n" + each.getText());
        } */
    }

}
