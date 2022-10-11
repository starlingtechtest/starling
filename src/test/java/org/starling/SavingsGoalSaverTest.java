package org.starling;

import junit.framework.Assert;
import org.starling.exceptions.APIException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;


public class SavingsGoalSaverTest {
    SavingsGoalSaver savingsGoalSaver;

    @BeforeEach
    public void setUp() {
        savingsGoalSaver = Mockito.mock(SavingsGoalSaver.class);
    }

    @Test
    public void testGetCustomerRequestSuccess() throws IOException, URISyntaxException, InterruptedException, APIException  {

        CustomerAccount customerAccount = new CustomerAccount();
        customerAccount.setAccountUid("3da3a57a-482c-11ed-b878-0242ac120002");
        customerAccount.setAccountType("Primary");
        customerAccount.setCreatedAt("");
        customerAccount.setCurrency("GBP");
        customerAccount.setName("Personal");
        customerAccount.setDefaultCategory("Main");
        Mockito.when(savingsGoalSaver.getCustomerMainAccount()).thenReturn (customerAccount);

        CustomerAccount result = savingsGoalSaver.getCustomerMainAccount();

        Assert.assertEquals("3da3a57a-482c-11ed-b878-0242ac120002", result.getAccountUid());
        Assert.assertEquals("Primary", result.getAccountType());
        Assert.assertEquals("3da3a57a-482c-11ed-b878-0242ac120002", result.getAccountUid());
        Assert.assertEquals("GBP", result.getCurrency());
        Assert.assertEquals("Personal", result.getName());
        Assert.assertEquals("Main", result.getDefaultCategory());
    }

    @Test
    public void testGetCustomerRequestFail() throws IOException, URISyntaxException, InterruptedException, APIException  {

        int statusCodeServerError = 500;
        Mockito.when(savingsGoalSaver.getCustomerMainAccount()).thenThrow (new APIException("Failure", statusCodeServerError));
        APIException thrown = assertThrows(
                APIException.class,
                () -> savingsGoalSaver.getCustomerMainAccount()
        );
        Assert.assertTrue(thrown.getMessage().contains("Failure"));
        Assert.assertEquals(thrown.getStatusCode(), statusCodeServerError);
    }

    @Test
    public void testGetAccountOneWeekRoundupSuccess() throws IOException, URISyntaxException, InterruptedException, APIException {

        String accoundUid = "3da3a57a-482c-11ed-b878-0242ac120002";
        Mockito.when(savingsGoalSaver.getAccountOneWeekRoundup(anyString())).thenReturn (130.77);

        double result = savingsGoalSaver.getAccountOneWeekRoundup(accoundUid);

        Assert.assertEquals(result,130.77);
    }

    @Test
    public void testGetAccountOneWeekRoundupFail() throws IOException, URISyntaxException, InterruptedException, APIException {

        String accoundUid = "3da3a57a-482c-11ed-b878-0242ac120002";
        int statusCodeServerError = 500;
        Mockito.when(savingsGoalSaver.getAccountOneWeekRoundup(anyString())).thenThrow (new APIException("Failure", statusCodeServerError));

        APIException thrown = assertThrows(
                APIException.class,
                () -> savingsGoalSaver.getAccountOneWeekRoundup(accoundUid)
        );
        Assert.assertTrue(thrown.getMessage().contains("Failure"));
        Assert.assertEquals(thrown.getStatusCode(), statusCodeServerError);
    }

    @Test
    public void testGetSavingsGoalSuccess() throws InterruptedException, IOException, URISyntaxException, APIException  {
        String accoundUid = "3da3a57a-482c-11ed-b878-0242ac120002";
        String savingsGoalUid = "2cfee9ea-556e-4573-884b-3cf642ce29ce";
        Mockito.when(savingsGoalSaver.getSavingsGoal(anyString())).thenReturn(savingsGoalUid);

        String result = savingsGoalSaver.getSavingsGoal(accoundUid);

        Assert.assertEquals(result, savingsGoalUid);
    }

    @Test
    public void testGetSavingsGoalFail() throws InterruptedException, IOException, URISyntaxException, APIException  {
        String accoundUid = "3da3a57a-482c-11ed-b878-0242ac120002";
        String savingsGoalUid = "2cfee9ea-556e-4573-884b-3cf642ce29ce";
        int statusCodeServerError = 500;
        Mockito.when(savingsGoalSaver.getSavingsGoal(anyString())).thenThrow(new APIException("Failure",statusCodeServerError));

        APIException thrown = assertThrows(
                APIException.class,
                () -> savingsGoalSaver.getSavingsGoal(accoundUid)
        );
        Assert.assertTrue(thrown.getMessage().contains("Failure"));
        Assert.assertEquals(thrown.getStatusCode(), statusCodeServerError);
    }

    @Test
    public void testCreateSavingsGoalSuccess() throws InterruptedException, IOException, URISyntaxException, APIException  {
        String accoundUid = "3da3a57a-482c-11ed-b878-0242ac120002";
        String savingsGoalUid = "2cfee9ea-556e-4573-884b-3cf642ce29ce";
        Mockito.when(savingsGoalSaver.getSavingsGoal(anyString())).thenReturn(savingsGoalUid);

        String result = savingsGoalSaver.getSavingsGoal(accoundUid);

        Assert.assertEquals(result, savingsGoalUid);
    }

    @Test
    public void testCreateSavingsGoalFail() throws InterruptedException, IOException, URISyntaxException, APIException  {
        String accoundUid = "3da3a57a-482c-11ed-b878-0242ac120002";
        int statusCodeServerError = 500;
        Mockito.when(savingsGoalSaver.createSavingsGoal(anyString())).thenThrow(new APIException("Failure", statusCodeServerError));

        APIException thrown = assertThrows(
                APIException.class,
                () -> savingsGoalSaver.createSavingsGoal(accoundUid)
        );
        Assert.assertTrue(thrown.getMessage().contains("Failure"));
        Assert.assertEquals(thrown.getStatusCode(), statusCodeServerError);
    }

    @Test
    public void testAddMoneyToSavingsGoalSuccess() throws InterruptedException, IOException, URISyntaxException, APIException  {
        double amount = 28.77;
        String accountUid = "3da3a57a-482c-11ed-b878-0242ac120002";
        String savingsGoalUid = "2cfee9ea-556e-4573-884b-3cf642ce29ce";
        String transferUid = "8640c9bd-215e-48e0-b3f4-110d364c7383";
        Mockito.when(savingsGoalSaver.addMoneyToSavingsGoal(anyDouble(),anyString(), anyString(), anyString())).thenReturn(true);

        boolean result = savingsGoalSaver.addMoneyToSavingsGoal(amount, accountUid, savingsGoalUid, transferUid);

        Assert.assertTrue(result);
    }

    @Test
    public void testAddMoneyToSavingsGoalFail() throws InterruptedException, IOException, URISyntaxException, APIException  {
        double amount = 28.77;
        String accountUid = "3da3a57a-482c-11ed-b878-0242ac120002";
        String savingsGoalUid = "2cfee9ea-556e-4573-884b-3cf642ce29ce";
        String transferUid = "8640c9bd-215e-48e0-b3f4-110d364c7383";
        int statusCodeServerError = 500;
        Mockito.when(savingsGoalSaver.addMoneyToSavingsGoal(anyDouble(),anyString(), anyString(), anyString())).thenThrow(new APIException("Failure", statusCodeServerError));

        APIException thrown = assertThrows(
                APIException.class,
                () -> savingsGoalSaver.addMoneyToSavingsGoal(amount, accountUid,savingsGoalUid, transferUid)
        );
        Assert.assertTrue(thrown.getMessage().contains("Failure"));
        Assert.assertEquals(thrown.getStatusCode(), statusCodeServerError);
    }


}
