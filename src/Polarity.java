
package tweetextraction;

import java.io.IOException;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
/**
 * 
 * @author SHUBH
 */
public class Polarity 
{
    /**
     * Returns the POS of the word uses Stanford POS-tagger
     * @param str
     * @return 
     */
    public static String sentiWordType(String str)
    {
        if(str.equals("JJ")|| str.equals("JJR")||str.equals("JJS"))
        return "a";
        if(str.equals("NN")||str.equals("NNS")||str.equals("NP")||str.equals("NPS"))
        return "n";
        if(str.equals("RB")||str.equals("RBR")||str.equals("RBS"))
        return "r";
        if(str.equals("VB")||str.equals("VBD")||str.equals("VBG")||str.equals("VBN")||str.equals("VBP")||str.equals("VBZ"))
        return "v";
        return "s";
    }
    /**
     * Returns the polarity of the tweet
     * @param tweet
     * @param tagger
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static int getPolarity(String tweet,MaxentTagger tagger) throws IOException,ClassNotFoundException
    {
        TagText tag=new TagText();
        int result=0;
        double resultscore=0;
        String tagged=tag.stringTagger(tweet,tagger);
        String data[]=tagged.split(" ");
        String ngrams="";
        for(String word:data)
        {
            String wt[]=word.split("_");
            ngrams+=wt[1]+" ";
        }
        //System.out.println(tagged);
        String PathToSWN="G:\\Semantic Orientation\\SentiWordNet_3.0.0\\home\\swn\\www\\admin\\dump\\SentiWordNet_3.0.0_20130122.txt";
        SWN3 s=new SWN3(PathToSWN);
         //System.out.println(data.length);
        for(String str:data)
        {
            String wt[]=str.split("_");
            String type=sentiWordType(wt[1]);
            if(wt[0]==null||type==null)
            {
                continue;
            }
            //System.out.println(wt[0]+"#"+type);
            if(!type.equals("s"))
            {
                if(type.equals("v")||type.equals("r"))
                {
                    double tempscore=s.extract(wt[0],type);
                    //double tempscore=s.extract(wt[0],type);
                    if(tempscore<s.extract(wt[0],"a"))
                    {
                            resultscore+=s.extract(wt[0],"a");
                            //System.out.println("Score of "+wt[0]+" is "+s.extract(wt[0],"a"));
                    }
                    else
                    {
                            resultscore += s.extract(wt[0], type);
                            //System.out.println("Score of "+wt[0]+" is "+s.extract(wt[0], type));
                    }
                }
                else
                {
                        resultscore += s.extract(wt[0], type);
                      //  System.out.println("Score of "+wt[0]+" is "+s.extract(wt[0], type));
                }
            }
        } 
          
        if(tweet.contains("not"))
        {
            resultscore*=-1;
        }
        System.out.println(resultscore);
        if(resultscore>0)
        {
            result=1;
        }
        else if(resultscore<0)
        {
            result=-1;
        }
        else
        {
            result=0;
        }
        return result;
    }
}
