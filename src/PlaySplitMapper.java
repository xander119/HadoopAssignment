import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

/**
 * Auther Zechen
 * Filter out the name of the characters
 */
public class PlaySplitMapper extends
		Mapper<LongWritable, Text, Text, IntWritable> {

	
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String line = value.toString();
		char[] stringInchar = line.toCharArray();

		String speaker = null;
		// get lines that are started with capital letters
		
		if (stringInchar.length > 0 && !Character.isWhitespace(stringInchar[0])) {
			// split words to array of words
			String[] words = line.split("\\W+");
			// check first two words are capital wards and not ACT or SCENE
			//Check if the the word is name
			if (words.length >= 1 && isName(words[0])) {
				speaker = words[0];
				
				//Check if the name has two words
				if (words.length >= 2 && isName(words[1])) {
					speaker += " " + words[1];
					
				}
				
				//write to context if the name is valid
				if (speaker != null && !speaker.toLowerCase().contains("null")) {
					context.write(new Text(speaker), new IntWritable(1));
				}
			}

		}
	}


	private boolean isName(String string) {
		// TODO Auto-generated method stub
		//should not contain specific sequence of characters
		//leave the ACT into output which indicate the firstname should be renewed.
		if (string.toLowerCase().contains("scene") 
				|| string.toLowerCase().contains("null")
				|| string.equalsIgnoreCase("all")
				|| string.length() < 3 || string == null) {
			return false;
		}
		// check if all characters are uppercase
		for (char letter : string.toCharArray()) {
			if (Character.isLowerCase(letter)) {
				return false;
			}
		}
		return true;
	}
}
