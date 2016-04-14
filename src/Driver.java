import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
*	Author Zechen Wang	
*
*/
public class Driver extends Configured implements Tool{
	private static Configuration conf;
	
	public int run(String[] arg0) throws Exception {
		// TODO Auto-generated method stub
		conf = new Configuration();
		Job job = Job.getInstance(conf);
		
		//Set two mappers with approprate input output format
		Configuration splitMapConfig = new Configuration(false);
		ChainMapper.addMapper(job, PlaySplitMapper.class, LongWritable.class,Text.class, Text.class, IntWritable.class, splitMapConfig);
		
		Configuration combineNameConfig = new Configuration(false);
		ChainMapper.addMapper(job, combineNameMapper.class, Text.class,IntWritable.class, TextPair.class, IntWritable.class, combineNameConfig);
		
		
		if(arg0.length!=2){
			System.out.println("Usage: Shakespears Plays <input path> <output path>");
			System.exit(-1);
		}
		job.setJarByClass(Driver.class);
		job.setJobName("Shakespears Plays");
		
		
		job.setReducerClass(PlayReducer.class);
		

		job.setOutputKeyClass(TextPair.class);
		job.setOutputValueClass(IntWritable.class);
		
		//Literate each play file in the directory
		FileInputFormat.setInputDirRecursive(job, true);
		
		FileInputFormat.addInputPath(job, new Path(arg0[0]));
		FileOutputFormat.setOutputPath(job, new Path(arg0[1]));
		
		int r  = ( job.waitForCompletion(true) ? 0 : 1);
		System.out.println("Job is completed ");
		
		return r;
	}
	public static void main(String[] args) throws Exception{
		
		int result = ToolRunner.run(conf,new Driver(), args);
		
		System.exit(result);

	}

}
