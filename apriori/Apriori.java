package apriori;
import java.io.*;
import java.util.*;

public class Apriori {
	public static void main(String[] args) {
        AprioriCalculation ap = new AprioriCalculation();
        ap.apriori();
    }
}

class AprioriCalculation
{
    Vector<String> candidates=new Vector<String>(); //candidates
    String inputFile="input.txt"; //input file
    String matrixFile="matrix.txt"; //matrix file
    String resultFile="result.txt";//result file
    int numCols; //number of columns
    int numRows; //number of rows
    double minSup; //minimum support for a frequent itemset
    String oneVal[]; //array of value per column that will be treated as a '1'
    String sepeChar = " ";

    public void apriori() //main method
    {
        Date d; //date object for timing purposes
        long start, end; //start and end time
        int itemsetNumber=0; //the current itemset being looked at
        //get inputfile
        getInput();

        System.out.println("Apriori algorithm has started.\n");

        d = new Date();
        start = d.getTime();

        //while not complete
        do
        {
            //increase the itemset that is being looked at
            itemsetNumber++;

            //generate the candidates
            calcCandidates(itemsetNumber);

            //determine and display frequent itemsets
            calcFreqSets(itemsetNumber);
            if(candidates.size()!=0)
            {
                System.out.println("Frequent " + itemsetNumber + "-itemsets");
                System.out.println(candidates);
            }
        //if there are <=1 frequent items, then its the end. This prevents reading through the database again. When there is only one frequent itemset.
        }while(candidates.size()>1);

        d = new Date();
        end = d.getTime();

        //execution time
        System.out.println("Execution time is: "+((double)(end-start)/1000) + " seconds.");
    }

    private void getInput() //get information about matrix
    {
        FileWriter fw;
        BufferedWriter file_out;

        System.out.println("\tInput File: " + inputFile);
        System.out.println("\tMatrix File: " + matrixFile);
        System.out.println("\tResult File: " + resultFile);

        try
        {
             FileInputStream file_in = new FileInputStream(inputFile);
             BufferedReader data_in = new BufferedReader(new InputStreamReader(file_in));
             //number of cols
             numCols=Integer.valueOf(data_in.readLine()).intValue();

             //number of rows
             numRows=Integer.valueOf(data_in.readLine()).intValue();

             //min support
             minSup=(Double.valueOf(data_in.readLine()).doubleValue());

             System.out.print("\nInput : "+numCols+" columns, "+numRows+" rows, ");
             System.out.println("minsupport = "+minSup+"%");
             System.out.println();
             minSup/=100.0;

            oneVal = new String[numCols];
            for(int i=0; i<oneVal.length; i++)
                oneVal[i]="1";

        }
        //if there is an error, print the message
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    private void calcCandidates(int n) // calculate cadidates sets
    {
        Vector<String> tempCandidates = new Vector<String>(); //temporary candidate string vector
        String str1, str2; //strings that will be used for comparisons
        StringTokenizer st1, st2; //string tokenizers for the two itemsets being compared

        //if its the first set, candidates are just the numbers
        if(n==1)
        {
            for(int i=1; i<=numCols; i++)
            {
                tempCandidates.add(Integer.toString(i));
            }
        }
        else if(n==2) //second itemset is just all combinations of itemset 1
        {
            //add each itemset from the previous frequent itemsets together
            for(int i=0; i<candidates.size(); i++)
            {
                st1 = new StringTokenizer(candidates.get(i));
                str1 = st1.nextToken();
                for(int j=i+1; j<candidates.size(); j++)
                {
                    st2 = new StringTokenizer(candidates.elementAt(j));
                    str2 = st2.nextToken();
                    tempCandidates.add(str1 + " " + str2);
                }
            }
        }
        else
        {
            //for each itemset
            for(int i=0; i<candidates.size(); i++)
            {
                //compare to the next itemset
                for(int j=i+1; j<candidates.size(); j++)
                {
                    //create the strigns
                    str1 = new String();
                    str2 = new String();
                    //create the tokenizers
                    st1 = new StringTokenizer(candidates.get(i));
                    st2 = new StringTokenizer(candidates.get(j));

                    //make a string of the first n-2 tokens of the strings
                    for(int s=0; s<n-2; s++)
                    {
                        str1 = str1 + " " + st1.nextToken();
                        str2 = str2 + " " + st2.nextToken();
                    }

                    //if they have the same n-2 tokens, add them together
                    if(str2.compareToIgnoreCase(str1)==0)
                        tempCandidates.add((str1 + " " + st1.nextToken() + " " + st2.nextToken()).trim());
                }
            }
        }
        //clear the old candidates
        candidates.clear();
        //set the new ones
        candidates = new Vector<String>(tempCandidates);
        tempCandidates.clear();
    }

    private void calcFreqSets(int n) //calculate frequent sets
    {
        Vector<String> freqCandidates = new Vector<String>(); //the frequent candidates for the current itemset
        FileInputStream file_in; //file input stream
        BufferedReader data_in; //data input stream
        FileWriter fw;
        BufferedWriter file_out;

        StringTokenizer st, stFile; //tokenizer for candidate and transaction
        boolean match; //whether the transaction has all the items in an itemset
        boolean trans[] = new boolean[numCols]; //array to hold a transaction so that can be checked
        int count[] = new int[candidates.size()]; //the number of successful matches

        try
        {
                //output file
                fw= new FileWriter(resultFile, true);
                file_out = new BufferedWriter(fw);
                //load the transaction file
                file_in = new FileInputStream(matrixFile);
                data_in = new BufferedReader(new InputStreamReader(file_in));

                //for each transaction
                for(int i=0; i<numRows; i++)
                {
                    //System.out.println("Got here " + i + " times"); //useful to debug files that you are unsure of the number of line
                    stFile = new StringTokenizer(data_in.readLine(), sepeChar); //read a line from the file to the tokenizer
                    //put the contents of that line into the transaction array
                    for(int j=0; j<numCols; j++)
                    {
                        trans[j]=(stFile.nextToken().compareToIgnoreCase(oneVal[j])==0); //if it is not a 0, assign the value to true
                    }

                    //check each candidate
                    for(int c=0; c<candidates.size(); c++)
                    {
                        match = false; //reset match to false
                        //tokenize the candidate so that we know what items need to be present for a match
                        st = new StringTokenizer(candidates.get(c));
                        //check each item in the itemset to see if it is present in the transaction
                        while(st.hasMoreTokens())
                        {
                            match = (trans[Integer.valueOf(st.nextToken())-1]);
                            if(!match) //if it is not present in the transaction stop checking
                                break;
                        }
                        if(match) //if at this point it is a match, increase the count
                            count[c]++;
                    }

                }
                for(int i=0; i<candidates.size(); i++)
                {
                    //write to result file
                    if((count[i]/(double)numRows)>=minSup)
                    {
                        freqCandidates.add(candidates.get(i));
                        file_out.write("["+candidates.get(i) +"]"+ "," + count[i]/(double)numRows + "\n");
                    }
                }
                file_out.write("\n");
                file_out.close();
        }
        //if error at all in this process, catch it and print the error messate
        catch(IOException e)
        {
            System.out.println(e);
        }
        //clear old candidates
        candidates.clear();
        //new candidates are the old frequent candidates
        candidates = new Vector<String>(freqCandidates);
        freqCandidates.clear();
    }
}