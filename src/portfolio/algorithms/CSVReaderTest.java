package portfolio.algorithms;

/*
 ***** Important!  Please Read! *****
 *
 *  - Do NOT remove any of the existing import statements
 *  - Do NOT import additional junit packages 
 *  - You MAY add in other non-junit packages as needed
 * 
 *  - Do NOT remove any of the existing test methods or change their name
 *  - You MAY add additional test methods.  If you do, they should all pass
 * 
 *  - ALL of your assert test cases within each test method MUST pass, otherwise the 
 *        autograder will fail that test method
 *  - You MUST write the require number of assert test cases in each test method, 
 *        otherwise the autograder will fail that test method
 *  - You MAY write more than the required number of assert test cases as long as they all pass
 * 
 *  - All of your assert test cases within a method must be related to the method they are meant to test
 *  - All of your assert test cases within a method must be distinct and non-trivial
 *  - Your test cases should reflect the method requirements in the homework instruction specification
 * 
 *  - Your assert test cases will be reviewed by the course instructors and they may take off
 *        points if your assert test cases to do not meet the requirements
 */

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.io.StringReader;

class CSVReaderTest {
	
    private CSVReader csvReader;
    private StringReader stringReader;
    private CSVReader csvReaderEasyFile1;
    private CSVReader csvReaderEasyFile2;
    private CSVReader csvReaderEasyFile3;
    private CSVReader csvReaderMediumFile1;

	@BeforeEach
	public void setUp() throws Exception {
		// Do not delete this method! You can leave it empty if not used.
		// TODO Write code to set up your test
        csvReaderEasyFile1 = new CSVReader(new CharacterReader("src/portfolio/algorithms/easy1.csv"));
        csvReaderEasyFile2 = new CSVReader(new CharacterReader("src/portfolio/algorithms/easy2.csv"));
        csvReaderEasyFile3 = new CSVReader(new CharacterReader("src/portfolio/algorithms/easy3.csv"));
        csvReaderMediumFile1 = new CSVReader(new CharacterReader("src/portfolio/algorithms/medium1.csv"));
	}

	@BeforeAll
	public static void setUpAll() throws Exception {
		// Do not delete this method! You can leave it empty if not used.
		// TODO Write code to set up your test
	}
	
	@Test
	void testBasic() throws IOException, CSVFormatException {
		// TODO Write at least 5 test cases with assert statements. All cases must pass
		// These tests should check basic functionality of readRow()
		
		//Test with an example string
        String csvData = "a,b,c\n1,2,3\nhello,world,test\nfoo,bar,baz\n123,456,789";
        StringReader stringReader = new StringReader(csvData);
        CSVReader csvReader = new CSVReader(new CharacterReader(stringReader));
        assertArrayEquals(new String[]{"a", "b", "c"}, csvReader.readRow());
        assertArrayEquals(new String[]{"1", "2", "3"}, csvReader.readRow());
        assertArrayEquals(new String[]{"hello", "world", "test"}, csvReader.readRow());
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, csvReader.readRow());
        assertArrayEquals(new String[]{"123", "456", "789"}, csvReader.readRow());
        
        //Newline inside quotes 
        stringReader = new StringReader("\"aaa\n\",bbb");
        CSVReader csvReader1 = new CSVReader(new CharacterReader(stringReader));
        assertArrayEquals(new String[]{"aaa\n", "bbb"}, csvReader1.readRow());
        
        //Newline inside quotes 
        stringReader = new StringReader("\"aaa\r\n\",bbb\n");
        CSVReader csvReader2 = new CSVReader(new CharacterReader(stringReader));
        assertArrayEquals(new String[]{"aaa\r\n", "bbb"}, csvReader2.readRow());
        
        //String that ends with newline 
        stringReader = new StringReader("aaa,bbb\n");
        CSVReader csvReader3 = new CSVReader(new CharacterReader(stringReader));
        assertArrayEquals(new String[]{"aaa", "bbb"}, csvReader3.readRow());
        
        //Escaped double quote in field surrounded by quotes
        stringReader = new StringReader("\"aaa\"\"aaa\",bbb");
        CSVReader csvReader4 = new CSVReader(new CharacterReader(stringReader));
        assertArrayEquals(new String[]{"aaa\"aaa", "bbb"}, csvReader4.readRow());
        
        //Test with the given easy1 file
        csvReaderEasyFile1 = new CSVReader(new CharacterReader("src/portfolio/algorithms/easy1.csv"));
        assertArrayEquals(new String[]{"name", "age"}, csvReaderEasyFile1.readRow());
        assertArrayEquals(new String[]{"hannah", "20"}, csvReaderEasyFile1.readRow());
        assertArrayEquals(new String[]{"harry", "40"}, csvReaderEasyFile1.readRow());
        assertArrayEquals(new String[]{"mary", "56"}, csvReaderEasyFile1.readRow());
        assertArrayEquals(new String[]{"john", "18"}, csvReaderEasyFile1.readRow());
        assertArrayEquals(new String[]{"yule", "25"}, csvReaderEasyFile1.readRow());
        assertArrayEquals(new String[]{"jiexi", "32"}, csvReaderEasyFile1.readRow());
        assertArrayEquals(new String[]{"lauren", "35"}, csvReaderEasyFile1.readRow());
        assertNull(csvReaderEasyFile1.readRow()); // Ensure EOF is handled correctly
        
        //Test with the given easy2 file
        csvReaderEasyFile2 = new CSVReader(new CharacterReader("src/portfolio/algorithms/easy2.csv"));
        //Validate header row
        assertArrayEquals(new String[]{"from", "to", "sequence_number", "message"}, csvReaderEasyFile2.readRow());
        //Validate normal rows
        assertArrayEquals(new String[]{"alice", "bob", "1", "hello"}, csvReaderEasyFile2.readRow());
        assertArrayEquals(new String[]{"bob", "alice", "2", "hello to you too"}, csvReaderEasyFile2.readRow());
        
        //Test with the given medium1 file
        csvReaderMediumFile1 = new CSVReader(new CharacterReader("src/portfolio/algorithms/medium1.csv"));
        assertArrayEquals(new String[]{"from", "to", "sequence_number", "message"}, csvReaderMediumFile1.readRow());
        csvReaderMediumFile1.readRow();
        assertArrayEquals(new String[]{"bob", "alice", "6", "only if they say something interesting"}, csvReaderMediumFile1.readRow());
        
    }


	@Test
	void testLineEnd() throws IOException, CSVFormatException {
		// TODO Write at least 5 test cases with assert statements. All cases must pass
		// These tests should check different line ending cases
		
		//Test an example string
        String csvData = "a,b,c\r\n1,2,3\nhello,world,test\r\nfoo,bar,baz\r\n123,456,789";
        StringReader stringReader = new StringReader(csvData);
        CSVReader csvReader = new CSVReader(new CharacterReader(stringReader));
        assertArrayEquals(new String[]{"a", "b", "c"}, csvReader.readRow());
        assertArrayEquals(new String[]{"1", "2", "3"}, csvReader.readRow());
        assertArrayEquals(new String[]{"hello", "world", "test"}, csvReader.readRow());
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, csvReader.readRow());
        assertArrayEquals(new String[]{"123", "456", "789"}, csvReader.readRow());
        
        //Test with the given easy2 file
        csvReaderEasyFile2 = new CSVReader(new CharacterReader("src/portfolio/algorithms/easy2.csv"));

        csvReaderEasyFile2.readRow();
        csvReaderEasyFile2.readRow();
        csvReaderEasyFile2.readRow();
        
        //Validate a multiline field
        assertArrayEquals(new String[]{"alice", "bob", "3", "do you like newlines?\r\nlike this?"}, csvReaderEasyFile2.readRow());
        
        //Validate last row
        assertArrayEquals(new String[]{"bob", "alice", "4", "meh"}, csvReaderEasyFile2.readRow());

        //Ensure EOF is reached
        assertNull(csvReaderEasyFile2.readRow());
        
	}

	@Test
	void testPathological(){
		// TODO Write at least 5 test cases with assert statements. All cases must pass
		// These tests should check that readRow() correctly handles exceptions
		
        //Unclosed quote at the end 
        stringReader = new StringReader("a,\"b,c");
        csvReader = new CSVReader(new CharacterReader(stringReader)); 
        assertThrows(CSVFormatException.class, () -> csvReader.readRow(), "A CSV with an unclosed quote should throw CSVFormatException");
        
        //Unclosed quote at the end
        stringReader = new StringReader("\"a\n,b,ccc");
        csvReader = new CSVReader(new CharacterReader(stringReader));
        assertThrows(CSVFormatException.class, () -> csvReader.readRow(), "A CSV with unclosed quote should throw CSVFormatException");
        
        //A standalone \r should throw an exception
        stringReader = new StringReader("aaa, bbb\r");
        csvReader = new CSVReader(new CharacterReader(stringReader));
        assertThrows(CSVFormatException.class, () -> csvReader.readRow(), "A standalone CR should throw CSVFormatException");
        
        //A field that's not fully encompassed by quotes should throw an exception
        stringReader = new StringReader("\"aa\"aa, bbb");
        csvReader = new CSVReader(new CharacterReader(stringReader));
        assertThrows(CSVFormatException.class, () -> csvReader.readRow(), "A field not fully encompassed by quotes should throw CSVFormatException");
        
        //Double quote can only appear in field surrounded by double quotes
        stringReader = new StringReader("abc\"de, bbb");
        csvReader = new CSVReader(new CharacterReader(stringReader));
        assertThrows(CSVFormatException.class, () -> csvReader.readRow(), "A double quote appearing in a field not surrounded by double quotes should throw CSVFormatException");
        
        //Incorrect escaping with extra quotes
        stringReader = new StringReader("wrong escape usage \"\"a,b\"\"");
        csvReader = new CSVReader(new CharacterReader(stringReader));
        assertThrows(CSVFormatException.class, () -> csvReader.readRow(), "Incorrect escaping of quotes should throw CSVFormatException");
        
        //Unexpected end quote
        stringReader = new StringReader("random end quote,a\"");
        csvReader = new CSVReader(new CharacterReader(stringReader));
        assertThrows(CSVFormatException.class, () -> csvReader.readRow(), "An extra end quote should throw CSVFormatException");
    }

	@Test
	void testData() throws IOException, CSVFormatException {
		// TODO Write at least 5 test cases with assert statements. All cases must pass
		// These tests should check different corner cases that can occur
		
        String csvData = "\"long field with spaces\",data\n\"text with, comma\",next\n\"quoted \"\"text\"\"\",end\n\";;special;;chars;;\",test";
        StringReader stringReader = new StringReader(csvData);
        CSVReader csvReader = new CSVReader(new CharacterReader(stringReader));
        assertArrayEquals(new String[]{"long field with spaces", "data"}, csvReader.readRow());
        assertArrayEquals(new String[]{"text with, comma", "next"}, csvReader.readRow());
        assertArrayEquals(new String[]{"quoted \"text\"", "end"}, csvReader.readRow());
        assertArrayEquals(new String[]{";;special;;chars;;", "test"}, csvReader.readRow());
        
        //Test with the given easy3 file
        csvReaderEasyFile3 = new CSVReader(new CharacterReader("src/portfolio/algorithms/easy3.csv"));
        //Validate a row containing escaped double quotes
        assertArrayEquals(new String[]{"example of using \" in a field", "1"}, csvReaderEasyFile3.readRow());
        
        //Test with the given medium1 file
        csvReaderMediumFile1 = new CSVReader(new CharacterReader("src/portfolio/algorithms/medium1.csv"));
        //Validate a row containing escaped double quotes
        csvReaderMediumFile1.readRow();
        assertArrayEquals(new String[]{"alice", "bob", "5", "Thoughts on so-called \"quotes\"?"}, csvReaderMediumFile1.readRow());
	}

	@Test
	void testSpeed() throws IOException, CSVFormatException {
		// TODO 
		// There are *** NO *** required tests for this method and they will *** NOT *** be checked by the autograder
		// However, it is good practice to consider how your code will do on large inputs
		// Consider cases that might result in inefficient run times
		// Also consider ways to measure your run time and determine if it can be improved
		
        StringBuilder largeCSV = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            largeCSV.append("data").append(i).append(",value").append(i).append("\n");
        }
        StringReader stringReader = new StringReader(largeCSV.toString());
        CSVReader csvReader = new CSVReader(new CharacterReader(stringReader));
        for (int i = 0; i < 10000; i++) {
            assertArrayEquals(new String[]{"data" + i, "value" + i}, csvReader.readRow());
        }
    }
}



