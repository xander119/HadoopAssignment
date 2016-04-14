import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Author: Zechen Wang
 * Combining the name of the conversation initiator and response person
 * 
 */
public class combineNameMapper extends Mapper<Text, IntWritable, TextPair, IntWritable>{
	//create global variables that enable us to read every second line of the file
	private static TextPair textpair = new TextPair();
	private static Text first = null;
	private static Text second = new Text();
	
	@Override
	protected void map(Text key, IntWritable value, Context context)
			throws IOException, InterruptedException {
		 String val = key.toString();

		 //Check if the this is a new scene, reset firstname to null if it is new scene.
		 if(val.toLowerCase().contains("act")){
			 first = null;
		 }else{
			//check if it is the start of the file
			 if(first == null){
				 //Assign the first character's name as the conversation initiator 
				 first = new Text(val);
			 }else{
				 //set the second as the response person, this is the person's name in second line
				 second.set(val);
				 //Set text pair with conversation initiator's name and response person's name
				 textpair.set(first, second);
				 //Check if the person is speaking to himself/herself
				 if(!first.toString().equalsIgnoreCase(second.toString())){
					 //Increase number of conversation of two person
					 context.write(textpair, new IntWritable(1));
				 }
				 //Replace the conversation initiator with the response person when finished one line 
				 first.set(second.toString());
			 }
		 }
		 
		 
		 
		 
		
	}
	

}
