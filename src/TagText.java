
package tweetextraction;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.IOException;
/**
 * 
 * @author SHUBH
 */
public class TagText 
{
    /**
     * Returns the tagged String
     * @param str
     * @param tagger
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static String stringTagger(String str,MaxentTagger tagger) throws IOException,ClassNotFoundException
    {
        return tagger.tagString(str);
    }
}
