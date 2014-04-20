package tweetextraction;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author SHUBH
 */
public class SWN3 {
	private Map<String, Double> dictionary;
        /**
         * 
         * @param pathToSWN
         * @throws IOException 
         */
	public SWN3(String pathToSWN) throws IOException {
		// This is our main dictionary representation
		dictionary = new HashMap<String, Double>();

		// From String to list of doubles.
		HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

		BufferedReader csv = null;
		try {
			csv = new BufferedReader(new FileReader(pathToSWN));
			int lineNumber = 0;
			String line;
			while ((line = csv.readLine()) != null) {
				lineNumber++;

				// If it's a comment, skip this line.
				if (!line.trim().startsWith("#")) {
					// We use tab separation
					String[] data = line.split("\t");
					String wordTypeMarker = data[0];

					// Example line:
					// POS ID PosS NegS SynsetTerm#sensenumber Desc
					// a 00009618 0.5 0.25 spartan#4 austere#3 ascetical#2
					// ascetic#2 practicing great self-denial;...etc

					// Is it a valid line? Otherwise, through exception.
					if (data.length != 6) {
						throw new IllegalArgumentException(
								"Incorrect tabulation format in file, line: "
										+ lineNumber);
					}

					// Calculate synset score as score = PosS - NegS
					Double synsetScore = Double.parseDouble(data[2])
							- Double.parseDouble(data[3]);

					// Get all Synset terms
					String[] synTermsSplit = data[4].split(" ");

					// Go through all terms of current synset.
					for (String synTermSplit : synTermsSplit) {
						// Get synterm and synterm rank
						String[] synTermAndRank = synTermSplit.split("#");
						String synTerm = synTermAndRank[0] + "#"
								+ wordTypeMarker;

						int synTermRank = Integer.parseInt(synTermAndRank[1]);
						// What we get here is a map of the type:
						// term -> {score of synset#1, score of synset#2...}

						// Add map to term if it doesn't have one
						if (!tempDictionary.containsKey(synTerm)) {
							tempDictionary.put(synTerm,
									new HashMap<Integer, Double>());
						}

						// Add synset link to synterm
						tempDictionary.get(synTerm).put(synTermRank,
								synsetScore);
					}
				}
			}

			// Go through all the terms.
			for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary.entrySet())
			{
				String word = entry.getKey();
				Map<Integer, Double> synSetScoreMap = entry.getValue();

				// Calculate weighted average. Weigh the synsets according to
				// their rank.
				// Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
				// Sum = 1/1 + 1/2 + 1/3 ...
				double score = 0.0;
				double sum = 0.0;
				for (Map.Entry<Integer, Double> setScore : synSetScoreMap
						.entrySet()) {
					score += setScore.getValue() / (double) setScore.getKey();
					sum += 1.0 / (double) setScore.getKey();
				}
				score /= sum;

				dictionary.put(word, score);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (csv != null) {
				csv.close();
			}
		}
	}
        /**
         * Returns the score of the sentinel word.
         * @param word
         * @param pos
         * @return 
         */
	public double extract(String word, String pos)
	{
		Double val=dictionary.get(word + "#" + pos);
                if(val!=null)
                {
                    return val;
                }
                else
                {
                    return 0.0;
                }
	}

	/*public static void main(String [] args) throws IOException
	{
		if(args.length<1)
		{
			System.err.println("Usage: java SentiWordNetDemoCode <pathToSentiWordNetFile>");
			return;
		}
		String pathToSWN = args[0];
		SWN3 sentiwordnet = new SWN3(pathToSWN);

		System.out.println("good#a "+sentiwordnet.extract("good", "a"));
		System.out.println("bad#a "+sentiwordnet.extract("bad", "a"));
		System.out.println("blue#a "+sentiwordnet.extract("blue", "a"));
		System.out.println("blue#n "+sentiwordnet.extract("blue", "n"));
	} */
}
/*
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
public class SWN3 
{
    public  String pathToSWN = "G:\\Semantic Orientation\\SentiWordNet_3.0.0\\home\\swn\\www\\admin\\dump\\SentiWordNet_3.0.0_20130122.txt";
    private HashMap<String, String> _dict;         
    public Double extract(String testword,String type){
    double _score = 0.0;
    int flag=0;
    HashMap<String, Vector<Double>> _temp = new HashMap<String, Vector<Double>>();
    try
    {
    BufferedReader csv = new BufferedReader(new FileReader(pathToSWN));
    String line = "";
    while((line = csv.readLine()) != null)
    {
    String[] data = line.split("\t");
    Double score = Double.parseDouble(data[2])-Double.parseDouble(data[3]);
    String[] words = data[4].split(" ");
    for(String w:words)
    {
    String[] w_n = w.split("#");
    if(w_n[0].equals(testword) && data[0].equals(type))
    {
    w_n[0] += "#"+data[0];
    int index = Integer.parseInt(w_n[1])-1;
    if(_temp.containsKey(w_n[0]))
    {
        Vector<Double> v = _temp.get(w_n[0]);
        if(index>v.size())
        for(int i = v.size();i<index; i++)
        v.add(0.0);
        v.add(index, score);
        _temp.put(w_n[0], v);
    }
    else
    {
        Vector<Double> v = new Vector<Double>();
        for(int i = 0;i<index; i++)
        v.add(0.0);
        v.add(index, score);
        _temp.put(w_n[0], v);
     }
    flag=1;
    break;
}
}
if(flag==1)
break;
}
Set<String> temp = _temp.keySet();
for (Iterator<String> iterator = temp.iterator(); iterator.hasNext();)
{
String word = (String) iterator.next();
Vector<Double> v = _temp.get(word);
_score = 0.0;
double sum = 0.0;
for(int i = 0; i < v.size(); i++)
_score += ((double)1/(double)(i+1))*v.get(i);
for(int i = 1; i<=v.size(); i++)
sum += (double)1/(double)i;
_score /= sum;
}
}
catch(Exception e)
{
}
return _score;
}
}
*/



/*
package tweetextraction;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SWN3 
{
    private Map<String, Double> dictionary;

	public SWN3(String pathToSWN) throws IOException 
        {
		// This is our main dictionary representation
		dictionary = new HashMap<String, Double>();

		// From String to list of doubles.
		HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

		BufferedReader csv = null;
		try 
                {
			csv = new BufferedReader(new FileReader(pathToSWN));
			int lineNumber = 0;
			String line;
			while ((line = csv.readLine()) != null) 
                        {
				lineNumber++;

				// If it's a comment, skip this line.
				if (!line.trim().startsWith("#")) 
                                {
					// We use tab separation
					String[] data = line.split("\t");
					String wordTypeMarker = data[0];

					// Example line:
					// POS ID PosS NegS SynsetTerm#sensenumber Desc
					// a 00009618 0.5 0.25 spartan#4 austere#3 ascetical#2
					// ascetic#2 practicing great self-denial;...etc

					// Is it a valid line? Otherwise, through exception.
					if (data.length != 6) 
                                        {
						throw new IllegalArgumentException(
								"Incorrect tabulation format in file, line: "
										+ lineNumber);
					}

					// Calculate synset score as score = PosS - NegS
					Double synsetScore = Double.parseDouble(data[2])
							- Double.parseDouble(data[3]);

					// Get all Synset terms
					String[] synTermsSplit = data[4].split(" ");

					// Go through all terms of current synset.
					for (String synTermSplit : synTermsSplit) 
                                        {
						// Get synterm and synterm rank
						String[] synTermAndRank = synTermSplit.split("#");
						String synTerm = synTermAndRank[0] + "#"
								+ wordTypeMarker;

						int synTermRank = Integer.parseInt(synTermAndRank[1]);
						// What we get here is a map of the type:
						// term -> {score of synset#1, score of synset#2...}

						// Add map to term if it doesn't have one
						if (!tempDictionary.containsKey(synTerm)) 
                                                {
							tempDictionary.put(synTerm,
									new HashMap<Integer, Double>());
						}

						// Add synset link to synterm
						tempDictionary.get(synTerm).put(synTermRank,
								synsetScore);
					}
				}
			}

			// Go through all the terms.
			for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary.entrySet())
			{
				String word = entry.getKey();
				Map<Integer, Double> synSetScoreMap = entry.getValue();

				// Calculate weighted average. Weigh the synsets according to
				// their rank.
				// Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
				// Sum = 1/1 + 1/2 + 1/3 ...
				double score = 0.0;
				double sum = 0.0;
				for (Map.Entry<Integer, Double> setScore : synSetScoreMap.entrySet()) 
                                {
					score += setScore.getValue() / (double) setScore.getKey();
					sum += 1.0 / (double) setScore.getKey();
				}
				score /= sum;

				dictionary.put(word, score);
			}
		} 
                catch (Exception e) 
                {        System.out.println("hi");
			e.printStackTrace();
		} 
                finally 
                {
			if (csv != null) 
                        {
				csv.close();
			}
		}
	}

	public double extract(String word, String pos) 
	{
		return dictionary.get(word + "#" + pos);
	}
}

*/