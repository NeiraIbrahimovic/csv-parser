package portfolio.algorithms;/*
 * I attest that the code in this file is entirely my own except for the starter
 * code provided with the assignment and the following exceptions:
 * <Enter all external resources and collaborations here. Note external code may
 * reduce your score but appropriate citation is required to avoid academic
 * integrity violations. Please see the Course Syllabus as well as the
 * university code of academic integrity:
 * Signed,
 * Author: Neira Ibrahimovic
 * Date: 2025-03-10
 */

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@code CSVReader} provides a stateful API for streaming individual CSV rows
 * as arrays of strings that have been read from a given CSV file.
 *
 * @author Neira Ibrahimovic
 */
public class CSVReader implements Serializable {
    @Serial
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 5130409650040L;
    private final CharacterReader reader;
    private int pushedBackChar = -1; //Store a character that needs to be pushed back

    public CSVReader(CharacterReader reader) {
        this.reader = reader;
    }

    /**
     * This method uses the class's {@code CharacterReader} to read in just enough
     * characters to process a single valid CSV row, represented as an array of
     * strings where each element of the array is a field of the row. If formatting
     * errors are encountered during reading, this method throws a
     * {@code CSVFormatException} that specifies the exact point at which the error
     * occurred.
     *
     * @return a single row of CSV represented as a string array, where each
     *         element of the array is a field of the row; or {@code null} when
     *         there are no more rows left to be read.
     * @throws IOException when the underlying reader encountered an error
     * @throws CSVFormatException when the CSV file is formatted incorrectly
     */
    public String[] readRow() throws IOException, CSVFormatException {
    	//Create an ArrayList variable to store parsed strings in a row
        List<String> row = new ArrayList<>(); 
        
        //Initialize a new StringBuilder to hold characters for the current field being processed (separated by commas)
        StringBuilder field = new StringBuilder();
        
        //Initialize a boolean variable to track if the current field contains any data
        boolean fieldHasData = false; 
        
        //Initialize a flag to track if we've read any characters in this row 
		boolean hasReadAnyChar = false;

        //Define states for the state machine
        enum State { DEFAULT, IN_QUOTE, END_QUOTE }
        
        //Initialize the state variable to start in the default state
        State state = State.DEFAULT; 

        //Declare an int variable ch that will be used to store each character read from the input
        int ch;
        
        //Read each character until end of file is reached
        while (true) {
        	
            //Check if we have a pushed back character (character that was read but needed to be processed later)
            if (pushedBackChar != -1) {
                ch = pushedBackChar;
                pushedBackChar = -1;
            } else {
            	//If no pushed back characters, read the next character
                ch = reader.read();
            }
            
            //Break if end of file
            if (ch == -1) {
                break;
            }
            
        	//Cast the int to a char for processing and store in variable
            char character = (char) ch;
            
            //Set the flag to true to indicate a character has been read
            hasReadAnyChar = true;
            
            //Create a state machine for all possible states
            switch (state) {
            
            	//Default state, processing normal CSV fields (not within quotes)
                case DEFAULT: 
                	
                	//Create a state machine for all possible characters
                    switch (character) {
       		
                    	//Comma case: Comma indicates the end of a field in a row
                        case ',': 
                        	//Store the completed field that came before the comma
                            row.add(field.toString()); 
                            //Reset field for next value
                            field.setLength(0); 
                            //Mark field as no longer containing data upon resetting
                            fieldHasData = false; 
                            break;
                            
                        //Quote case: Start of a quoted field
                        case '"': 
                        	//If we encounter a quote and the field does not yet have data, this is a starting quote and should be moved to IN_QUOTE state
                        	 if (!fieldHasData) {
                        	 //Start of a quoted field
                        	     state = State.IN_QUOTE;
                        	 }
                        	//Else, if field already has data, it's an error
                        	else{ 
                            	throw new CSVFormatException("A field cannot have a quote in the middle of the field unless surrounded by quotes", 0, 0, row.size(), field.length()); 
                            }
                            break;
                            
                        //Newline case: Indicates the end of a row
                        case '\n': 
                        	//Store the last field before returning
                            row.add(field.toString()); 
                            //Return the parsed row
                            return row.toArray(new String[0]); 
                            
                        //CR or  CRLF case: Standalone newline or next line
                        case '\r':                      	
                            //Check for CRLF by looking ahead
                            int nextChar = reader.read();
                            if (nextChar == '\n') {
                            	//CRLF detected, add the field and return the row
                                row.add(field.toString());
                                return row.toArray(new String[0]);
                            } else {
                            	//Save the next character since we've already read it
                                pushedBackChar = nextChar;
                            	//Standalone /r detected - throw exception
                                throw new CSVFormatException("Standalone CR detected", 0, 0, row.size(), field.length());
                            }
                            
                        //Default case: normal characters
                       	default:	     		                 		
                       		//Append character to the field
                       		field.append(character); 
                       		//Mark field as containing data
                       		fieldHasData = true; 
                       		break;
                            
                    }
                    
                    break;
                
                //In-quote state, processing characters inside a quoted field
                case IN_QUOTE: 
                    
                	//Create switch machine for all possible characters
                	switch(character) {
                        
                        //In the case that you have double quotes inside a quoted field, you need to check if it is an escaped quote
                        case '"':
                            int nextChar = reader.read();
                            if (nextChar == '"') {
                                //This is an escaped quote, append the double quote
                                field.append('"');
                                fieldHasData = true;
                                break;
                            } else if (nextChar == -1) {
                                //End of file after closing quote
                                //Add the field to the row and return
                                row.add(field.toString());
                                state = State.DEFAULT; //Update the state
                                return row.toArray(new String[0]);
                            }
                                else {
                                //If quote is not followed by a quote (not escaped), handle as an end quote
                                pushedBackChar = nextChar; //Capture the character you read
                                state = State.END_QUOTE;
                            }
                            
                            break;
                         
                         //Preserve newlines inside quotes
                        case '\n': 
                            field.append('\n'); 
                            break;
                         
                        //Preserve \r inside quotes
                        case '\r':
                        	field.append('\r'); //Preserve standalone CR inside quotes
                            break;
                            
                		default:
                            //Any regular character inside quotes should be added to the field (including /r and /n)
                            field.append(character);
                            fieldHasData = true;
                            break;
                    }
                    break;
                
                //End quote state, handling closing quote cases (already on next char after end quote because we saved pushedBackChar)
                case END_QUOTE: 
                	//Create switch machine for all possible characters
                    switch (character) {
                    
                    	//Comma after a quoted field means end of field
                        case ',': 
                            row.add(field.toString()); //Store the completed field
                            field.setLength(0); //Reset field builder
                            fieldHasData = false; //Reset tracking
                            state = State.DEFAULT; //Return to default state
                            break;
                            
                        //Newline after a quoted field ends the row    
                        case '\n': 
                            row.add(field.toString()); //Store last field
                            state = State.DEFAULT; //Return to default state
                            return row.toArray(new String[0]); //Return the row
                            
                        //Handle CRLF (\r\n) correctly
                        case '\r': 
                        	//Check for CRLF
                            int nextCharAfterQuote = reader.read();
                            if (nextCharAfterQuote == '\n') {
                                //CRLF, add the field and return the row
                                row.add(field.toString());
                                state = State.DEFAULT; //Return to default state
                                return row.toArray(new String[0]);
                            } else if (nextCharAfterQuote == -1) {
                                //End of file after a CR
                                row.add(field.toString());
                                state = State.DEFAULT; //Return to default state
                                return row.toArray(new String[0]);  
                            }
                            else {
                                //Standalone CR detected - throw exception
                                throw new CSVFormatException("Standalone CR detected", 0, 0, row.size(), field.length());
                            }
                       
                        //If some other character, throw an exception
                        default:
                        	throw new CSVFormatException("Character after end quote must be comma, newline, or another quote", 0, 0, row.size(), field.length()); 
                    }
                    break;
            }
        }
        
        //If still inside a quote at end of file, it's an error
        if (state == State.IN_QUOTE) { 
        	throw new CSVFormatException("Unclosed quote at end of file", 0, 0, row.size(), field.length());
        }
        
        //If we reached END_QUOTE state but didn't get a valid terminating character, it's an error
        if (state == State.END_QUOTE) {
            throw new CSVFormatException("Quote incorrectly terminated at end of file", 0, 0, row.size(), field.length());
        }
        
        //Check for unescaped quotes in the field data
        if (state == State.DEFAULT && field.indexOf("\"") != -1) {
        	throw new CSVFormatException("Unescaped quote in the field data", 0, 0, row.size(), field.length());
        }
        
        //If the field still has data, add the last field to the row
        if (fieldHasData || field.length() > 0) {
            row.add(field.toString()); 
        }
        
        //Return null at end of file if no characters were read for this row
        //Otherwise return the final row (possibly empty)
        if (!hasReadAnyChar && row.isEmpty()) {
            return null;
        } else {
            return row.isEmpty() ? new String[0] : row.toArray(new String[0]);
        }
    }

    /**
     * Feel free to edit this method for your own testing purposes. As given, it
     * simply processes the CSV file supplied on the command line and prints each
     * resulting array of strings to standard out. Any reading or formatting errors
     * are printed to standard error.
     *
     * @param args command line arguments (1 expected)
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("usage: CSVReader <filename.csv>");
            return;
        }

        /*
         * This block of code demonstrates basic usage of CSVReader's row-oriented API:
         * initialize the reader inside try-with-resources, initialize the CSVReader
         * using the reader, and repeatedly call readRow() until null is encountered. Since
         * CharacterReader implements AutoCloseable, the reader will be automatically
         * closed once the try block is exited.
         */
        var filename = args[0];
        try (var reader = new CharacterReader(filename)) {
            var csvReader = new CSVReader(reader);
            String[] row;
            while ((row = csvReader.readRow()) != null) {
                System.out.println(Arrays.toString(row));
            }
        } catch (IOException | CSVFormatException e) {
           System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
