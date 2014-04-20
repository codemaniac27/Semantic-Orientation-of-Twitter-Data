
package tweetextraction;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NotRemoval 
{
    private  Map<String, String> Antonym;
    /**
     * 
     * @param PathToAntonym 
     */
    public NotRemoval(String PathToAntonym)
    {
        Antonym=new HashMap<String,String>();
        BufferedReader csv=null;
        try 
        {
			csv = new BufferedReader(new FileReader(PathToAntonym));
			String line;
			while ((line = csv.readLine()) != null) 
                        {
				String data[]=line.split("\t");
                                Antonym.put(data[0], data[1]);
                                Antonym.put(data[1], data[0]);
                        }
        }
        catch(IOException e)
        {
            System.out.println("Exception in NotRemoval");
        }
    }
    /**
     * Returns the antonym of the word
     * @param s
     * @return 
     */
    public String getAntonym(String s)
    {
        return Antonym.get(s);
    }
}
