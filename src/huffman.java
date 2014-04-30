import java.util.*;

import java.util.Map.Entry;
import java.io.*;
public class huffman {
	@SuppressWarnings("unused")
	private  static class GraphNode implements Comparable<GraphNode> {
		String ch;
		int frequency;
		private GraphNode left;
		private GraphNode right;
		private String encoding;
		GraphNode(String ch, int frequency,  GraphNode left,  GraphNode right) {
			this.ch = ch;
			this.frequency = frequency;
			this.left = left;
			this.right = right;
		}

		public void encode (String temp )
		{
			this.encoding = String.copyValueOf(temp.toCharArray());
		}

		public String getEncode()
		{
			return this.encoding;
		}
		public GraphNode getLeft()
		{
			return this.left;
		}
		public GraphNode getRight()
		{
			return this.right;
		}
		public void setRight(GraphNode right)
		{
			this.right = right;
		}
		public void setLeft (GraphNode left)
		{
			this.left = left;
		}
		public boolean isLeaf ()
		{
			return this.left==null && this.right==null;
		}
		@Override
		public int compareTo(GraphNode arg0) {
			// TODO Auto-generated method stub
			assert(arg0 instanceof GraphNode):"different class";
			if(arg0.frequency < this.frequency)
			{
				return 1;
			}
			else if(arg0.frequency > this.frequency)
			{
				return -1 ;
			}
			else
			{
				return 0 ;
			}
		}
	}
	/*
	 * library 
	 */
	public static HashSet<String> wordHolder;
	// only 2 genres
	public static String[] genres = new String[2];

	public static String[] subgenresArray = new String[8];

	public static HashMap<String, HashMap<String,Integer>> genre = new HashMap<String,HashMap<String,Integer>>();

	public static HashMap<String, HashMap<String,Integer>> artistList = new HashMap<String,HashMap<String,Integer>>();

	public static HashMap<String, String> genreSubgenreRelation = new HashMap<String,String>();

	public static HashMap<String,String> SubgenreArtistRelation =  new HashMap<String,String>();


	public static HashMap<String, HashMap<String,Integer>> subgenre = new HashMap<String,HashMap<String,Integer>>();;



	public static HashMap<String,HashSet<String>> woddd;

	public static int genreCount = 0 ;

	public static int subgenreCount = 0 ;

	public static int artistCount = 0 ;

	public static HashMap<String, HashMap<String,Integer>> artists;

	public static String CurrentArtists = null;

	public static String CurrentGenre = null;

	public static String CurrentSubGenre = null;


	// shall be  replaced in our case;

	public static HashMap<String,Integer> countHolder;

	/*
	 *   shared resource this queue  is used for building multiple graphs
	 */
	public static Queue<GraphNode> queue = new PriorityQueue<GraphNode>();

	//public static HashMap<String,String> encodeString = new HashMap<String,String>();

	public static void main (String [] args) throws Exception 
	{
		wordHolder = new HashSet<String>();

		countHolder  = new HashMap<String,Integer>();

		//
		ArrayList<GraphNode> treeHolder = new ArrayList<GraphNode>();

		String temp ; 
		// The first pass: retrieving all of the words
		try{

			FileReader file = new FileReader("src/input.txt");
			//	Scanner scanning = new Scanner( file);
			BufferedReader reader = new BufferedReader(file);

			while((temp=reader.readLine())!= null)
			{
				//	System.out.println(temp);
				if(!temp.isEmpty()&&(temp.charAt(0)=='+'||temp.charAt(0)=='<'||temp.charAt(0)=='>'))
				{
					continue;
				}
				String[] list = temp.split("[^a-zA-Z']+");

				// [^a-zA-Z] [,.?!\"\\s+]
				int len = list.length;

				for(int  i = 0 ; i < len ; i++)
				{
					if(!wordHolder.contains(list[i].toLowerCase()))
					{
						wordHolder.add(list[i].toLowerCase());
					}
					//	 genre.get(CurrentGenre).put(list[i].toLowerCase(),genre.get(CurrentGenre).get(list[i].toLowerCase())+1);
				}

			}          
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(String tempWord :wordHolder)
		{
			/* put all the word into the hashmap 
			 *  
			 */
			//System.out.println(tempWord);
			countHolder.put(tempWord, 1);
		}

		// doing parse for genre
		try{
			// shall be the same file
			FileReader fileTemp2 = new FileReader("src/input.txt");

			BufferedReader reader = new BufferedReader(fileTemp2);

			while((temp=reader.readLine())!= null)
			{
				if(!temp.isEmpty()&&temp.charAt(0)=='+')
				{
					if(temp.contains("++"))
					{

						//System.out.println(temp.substring(3));

						if(!genre.containsKey(temp.substring(3).toLowerCase()))
						{
							CurrentGenre = String.copyValueOf(temp.substring(3).toLowerCase().toCharArray());
							genre.put(CurrentGenre, new HashMap<String,Integer>());
							// put all word into genre library to build the huffman coding;
							genre.get(CurrentGenre).putAll(countHolder);


							genres[genreCount] = String.copyValueOf(temp.substring(3).toLowerCase().toCharArray());
							genreCount++;

							if(!(genreCount<3))
							{
								throw new Exception("It should be exactly two genres");
							}
						}
					//	System.out.println();
						continue;
					}
				}
				if(!temp.isEmpty()&&temp.charAt(0)=='<')
				{
					if(temp.contains("<<"))
					{
						//System.out.println(temp.substring(3));
						if(!subgenre.containsKey(temp.substring(3).toLowerCase()))
						{
							CurrentSubGenre = String.copyValueOf(temp.substring(3).toLowerCase().toCharArray());

							subgenre.put(String.copyValueOf(CurrentSubGenre.toLowerCase().toCharArray()), new HashMap<String,Integer>());
							// dump all words into current subgenre library
							subgenre.get(CurrentSubGenre).putAll(countHolder);

							subgenresArray[subgenreCount] = String.copyValueOf(temp.substring(3).toLowerCase().toCharArray());

							subgenreCount++;
							// assert the number of subgenre is less than 9

							if(!(subgenreCount<9))
							{
								throw new Exception("It should be exactly eight subgenres");
							}

						}
					//	System.out.println();
						continue;
					}
				}
				if(!temp.isEmpty()&&temp.charAt(0)=='>')
				{
					if(temp.contains(">>"))
					{
						// we have to modify the identifier
						//System.out.println(temp.substring(3));

						if(!artistList.containsKey(temp.substring(3).toLowerCase()))
						{
						//	System.out.println(temp.substring(3));

							CurrentArtists = String.copyValueOf(temp.substring(3).toLowerCase().toCharArray());

							artistList.put(String.copyValueOf(CurrentArtists.toLowerCase().toCharArray()), new HashMap<String,Integer>());
							// dump all words into current subgenre library
							artistList.get(CurrentArtists).putAll(countHolder);

							artistCount++;
							if(artistCount > 80)
							{
								throw new Exception("It should be exactly eight subgenres");
							}
							//subgenresArray[subgenreCount] = String.copyValueOf(temp.substring(2).toCharArray());

							//	subgenreCount++;
							// assert the number of subgenre is less than 9						
						}
					//	System.out.println();
						continue;
					}
				}


				String[] list = temp.split("[^a-zA-Z']+");
				int len = list.length;
				for(int  i = 0 ; i < len ; i++)
				{	
					String lowercase = list[i].toLowerCase();
					if(	genre.get(CurrentGenre).containsKey(lowercase))
					{
						genre.get(CurrentGenre).put(lowercase,genre.get(CurrentGenre).get(lowercase)+1);
					}
					if(subgenre.get(CurrentSubGenre).containsKey(lowercase))
					{
						subgenre.get(CurrentSubGenre).put(lowercase,subgenre.get(CurrentSubGenre).get(lowercase)+1);
					}
					if(artistList.get(CurrentArtists).containsKey(list[i].toLowerCase()))
					{
						artistList.get(CurrentArtists).put(lowercase, artistList.get(CurrentArtists).get(lowercase)+1);
					}
					/*
					if(countHolder.containsKey(list[i].toLowerCase()))
					{
						countHolder.put(list[i].toLowerCase(), countHolder.get(list[i].toLowerCase())+1);
					}*/

				}
			}     
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printForGenre(treeHolder);
		System.out.println();
		printForSubGenre();
		//System.out.println(root.frequency);
	/*	System.out.println("\naritst number is " + artistCount);
		System.out.println("genre number is " + genreCount);
		System.out.println("genre number is " + subgenreCount);*/

		
	}

	
	public static void printForSubGenre() throws Exception
	{
		Set<String> SubgenreSet = subgenre.keySet();

		GraphNode root = null;

		Iterator<String> iterr = SubgenreSet.iterator();
		int loopCount = 0;
		while(iterr.hasNext())
		{		
			String genreName = iterr.next();
			loopCount++;
			initilizeGraph(subgenre.get(genreName));

			root = buildGraph(queue);
			StringBuffer empty = new StringBuffer();

			encordProcess(root,empty);
			/*
			 * store the root in arraylist<GraphNode> 
			 */
		//	treeHolder.add(root);
			HashMap<String,String> encoding  =  new HashMap<String,String>();
			int BlockCode ;
			BlockCode = ((int) Math.ceil(Math.log(wordHolder.size())/Math.log(2)));
			preOrderTraversal(root,encoding);
			int huffcodeEncodingLen = computeLength(subgenre.get(genreName),encoding);
			int BlockCodeLength = BlockCode*wordCount(subgenre.get(genreName));
			//	System.out.println("The number of leadNode is "  + coutingLeafNode(root));
			System.out.println("start printing BLocking code for subgenre " + loopCount +"-----------------------------------");
			System.out.println(BlockCodeLength);
			System.out.println("end printting BLocking code -----------------------------------");
			System.out.println("start printing huffcode code -----------------------------------");

			System.out.println(huffcodeEncodingLen);
			System.out.println("end printing huffcode code subgenre " + loopCount +"-----------------------------------");
			System.out.println("compression ratio is " + (double)huffcodeEncodingLen/BlockCodeLength+"\n");
			queue.clear();

		}
		
	}
	
	
	public static void printForGenre(ArrayList<GraphNode> treeHolder) throws Exception
	{
		Set<String> genreSet = genre.keySet();

		GraphNode root = null;

		Iterator<String> iterr = genreSet.iterator();
		int loopCount = 0;
		while(iterr.hasNext())
		{		
			String genreName = iterr.next();
			loopCount++;
			initilizeGraph(genre.get(genreName));

			root = buildGraph(queue);
			StringBuffer empty = new StringBuffer();

			encordProcess(root,empty);
			/*
			 * store the root in arraylist<GraphNode> 
			 */
			treeHolder.add(root);
			HashMap<String,String> encoding  =  new HashMap<String,String>();
			int BlockCode ;
			BlockCode = ((int) Math.ceil(Math.log(wordHolder.size())/Math.log(2)));
			preOrderTraversal(root,encoding);
			int huffcodeEncodingLen = computeLength(genre.get(genreName),encoding);
			int BlockCodeLength = BlockCode*wordCount(genre.get(genreName));
			//	System.out.println("The number of leadNode is "  + coutingLeafNode(root));
			System.out.println("start printing BLocking code for genre " + loopCount +"-----------------------------------");
			System.out.println(BlockCodeLength);
			System.out.println("end printting BLocking code -----------------------------------");
			System.out.println("start printing huffcode code -----------------------------------");

			System.out.println(huffcodeEncodingLen);
			System.out.println("end printing huffcode code genre " + loopCount +"-----------------------------------");
			System.out.println("compression ratio is " + (double)huffcodeEncodingLen/BlockCodeLength+"\n");
			queue.clear();

		}
		//System.out.println(root.frequency);
		System.out.println("\naritst number is " + artistCount);
		System.out.println("genre number is " + genreCount);
		System.out.println("genre number is " + subgenreCount);
	}
	public static int wordCount (HashMap<String,Integer> map)
	{
		Set<String> keyset = map.keySet();
		Iterator<String> ii = keyset.iterator();
		int wordCount = 0 ;
		while (ii.hasNext())
		{
			String temp = ii.next();
			wordCount+= (map.get(temp)-1);
		}
		return wordCount;
	}
	public static int computeLength(HashMap<String, Integer> map,HashMap<String,String> map2)
	{
		Set<String> keyset = map.keySet();
		Iterator<String> ii = keyset.iterator();
		int totalLength = 0 ;
		while (ii.hasNext())
		{
			String temp = ii.next();
			totalLength+= (map.get(temp)-1)*map2.get(temp).length();
		}
		return totalLength;
	}


	public static int  preOrderTraversal (GraphNode node, HashMap<String,String> map) throws Exception
	{
		if(node.isLeaf())
		{

			//System.out.println("it is leafNode @!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1    " + node.ch);
			map.put(node.ch, node.encoding);
			return node.encoding.length();
		}
		return preOrderTraversal(node.left,map)+preOrderTraversal(node.right,map);

	}

	public static int  coutingLeafNode (GraphNode node)
	{

		if(node.isLeaf())
		{
			System.out.println("it is leafNode @!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
			return 1;
		}

		return coutingLeafNode(node.left)+coutingLeafNode(node.right);

	}

	/*
	public static int totalLenghByHuf (HashMap<String, String> map)
	{
		int length = 0 ;
		Iterator<Map.Entry<String, String>> it = encodeString.entrySet().iterator();
		while (it.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) it.next();
			String value = (String)entry.getValue();
			length += value.length();
		}
		return length;
	}*/





	public static void initilizeGraph(HashMap<String, Integer> mapping)
	{
		Iterator<Map.Entry<String, Integer>> it = mapping.entrySet().iterator();
		int  count = 0 ;
		while (it.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			Integer value = (Integer)entry.getValue();
			queue.add(new GraphNode(key,value,null,null));
			count++;
		}		
		//System.out.println("there are so many graphNodes"+ count);

	}

	public static GraphNode buildGraph( Queue<GraphNode> queue2) throws Exception
	{
		while(queue2.size()>1)
		{
			GraphNode min = queue2.poll();
			GraphNode min2 = queue2.poll();

			GraphNode combination = new GraphNode(null,min.frequency+min2.frequency,min,min2);
			queue2.add(combination);
		}
		
		return queue2.poll();
	}

	public static void encordProcess(GraphNode node,StringBuffer sequence)
	{
		if(node.isLeaf())
		{
			node.encode(sequence.toString());
			//System.out.println(node.ch+ "is encoded as " +sequence.toString() );
			return ;
		}
		StringBuffer leftdown;

		leftdown = new StringBuffer(sequence.toString());

		leftdown.append('0');

		StringBuffer rightdown;

		rightdown = new StringBuffer(sequence.toString());

		rightdown.append('1');

		encordProcess(node.getLeft(),leftdown);

		encordProcess(node.getRight(),rightdown);
	}

	public static void levelOrderTraversal (GraphNode root)
	{
		Queue<GraphNode> table1 = new LinkedList<GraphNode>();
		Queue<GraphNode> table2 = new LinkedList<GraphNode>();
		table1.add(root);
		System.out.println("start printing------------------------------");
		while (!(table1.isEmpty() && table2.isEmpty()))
		{
			if(!table1.isEmpty())
			{

				while(!table1.isEmpty())
				{
					GraphNode temp1 =  table1.poll();

					System.out.print(temp1.frequency+ "   "   + temp1.ch+  "   ");
					System.out.print("    "       ); 
					if(temp1.left != null)
					{
						table2.add(temp1.left);
					}
					if(temp1.right != null)
					{
						table2.add(temp1.right);
					}
				}
			}
			else
			{

				while(!table2.isEmpty())
				{

					GraphNode temp1 =  table2.poll();

					System.out.print(temp1.frequency+ "   "   + temp1.ch+  "   ");
					System.out.print("    "       ); 
					if(temp1.left != null)
					{
						table1.add(temp1.left);
					}
					if(temp1.right!= null)
					{
						table1.add(temp1.right);
					}
				}
			}
			System.out.println();
		}
		System.out.println("finishing printing------------------------------");

	}

}
