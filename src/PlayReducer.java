import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Author: Zechen Wang
 * Combining the name of the conversation initiator and response person
 * 
 */
public class PlayReducer extends Reducer<TextPair, IntWritable, Text, IntWritable>{
	private static Text textPairText = new Text();
	@Override
	protected void reduce(TextPair key, Iterable<IntWritable> values,Context context)
			throws IOException, InterruptedException {
		
		int count=0;
        for(IntWritable value: values)
        {
			//count number of conversations occured and sum uo
            count += value.get();
        }
 
        textPairText.set(key.toString());
        context.write(textPairText, new IntWritable(count));
	
	}
}
