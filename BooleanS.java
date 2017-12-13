import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BooleanS
{
    private static String LOG_prefix = "[SYSTEM]--";

    public static void listFiles(String dir) throws IOException
    {
        File files[] = new File(dir).listFiles();
        assert files != null;
        for (File file : files) System.out.println(file.getName());
    }
    private static void test_listFiles()
    {
        System.out.println(LOG_prefix + "Started search to dir");
        try
        {
            listFiles("/home/rahel/IdeaProjects/RI/src/Archives");
        } catch (IOException e)
        {
            System.out.println(LOG_prefix + "Dir error");
        }
        System.out.println(LOG_prefix + "Ended");
    }

    public static File[] getFiles(String dir)
    {
        File files[] = new File(dir).listFiles();
        assert files != null;
        return files;
    }

    public static ArrayList<ArrayList<String>> index(File[] files)
    {
        ArrayList<ArrayList<String>> indexed_list = new ArrayList<>();
        for (File arc: files) indexed_list.add(index_document(arc));
        //System.out.println(indexed_list);
        return indexed_list;
    }

    private static ArrayList<String> index_document(File file)
    {
        ArrayList<String> document_words = new ArrayList<>();
        document_words.add(file.getPath());
        document_words.add(file.getName());
        try
        {
            FileReader arc = new FileReader(file.getPath());
            BufferedReader arcReader = new BufferedReader(arc);
            String line = arcReader.readLine();

            while (line != null)
            {
                String words[] = line.split(" ");
                for (String word: words) if (valid(word) && !document_words.contains(word)) document_words.add(word);
                line = arcReader.readLine();
            }
            arc.close();
        } catch (IOException e)
        {
            System.err.printf("Error in arc open: %s.\n",
                    e.getMessage());
        }
        //System.out.print(document_words);
        return document_words;
    }

    private static boolean contains(String arr[], String verify)
    {
        boolean check = false;
        for (String to_verify: arr) if(to_verify.equals(verify)) check = true;
        return check;
    }

    private static boolean valid(String word){
        return !(word.contains(".") || word.contains(",")
                || word.contains(" ")
                || word.contains("!") || word.contains("?")
                || word.equals("") || word.equals(" "));
    }

    public static ArrayList<String> search(ArrayList<ArrayList<String>> list,String search)
    {
        ArrayList<String> to_return = new ArrayList<>();
        for (ArrayList<String> document : list)
        {
            for (String lex : document)
            {
                if (lex.equals(search))
                {
                    if(!to_return.contains(document.get(0))){
                        to_return.add(document.get(0));
                        break;
                    }
                }
            }
        }
        //System.out.println(to_return);
        return to_return;
    }

    public static ArrayList<String> booleanSearch(ArrayList<ArrayList<String>> list, Bool_search query)
    {
        switch (query.op)
        {
            case "V":
                return union(search(list, (String) query.arg1), search(list, (String) query.arg2));
            case "^":
                return intersection(search(list, (String) query.arg1), search(list, (String) query.arg2));
        }
        return null;
    }

    public static ArrayList<String> union(ArrayList<String> arg1, ArrayList<String> arg2)
    {
        ArrayList<String> to_return = new ArrayList<>();
        to_return.addAll(arg1);
        for (String item: arg2) if(!to_return.contains(item)) to_return.add(item);
        return  to_return;
    }

    public static ArrayList<String> intersection(ArrayList<String> arg1, ArrayList<String> arg2)
    {
        ArrayList<String> to_return = new ArrayList<>();
        for (String item : arg1 ) if(arg2.contains(item)) to_return.add(item);
        return to_return;
    }

    public static void main(String[] args)
    {
        ArrayList<ArrayList<String>> list = index(getFiles("/home/rahelund/Disney/J/RI/Bool/Archives"));
        //   booleanSearch("/home/rahel/IdeaProjects/RI/src/Archives", "lololo");
        Bool_search procura = new Bool_search();
        procura.arg1 = "contigo";
        procura.arg2 = "passear";
        procura.op = "^";

        Bool_search procura2 = new Bool_search();
        procura.arg1 = procura;
        procura.arg2 = "trevo";
        procura.op = "^";
        System.out.println(booleanSearch(list,procura2));

        //adicionar para pesquisas de pesquisas


    }
    
}
