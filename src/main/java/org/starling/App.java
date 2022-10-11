package org.starling;

import org.starling.exceptions.APIException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;



public class App {


    public static void main( String[] args ) throws IOException, InterruptedException, URISyntaxException, APIException {

        SavingsGoalSaver savingsGoalSaver = new SavingsGoalSaver();

        // get main customer account
        String accountUid = savingsGoalSaver.getCustomerMainAccount().getAccountUid();

        // get the last one-week's account roundup total
        double roundupTotal = savingsGoalSaver.getAccountOneWeekRoundup(accountUid);

        // get savings goal associated with this account
        String savingsGoalUid = savingsGoalSaver.getSavingsGoal(accountUid);

        // save into savings goal
        UUID transferUid = UUID.randomUUID();
        boolean moneySaved = savingsGoalSaver.addMoneyToSavingsGoal(roundupTotal, accountUid, savingsGoalUid, transferUid.toString());

        System.out.println("roundupTotal is ......" + roundupTotal);
        System.out.println("savingsGoalUid is ...... " + savingsGoalUid);
        System.out.println("moneySaved .... " + moneySaved);

    }
//
//    static public CustomerAccount getCustomerMainAccount() throws IOException, URISyntaxException, InterruptedException {
//        request = HttpRequest.newBuilder()
//                .uri(new URI("https://api-sandbox.starlingbank.com/api/v2/accounts"))
//                .GET()
//                .header("Authorization", "Bearer " + ACCESS_TOKEN)
//                .header("Accept", "application/json")
//                .header("User-Agent", "Elizabeth Fatona")
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        List<CustomerAccount> accounts = new ArrayList<>();
//
//        if (response.statusCode() == 200) {
//            Map<String, Object> accountsMap = gson.fromJson(response.body(), new TypeToken<Map<String, Object>>() {
//            }.getType());
//            accounts = mapper.readValue(gson.toJson(accountsMap.get("accounts")), new TypeReference<>() {
//            });
//        }
//
//        return accounts.get(0);
//    }
//
//    static public double getAccountOneWeekRoundup(String accountUid) throws InterruptedException, IOException, URISyntaxException{
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
//        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(new URI("https://api-sandbox.starlingbank.com/api/v2/feed/account/"+ accountUid + "/settled-transactions-between?minTransactionTimestamp="+ simpleDateFormat.format(getADate(-7)) + "&maxTransactionTimestamp=" + simpleDateFormat.format(getTodaysDate())))
//                .GET()
//                .header("Authorization", "Bearer "+ ACCESS_TOKEN)
//                .header("Accept", "application/json")
//                .header("User-Agent", "Elizabeth Fatona")
//                .build();
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        double totalRoundup = 0.00;
//        if(response.statusCode() == 200) {
//            Map<String, Object> feedItemsMap = gson.fromJson(response.body(), new TypeToken<Map<String, Object>>() {
//            }.getType());
//
//            double transactionAmount;
//            double transactionRoundup;
//
//            try {
//                ArrayNode arrayNode = (ArrayNode) mapper.readTree(gson.toJson(feedItemsMap.get("feedItems")));
//                if (arrayNode != null && arrayNode.isArray()) {
//                    for (JsonNode node : arrayNode) {
//                        JsonNode amountNode = node.get("amount");
//                        JsonNode minorUnitsChild = amountNode.get("minorUnits");
//                        transactionAmount = minorUnitsChild.asDouble() / 100;
//                        transactionRoundup = Math.ceil(transactionAmount) - transactionAmount;
//                        totalRoundup += Math.round(transactionRoundup * 100.0) / 100.0;
//                    }
//                } else {
//                    System.out.println("No feed items found for this account");
//                }
//            } catch (IOException e) {
//                e.printStackTrace(new PrintStream("IOException caught"));
//            }
//            totalRoundup = Math.round(totalRoundup * 100.0) / 100.0;
//        }
//        return totalRoundup;
//    }
//
//
//    static public String getSavingsGoal(String accountUid) throws InterruptedException, IOException, URISyntaxException{
//        request = HttpRequest.newBuilder()
//                .uri(new URI("https://api-sandbox.starlingbank.com/api/v2/account/"+ accountUid + "/savings-goals"))
//                .GET()
//                .header("Authorization", "Bearer " + ACCESS_TOKEN)
//                .header("Accept", "application/json")
//                .header("User-Agent", "Elizabeth Fatona")
//                .build();
//        response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        String savingsGoalID = "";
//
//        if(response.statusCode() == 200) {
//            Map<String, Object> savingsGoalsMap = gson.fromJson(response.body(), new TypeToken<Map<String, Object>>() {
//            }.getType());
//
//            ArrayNode arrayNode = (ArrayNode) mapper.readTree(gson.toJson(savingsGoalsMap.get("savingsGoalList")));
//            if (arrayNode.isEmpty()) {
//                return createSavingsGoal(accountUid);
//            }
//            savingsGoalID = arrayNode.get(0).get("savingsGoalUid").asText();
//        }
//        return savingsGoalID;
//    }
//
//    static public String createSavingsGoal(String accountUid)throws InterruptedException, IOException, URISyntaxException{
//
//        JSONObject requestJsonObject = new JSONObject();
//        requestJsonObject.put("name", "first_savings_goal");
//        requestJsonObject.put("currency", "GBP");
//
//        String requestJson = "{\n" +
//                "  \"name\": \"first_savings_goal\",\n" +
//                "  \"currency\": \"GBP\"\n" +
//                "}";
//
//        request = HttpRequest.newBuilder()
//                .uri(new URI("https://api-sandbox.starlingbank.com/api/v2/account/"+ accountUid + "/savings-goals"))
//                .header("Authorization", "Bearer " + ACCESS_TOKEN)
//                .header("Accept", "application/json")
//                .header("User-Agent", "Elizabeth Fatona")
//                .header("content-type", "application/json")
//                .PUT(HttpRequest.BodyPublishers.ofString(requestJson))
//                .build();
//        response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//        String savingsGoal = "";
//        if(response.statusCode() == 200) {
//            Map<String, Object> savingsGoalsMap = gson.fromJson(response.body(), new TypeToken<Map<String, Object>>() {
//            }.getType());
//            savingsGoal = savingsGoalsMap.get("savingsGoalUid").toString();
//        }
//
//        return savingsGoal;
//    }
//
//    static public boolean addMoneyToSavingsGoal(double amount, String accountUid, String savingsGoalUid, String transferUid)throws InterruptedException, IOException, URISyntaxException {
//
//        // create a transfer UID with savings goals first 20 digits and last 9 digits
//        Map<String, String> amountJsonObject = new HashMap<>();
//        amountJsonObject.put("currency", "GBP");
//        amountJsonObject.put("minorUnits", String.valueOf((long)(amount * 100)));
//
//        Map<String, Object> amountMap = new HashMap<>();
//        amountMap.put("amount", amountJsonObject);
//
//        request = HttpRequest.newBuilder()
//                .uri(new URI("https://api-sandbox.starlingbank.com/api/v2/account/" + accountUid + "/savings-goals/" + savingsGoalUid + "/add-money/" + transferUid))
//                //  .PUT(HttpRequest.BodyPublishers.ofString(gson.fromJson(amountMap)))
//                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(amountMap)))
//                .header("Authorization", "Bearer " + ACCESS_TOKEN)
//                .header("Accept", "application/json")
//                .header("User-Agent", "Elizabeth Fatona")
//                .header("Content-Type", "application/json")
//                .build();
//        response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        boolean result = false;
//        if(response.statusCode() == 200) {
//            JSONObject jsonObject = new JSONObject(response.body());
//
//            Iterator<String> keys;
//            keys = jsonObject.keys();
//            while (keys.hasNext()) {
//                String key = keys.next();
//                if (key.equals("success")) {
//                    result = (boolean) jsonObject.get("success");
//                }
//            }
//        }
//
//        return result;
//    }
//
//
//    static private Date getADate(int daysCount){
//
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, daysCount + 1);
//        return cal.getTime();
//    }
//
//    static private Date getTodaysDate(){
//        Calendar cal = Calendar.getInstance();
//        return cal.getTime();
//    }

}
