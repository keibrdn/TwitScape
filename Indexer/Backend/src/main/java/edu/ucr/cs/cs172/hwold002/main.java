package edu.ucr.cs.cs172.hwold002;

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




public class main {
    /**
     * Parse a Json file. The file path should be included in the constructor
     */

    static int count = 1;

    public static void main(String[] args) {

        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("/Users/heyabwoldegebriel/Desktop/cs172/hwold002_172/src/main/java/edu/ucr/cs/cs172/hwold002/sample.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONArray resultList = (JSONArray) obj;

            //Iterate over employee array
            resultList.forEach( result -> parseEmployeeObject( (JSONObject) result ) );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void parseEmployeeObject(JSONObject result)
    {
        //Get employee object within list
        JSONObject resultObject = (JSONObject) result.get("data");
        String text = (String) resultObject.get("text");

        //Create file
        try {
            File myObj = new File("/Users/heyabwoldegebriel/Desktop/cs172/hwold002_172/input/" + count + ".txt");
            FileWriter myWriter = new FileWriter("/Users/heyabwoldegebriel/Desktop/cs172/hwold002_172/input/" + count + ".txt");
            myWriter.write(text);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            count++;
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
