package itm.oss.splitter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import java.io.ByteArrayInputStream;

public class AppTest {

    // JUnit 5 기본 구조
    @Test  
    void testAppInitialization() {  
        assertTrue(true);  
    }

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    private App createAppWithSimulatedInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        return new App();
    }

    @Test
    @DisplayName("Edge Case: readRequiredLine rejects empty input")
    void testReadRequiredLine_EmptyInput_ShowsError(){
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try{ 
            App app = createAppWithSimulatedInput("\n\nGithero\n");
            String result = app.readRequiredLine("Enter name: ");
            String output = outContent.toString();

            assertEquals("Githero", result);
            assertTrue(output.contains("This field is required and cannot be empty."));
        } finally { 
        System.setIn(originalIn);
        System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("Edge Case: readBigDecimal rejects invalid amounts")
    void testReadBigDecimal_InvalidAmount_ShowsError() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try{
            App app = createAppWithSimulatedInput("abc\n0\n-100\n50.5\n");

            BigDecimal result = app.readBigDecimal("Enter amount: ");
            String output = outContent.toString();

            assertTrue(result.compareTo(new BigDecimal("50.5")) == 0)
            assertTrue(output.contains("Please enter a valid number."));
            assertTrue(output.contains("Amount must be greater than zero"));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }




    @Test
    @DisplayName("Happy Path: addExpenseFlow succeeds with valid input")
    void testAddExpenseFlow_Success() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            String simulatedInput = "2025-10-24\n" + 
                                    "Githero\n" +           
                                    "1000\n" +          
                                    "USD\n" +           
                                    "Githero;Kim\n" +     
                                    "Transport\n" +     
                                    "Taxi ride\n";
            App app = createAppWithSimulatedInput(simulatedInput);
            app.addExpenseFlow();
            String output = outContent.toString(); 
            assertFalse(output.contains("Error:")); 
            assertTrue(output.contains("SUCCESS: Expense added.")); 

        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

}
