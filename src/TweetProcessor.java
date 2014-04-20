package tweetextraction;

public class TweetProcessor 
{
    /**
     * Returns the processed tweet
     * @param tweet
     * @return 
     */
    public String getCleanTweet(String tweet)
    {
         tweet=tweet.toLowerCase();
         tweet=tweet.replaceAll("#", "");
         tweet=tweet.replaceAll("!", "");
         tweet=tweet.replaceAll(",", "");
         tweet=tweet.replaceAll(":", "");
         String temp[]=tweet.split(" ");
         tweet="";
         label :
         for(int i=0;i<temp.length;i++)
         {
             if(temp[i].startsWith("@")||temp[i].startsWith("http"))
             {
                 continue label;
             }
             else
             {
                 tweet+=temp[i]+" ";
             }
         }
         temp=tweet.split(" ");
         String PathToAntonym="G:\\Semantic Orientation\\Antonym.txt";
         NotRemoval nr=new NotRemoval(PathToAntonym);
         tweet="";
         loop :
         for(int i=0;i<temp.length;i++)
         {
             if(temp[i].equals("not")&&(i+1)!=temp.length)
             {
                 if(nr.getAntonym(temp[i+1])!=null)
                 {
                     tweet+=nr.getAntonym(temp[i+1])+" ";
                     i+=2;
                     continue loop;
                 }
                 else
                 {
                     if(i+2!=temp.length&&nr.getAntonym(temp[i+2])!=null)
                     {
                         tweet+=temp[i+1]+" ";
                         tweet+=nr.getAntonym(temp[i+2])+" ";
                         i+=3;
                     }
                     else
                     {
                         tweet+=temp[i]+" ";
                     }
                 }
             }
             else
             {
                tweet+=temp[i]+" ";   
             }
         }
         return tweet;
    }
}
