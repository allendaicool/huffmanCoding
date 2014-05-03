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
	// public static HashSet<String> wordHolder;
	// only 2 genres
	public static String[] genres = new String[2];

	/* string variable store the name of genre and hashMap<String,Integer> keep track of the number of appearance of certain word 
	 * in the corresponding genre
	 */
	public static HashMap<String, HashMap<String,Integer>> genre = new HashMap<String,HashMap<String,Integer>>();

	/* string variable store the name of artistList and hashMap<String,Integer> keep track of the number of appearance of certain word 
	 * in the corresponding artistList
	 */
	public static HashMap<String, HashMap<String,Integer>> artistList = new HashMap<String,HashMap<String,Integer>>();

	/* string variable store the name of subgenre and hashMap<String,Integer> keep track of the number of appearance of certain word 
	 * in the corresponding subgenre
	 */
	public static HashMap<String, HashMap<String,Integer>> subgenre = new HashMap<String,HashMap<String,Integer>>();;

	/*
	 *   key value is the subgenre, the value is the genre that subgenre belongs to
	 */
	public static HashMap<String, String> genreSubgenreRelation = new HashMap<String,String>();

	/*
	 *   key value is the Subgenre, the value is the artists that are classified under specific subgenre
	 */
	public static HashMap<String, ArrayList<String>>ArtistSubgenre = new HashMap<String, ArrayList<String>>();

	/*
	 *   key value is the genre, the value is the artists that are classified under specific genre
	 */
	public static HashMap<String, ArrayList<String>>Artistgenre = new HashMap<String, ArrayList<String>>();



	public static int genreCount = 0 ;

	public static int subgenreCount = 0 ;

	public static int artistCount = 0 ;


	public static String CurrentArtists = null;

	public static String CurrentGenre = null;

	public static String CurrentSubGenre = null;


	// the collection of all of the words read in the file
	public static HashMap<String,Integer> countHolder;

	/*
	 *   shared resource this queue  is used for building multiple graphs
	 */
	public static Queue<GraphNode> queue = new PriorityQueue<GraphNode>();


	public static void main (String [] args) throws Exception 
	{

		countHolder  = new HashMap<String,Integer>();

		//
		ArrayList<GraphNode> treeHolder = new ArrayList<GraphNode>();

		String temp ; 
		// The first pass: retrieving all of the words
		try{

			FileReader file = new FileReader("src/input.txt");
			//	Scanner scanning = new Scanner( file);
			BufferedReader reader = new BufferedReader(file);

			/* first scan build the lyric library in the file
			 * 
			 */
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
					if(!countHolder.containsKey(list[i].toLowerCase()))
					{
						countHolder.put(list[i].toLowerCase(),1);
					}
				}

			}          
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* second scan 
		 *  
		 */
		try{
			// shall be the same file
			FileReader fileTemp2 = new FileReader("src/input.txt");

			BufferedReader reader = new BufferedReader(fileTemp2);

			while((temp=reader.readLine())!= null)
			{
				/*  ++ symbol is followed by the genre name
				 * 
				 */
				if(!temp.isEmpty()&&temp.charAt(0)=='+')
				{
					if(temp.contains("++"))
					{


						if(!genre.containsKey(temp.substring(3).toLowerCase()))
						{
							CurrentGenre = String.copyValueOf(temp.substring(3).toLowerCase().toCharArray());

							genre.put(String.copyValueOf(CurrentGenre.toCharArray()), new HashMap<String,Integer>());
							// put all word into genre library to build the huffman coding;
							genre.get(CurrentGenre).putAll(countHolder);

							Artistgenre.put(CurrentGenre, new ArrayList<String>());


							genres[genreCount] = String.copyValueOf(CurrentGenre.toCharArray());

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
				/*
				 *  << symbol is followed by the subgenre
				 */
				if(!temp.isEmpty()&&temp.charAt(0)=='<')
				{
					if(temp.contains("<<"))
					{
						//System.out.println(temp.substring(3));
						if(!subgenre.containsKey(temp.substring(3).toLowerCase()))
						{


							CurrentSubGenre = String.copyValueOf(temp.substring(3).toLowerCase().toCharArray());

							// associate subgenre with artist
							ArtistSubgenre.put(CurrentSubGenre, new ArrayList<String>());

							// asscociate subenre with genre
							genreSubgenreRelation.put(String.copyValueOf(CurrentSubGenre.toCharArray()), CurrentGenre);

							subgenre.put(String.copyValueOf(CurrentSubGenre.toCharArray()), new HashMap<String,Integer>());
							// dump all words into current subgenre library
							subgenre.get(CurrentSubGenre).putAll(countHolder);

							//subgenresArray[subgenreCount] = String.copyValueOf(temp.substring(3).toLowerCase().toCharArray());

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
				/*
				 * >> symbol is folloed by the author
				 */
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

							ArtistSubgenre.get(CurrentSubGenre).add(CurrentArtists);

							Artistgenre.get(CurrentGenre).add(CurrentArtists);

							artistList.put(String.copyValueOf(CurrentArtists.toCharArray()), new HashMap<String,Integer>());
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
				/* keep track of word count in specified hashMap<String, Integer>
				 * 
				 */
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

				}
			}     
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * compute block for the entire library
		 */
		int BlockCode ;
		BlockCode = ((int) Math.ceil(Math.log(countHolder.size())/Math.log(2)));

		printForGenre(BlockCode);
		System.out.println();

		printForSubGenre(BlockCode);
		System.out.println();

		printForArtist(BlockCode);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("start doing cross comparison -----------------------------------------------------------------------------");

		crossRatioOnSUbgenre(BlockCode);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("start doing author  comparison on subgenre -----------------------------------------------------------------------------");

		SubGenreRepresentativeAritist(BlockCode);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("start doing author  comparison on genre-----------------------------------------------------------------------------");
		GenreRepresentativeAritist(BlockCode);

	}




	public static void SubGenreRepresentativeAritist(int BlockCode)
	{

		HashMap<String, Double> authorPair = new HashMap<String,Double >();

		// get the set of subgener
		Set<String> subgenreNameList = ArtistSubgenre.keySet();
		ArrayList<String> authorCollec ;
		Iterator<String> iter = subgenreNameList.iterator();
		while(iter.hasNext())
		{
			String subgenreName = iter.next();

			// arraylist of authors
			authorCollec = ArtistSubgenre.get(subgenreName);
			double[] tobeSorted = new double[authorCollec.size()];
			for (int i = 0 ; i<authorCollec.size();i++)
			{
				HashMap<String,String> encoding = buildEncode(artistList.get(authorCollec.get(i)));


				int huffcodeEncodingLen = computeLength(subgenre.get(subgenreName),encoding);

				int BlockCodeLength = BlockCode*wordCount(subgenre.get(subgenreName));
				double ratio = (double)huffcodeEncodingLen/BlockCodeLength;
				tobeSorted[i]=ratio;
				authorPair.put(authorCollec.get(i), ratio);
			}
			Arrays.sort(tobeSorted);
			double firstPrice = tobeSorted[0];
			double secondPrice = tobeSorted[1];

			double thirdPrice = tobeSorted[2];

			Iterator<String> itt = authorPair.keySet().iterator();
			while (itt.hasNext())
			{

				String temp1 = itt.next();
				if(authorPair.get(temp1)==firstPrice)
				{
					System.out.println("start printing first representative author on subgenre  " +subgenreName+ "  is "+" -----------------------------------");
					System.out.println(temp1);
					System.out.println("compression ratio is " + firstPrice + "\n");

				}
				if(authorPair.get(temp1)==secondPrice)
				{
					System.out.println("start printing second representative author on subgenre  " +subgenreName+ " is "+" -----------------------------------");
					System.out.println(temp1);
					System.out.println("compression ratio is " + secondPrice + "\n");

				}
				if(authorPair.get(temp1)==thirdPrice)
				{
					System.out.println("start printing third representative author on subgenre  " +subgenreName+ " is "+" -----------------------------------");
					System.out.println(temp1);
					System.out.println("compression ratio is " + thirdPrice + "\n");		
				}
			}
		}

	}


	public static void GenreRepresentativeAritist(int BlockCode)
	{

		HashMap<String, Double> authorPair = new HashMap<String,Double >();

		// get the set of subgener
		Set<String> genreNameList = Artistgenre.keySet();
		ArrayList<String> authorCollec ;
		Iterator<String> iter = genreNameList.iterator();
		while(iter.hasNext())
		{
			String genreName = iter.next();

			// arraylist of authors
			authorCollec = Artistgenre.get(genreName);
			double[] tobeSorted = new double[authorCollec.size()];
			for (int i = 0 ; i<authorCollec.size();i++)
			{
				HashMap<String,String> encoding = buildEncode(artistList.get(authorCollec.get(i)));


				int huffcodeEncodingLen = computeLength(genre.get(genreName),encoding);

				int BlockCodeLength = BlockCode*wordCount(genre.get(genreName));
				double ratio = (double)huffcodeEncodingLen/BlockCodeLength;
				tobeSorted[i]=ratio;
				authorPair.put(authorCollec.get(i), ratio);
			}
			Arrays.sort(tobeSorted);
			double firstPrice = tobeSorted[0];
			double secondPrice = tobeSorted[1];
			double thirdPrice = tobeSorted[2];

			Iterator<String> itt = authorPair.keySet().iterator();
			while (itt.hasNext())
			{

				String temp1 = itt.next();
				if(authorPair.get(temp1)==firstPrice)
				{
					System.out.println("start printing first representative author on genre" +genreName+ "is "+" -----------------------------------");
					System.out.println(temp1);
					System.out.println("compression ratio is " + firstPrice + "\n");

				}
				if(authorPair.get(temp1)==secondPrice)
				{
					System.out.println("start printing second representative author on genre" +genreName+ "is "+" -----------------------------------");
					System.out.println(temp1);
					System.out.println("compression ratio is " + secondPrice + "\n");

				}
				if(authorPair.get(temp1)==thirdPrice)
				{
					System.out.println("start printing third representative author on genre" +genreName+ "is "+" -----------------------------------");
					System.out.println(temp1);
					System.out.println("compression ratio is " + thirdPrice + "\n");		
				}
			}
		}

	}



	public static HashMap<String,String> buildEncode( HashMap<String, Integer> pair)
	{
		StringBuffer empty = new StringBuffer();
		GraphNode root = null;
		initilizeGraph(pair);
		root = buildGraph(queue);
		encordProcess(root,empty);
		HashMap<String,String> encoding  =  new HashMap<String,String>();
		preOrderTraversal(root,encoding);
		queue.clear();
		return encoding;
	}

	/*
	public static void crossRatioOnSUbgenreHelper (String genreName, ArrayList<String> subGenrename,int BlockCode)
	{
		for (int i = 0 ; i < 4;i++)
		{
			String subGenre = subGenrename.get(i);

			HashMap<String,String> encoding = buildEncode(subgenre.get(subGenre));

			for(int j =0 ; j< 4;j++)
			{

				int huffcodeEncodingLen = computeLength(subgenre.get(subGenrename.get(j)),encoding);

				int BlockCodeLength = BlockCode*wordCount(subgenre.get(subGenrename.get(j)));

				//	System.out.println("The number of leadNode is "  + coutingLeafNode(root));
				System.out.println("start printing BLocking code on " + genreName +" subgenre " + subGenrename.get(j)+ " -----------------------------------");
				System.out.println(BlockCodeLength);
				System.out.println("end printting BLocking code -----------------------------------");
				System.out.println("start printing huffcode code -----------------------------------");
				System.out.println(huffcodeEncodingLen);
				System.out.println("end printing huffcode code on " + genreName + ": the compression ratio of subgenre: " + subGenre  +" on subgenre  " + subGenrename.get(j)+  "----------------------------------");
				System.out.println("compression ratio is " + (double)huffcodeEncodingLen/BlockCodeLength+"\n");
				//queue.clear();
			}

		}
	}
*/
	public static void crossRatioOnSUbgenre(int BlockCode)
	{
		

		Set<String> SubgenreSet = subgenre.keySet();
		Iterator<String> iter = SubgenreSet.iterator();
		System.out.println("\nstart doing  cross  comparison !!!!!!!!###### -----------------------------------");
		int loopcount = 0 ;
		while ( iter.hasNext())
		{
			String outSubGenre = iter.next();
			//Set<String> SubgenreSet2 = subgenre.keySet();
			Iterator<String> iter2 = SubgenreSet.iterator();
			HashMap<String,String> encoding = buildEncode(subgenre.get(outSubGenre));
			
			while(iter2.hasNext())
			{
				String InnersubGenre = iter2.next();
				int huffcodeEncodingLen = computeLength(subgenre.get(InnersubGenre),encoding);

				int BlockCodeLength = BlockCode*wordCount(subgenre.get(InnersubGenre));
				System.out.println(outSubGenre);
				System.out.println(InnersubGenre);
				System.out.println ((double)huffcodeEncodingLen/BlockCodeLength+"\n");
				loopcount++;
			}

		}
		System.out.println("End doing  cross  comparison !!!!!!!!###### -----------------------------------" + loopcount +"\n");

	}
/*
 * String firstGenre = genres[0];
		String secondGenre = genres[1];
		ArrayList<String> subGenrename1 = new ArrayList<String>();
		ArrayList<String> subGenrename2 = new ArrayList<String>();
 * Iterator<String> iter = SubgenreSet.iterator();
		while(iter.hasNext())
		{
			String subGenre = iter.next();
			if(genreSubgenreRelation.get(subGenre).equals(firstGenre))
			{
				subGenrename1.add(subGenre);
			}
			else
			{
				subGenrename2.add(subGenre);
			}
		}
		System.out.println("doing the first subgenre cross comparison ----------------------------------");

		crossRatioOnSUbgenreHelper(firstGenre,subGenrename1,BlockCode);
		System.out.println();
		System.out.println();
		System.out.println("doing the second subgenre cross comparison ----------------------------------");
		crossRatioOnSUbgenreHelper(secondGenre,subGenrename2,BlockCode);
 */
	public static void printForArtist(int BlockCode) 
	{
		Set<String> artistSet = artistList.keySet();

		Iterator<String> iterr = artistSet.iterator();
		while(iterr.hasNext())
		{		
			String AuthorName = iterr.next();

			HashMap<String,String> encoding = buildEncode(artistList.get(AuthorName));

			int huffcodeEncodingLen = computeLength(artistList.get(AuthorName),encoding);


			int BlockCodeLength = BlockCode*wordCount(artistList.get(AuthorName));
			//	System.out.println("The number of leadNode is "  + coutingLeafNode(root));
			System.out.println("start printing BLocking code for Artissts " + AuthorName +"-----------------------------------");
			System.out.println(BlockCodeLength);
			System.out.println("end printting BLocking code -----------------------------------");
			System.out.println("start printing huffcode code -----------------------------------");

			System.out.println(huffcodeEncodingLen);
			System.out.println("end printing huffcode code Artists " + AuthorName +"-----------------------------------");
			System.out.println("compression ratio is " + (double)huffcodeEncodingLen/BlockCodeLength+"\n");
		}
	}


	public static void printForSubGenre(int BlockCode) 
	{
		Set<String> SubgenreSet = subgenre.keySet();
		double maxValue = Double.MIN_VALUE;
		double minValue = Double.MAX_VALUE;
		Iterator<String> iterr = SubgenreSet.iterator();
		String mostDiverse = null;
		String leastDiverse = null ;
		while(iterr.hasNext())
		{		

			String genreName = iterr.next();
			HashMap<String,String> encoding = buildEncode(subgenre.get(genreName));

			int huffcodeEncodingLen = computeLength(subgenre.get(genreName),encoding);
			int BlockCodeLength = BlockCode*wordCount(subgenre.get(genreName));

			double compressionRatio = (double)huffcodeEncodingLen/BlockCodeLength;
			if(compressionRatio >maxValue )
			{
				maxValue = compressionRatio ;
				mostDiverse = genreName;
			}
			if(compressionRatio<minValue)
			{
				minValue = compressionRatio ;
				leastDiverse = genreName;
			}
			//	System.out.println("The number of leadNode is "  + coutingLeafNode(root));
			System.out.println("start printing BLocking code for subgenre " + genreName +"-----------------------------------");
			System.out.println(BlockCodeLength);
			System.out.println("end printting BLocking code -----------------------------------");
			System.out.println("start printing huffcode code -----------------------------------");

			System.out.println(huffcodeEncodingLen);
			System.out.println("end printing huffcode code subgenre " + genreName +"-----------------------------------");
			System.out.println("compression ratio is " + compressionRatio+"\n");
		}
		System.out.println(" *********************************************************************************8");
		System.out.println("the most diverse subgenre: "+ mostDiverse + " with the compression ratio equal to " + maxValue );
		System.out.println(" *********************************************************************************8");

		System.out.println(" *********************************************************************************8");
		System.out.println("the lease diverse subgenre is " +  leastDiverse+ " with the compression ratio equal to " + minValue );
		System.out.println(" *********************************************************************************8");
	}


	public static void printForGenre(int BlockCode) 
	{
		Set<String> genreSet = genre.keySet();
		double maxValue = Double.MIN_VALUE;
		String mostDiverse = null;
		double minValue = Double.MAX_VALUE;
		String LeastDiverse = null;

		Iterator<String> iterr = genreSet.iterator();
		while(iterr.hasNext())
		{		
			String genreName = iterr.next();
			//genre.get(genreName)
			HashMap<String,String> encoding = buildEncode(genre.get(genreName));	

			int huffcodeEncodingLen = computeLength(genre.get(genreName),encoding);
			int BlockCodeLength = BlockCode*wordCount(genre.get(genreName));
			double compressionRatio = (double)huffcodeEncodingLen/BlockCodeLength;
			if(compressionRatio >maxValue )
			{
				maxValue = compressionRatio ;
				mostDiverse = genreName;
			}
			if(compressionRatio < minValue )
			{
				minValue = compressionRatio ;
				LeastDiverse = genreName;
			}
			//	System.out.println("The number of leadNode is "  + coutingLeafNode(root));
			System.out.println("start printing BLocking code for genre " + genreName +"-----------------------------------");
			System.out.println(BlockCodeLength);
			System.out.println("end printting BLocking code -----------------------------------");
			System.out.println("start printing huffcode code -----------------------------------");

			System.out.println(huffcodeEncodingLen);
			System.out.println("end printing huffcode code genre " + genreName +"-----------------------------------");
			System.out.println("compression ratio is " + compressionRatio+"\n");

		}
		//System.out.println(root.frequency);
		System.out.println(" *********************************************************************************8");
		System.out.println("the most diverse genre is " + mostDiverse + " with the compression ratio equal to " + maxValue );
		System.out.println(" *********************************************************************************8");
		System.out.println(" *********************************************************************************8");
		System.out.println("the least diverse genre is " + LeastDiverse + " with the compression ratio equal to " + minValue );
		System.out.println(" *********************************************************************************8");
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


	public static int  preOrderTraversal (GraphNode node, HashMap<String,String> map) 
	{
		if(node.isLeaf())
		{

			map.put(node.ch, node.encoding);

			return node.encoding.length();
		}
		return preOrderTraversal(node.left,map)+preOrderTraversal(node.right,map);

	}



	public static void initilizeGraph(HashMap<String, Integer> mapping)
	{
		Iterator<Map.Entry<String, Integer>> it = mapping.entrySet().iterator();
		while (it.hasNext())
		{
			@SuppressWarnings("rawtypes")
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			Integer value = (Integer)entry.getValue();
			queue.add(new GraphNode(key,value,null,null));
			//count++;
		}		

	}

	public static GraphNode buildGraph( Queue<GraphNode> queue2)
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

