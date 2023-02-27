package edu.ucr.cs.cs172.hwold002;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.lang.String;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/** Index all text files under a directory.
 048 * <p>
 049 * This is a command-line application demonstrating simple Lucene indexing.
 050 * Run it with no command-line arguments for usage information.
 051 */


public class IndexFiles {
    private IndexFiles() {}

    /** Index all text files under a directory. */
    public static void main(String[] args) {


//        ConnectionString connectionString = new ConnectionString("mongodb+srv://admin:admin1@clusty.gehnm.mongodb.net/?retryWrites=true&w=majority");
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .applyConnectionString(connectionString)
//                .build();
//        MongoClient mongoClient = MongoClients.create(settings);
//        MongoDatabase database = mongoClient.getDatabase("test");


        String usage = "java org.apache.lucene.demo.IndexFiles"
                + " [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\n"
                + "This indexes the documents in DOCS_PATH, creating a Lucene index"
                + "in INDEX_PATH that can be searched with SearchFiles";
        String indexPath = "index";
//        String docsPath = null;
        boolean create = true;
//        for(int i=0;i<args.length;i++) {
//            if ("-index".equals(args[i])) {
//                indexPath = args[i+1];
//                i++;
//            } else if ("-docs".equals(args[i])) {
//                docsPath = args[i+1];
//                i++;
//            } else if ("-update".equals(args[i])) {
//                create = false;
//            }
//        }

//        if (docsPath == null) {
//            System.err.println("Usage: " + usage);
//            System.exit(1);
//        }

//        final Path docDir = Paths.get(docsPath);
//        if (!Files.isReadable(docDir)) {
//            System.out.println("Document directory '" +docDir.toAbsolutePath()+ "' does not exist or is not readable, please check the path");
//            System.exit(1);
//        }

        Date start = new Date();
        try {
            System.out.println("Indexing to directory '" + indexPath + "'...");

            Directory dir = FSDirectory.open(Paths.get(indexPath));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            if (create) {
                // Create a new index in the directory, removing any
                // previously indexed documents:
                iwc.setOpenMode(OpenMode.CREATE);
            } else {
                // Add new documents to an existing index:
                iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
            }

            // Optional: for better indexing performance, if you
            // are indexing many documents, increase the RAM
            // buffer.  But if you do this, increase the max heap
            // size to the JVM (eg add -Xmx512m or -Xmx1g):
            //
            // iwc.setRAMBufferSizeMB(256.0);

            IndexWriter writer = new IndexWriter(dir, iwc);
//            indexDocs(writer, docDir);
              indexDoc(writer);

            // NOTE: if you want to maximize search performance,
            // you can optionally call forceMerge here.  This can be
            // a terribly costly operation, so generally it's only
            // worth it when your index is relatively static (ie
            // you're done adding documents to it):
            //
            // writer.forceMerge(1);

            writer.close();

            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");

        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
        }
    }

    /**
     135   * Indexes the given file using the given writer, or if a directory is given,
     136   * recurses over files and directories found under the given directory.
     137   *
     138   * NOTE: This method indexes one document per input file.  This is slow.  For good
     139   * throughput, put multiple documents into your input file(s).  An example of this is
     140   * in the benchmark module, which can create "line doc" files, one document per line,
     141   * using the
     142   * <a href="../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
     143   * >WriteLineDocTask</a>.
     144   *
     145   * @param writer Writer to the index where the given file/dir info will be stored
     146   * @param path The file to index, or the directory to recurse into to find files to index
     147   * @throws IOException If there is a low-level I/O error
     148   */
//    static void indexDocs(final IndexWriter writer, Path path) throws IOException {
//        System.out.println("indexdocs");
//        if (Files.isDirectory(path)) {
//            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
//                @Override
//                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                    try {
//                        indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
//                    } catch (IOException ignore) {
//                        // don't index files that can't be read.
//                    }
//                    return FileVisitResult.CONTINUE;
//                }
//            });
//        } else {
//            indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
//        }
//    }
    private static void parseEmployeeObject(JSONObject result)
    {


    }
    /** Indexes a single document */
    static void indexDoc(IndexWriter writer) throws IOException {
        System.out.println("indexdoc");
//        try (InputStream stream = Files.newInputStream(file)) {
            // make a new, empty document

            JSONParser jsonParser = new JSONParser();
            try (FileReader reader = new FileReader("/Users/heyabwoldegebriel/Desktop/hwold002_phase2/hwold002_172/src/main/java/edu/ucr/cs/cs172/hwold002/sample 2.json"))
            {
                //Read JSON file
                Object obj = jsonParser.parse(reader);

                JSONArray resultList = (JSONArray) obj;

                //Iterate over employee array
//                resultList.forEach( result -> parseEmployeeObject( (JSONObject) result ) );

                for(int i = 0; i < resultList.size(); ++i) {
                    Document doc = new Document();

                    //Get employee object within list
                    JSONObject resultObject = (JSONObject) resultList.get(i);
                    JSONObject data = (JSONObject) resultObject.get("data");
                    String id = (String) data.get("id");
                    String text = (String) data.get("text");
                    String author = (String) data.get("author_id");
                    JSONObject entities = (JSONObject) data.get("entities");
                    Array hashtags = (Array) data.get("hashtags");

                    // Add the path of the file as a field named "path".  Use a
                    // field that is indexed (i.e. searchable), but don't tokenize
                    // the field into separate words and don't index term frequency
                    // or positional information:
//                    Field pathField = new StringField("path", file.toString(), Field.Store.YES);
//                    doc.add(pathField);

                    // Add the last modified date of the file a field named "modified".
                    // Use a LongPoint that is indexed (i.e. efficiently filterable with
                    // PointRangeQuery).  This indexes to milli-second resolution, which
                    // is often too fine.  You could instead create a number based on
                    // year/month/day/hour/minutes/seconds, down the resolution you require.
                    // For example the long value 2011021714 would mean
                    // February 17, 2011, 2-3 PM.
//                    doc.add(new LongPoint("modified", lastModified));
//                    System.out.println("TEXT: " + text);
                    doc.add(new StringField("id", id, Field.Store.YES));
                    doc.add(new TextField("text", text, Field.Store.YES));

                    if(hashtags != null) {
                        doc.add(new TextField("hashtags", hashtags.toString(), Field.Store.YES));
                    }

                    // Add the contents of the file to a field named "contents".  Specify a Reader,
                    // so that the text of the file is tokenized and indexed, but not stored.
                    // Note that FileReader expects the file to be in UTF-8 encoding.
                    // If that's not the case searching for special characters will fail.
//                    doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));

                    if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                        // New index, so we just add the document (no old document can be there):
                        System.out.println("adding " + "file " + id);
                        writer.addDocument(doc);
                    } else {
                        // Existing index (an old copy of this document may have been indexed) so
                        // we use updateDocument instead to replace the old one matching the exact
                        // path, if present:
                        System.out.println("updating " + "file");
                        writer.updateDocument(new Term("path", id), doc);
                    }
                }

                writer.commit();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
//        }
    }
}
