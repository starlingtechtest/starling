package org.starling;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.starling.exceptions.APIException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.*;

public class SavingsGoalSaver {
    private HttpRequest request;
    private HttpResponse<String> response;
    private final Gson gson = new Gson();
    private final ObjectMapper mapper = new ObjectMapper();
    private final HttpClient client = HttpClient.newHttpClient();
    public final String ACCESS_TOKEN = "Bearer eyJhbGciOiJQUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAA_31Uy5LaMBD8lS2fV1sYv7BvueUH8gGj0RhUyJJLktlspfLvkS0ZY5bKhaK759WagT-ZdC7rMhglEzSYD-fBKqnPHPT1A82QvWdu4iGihKbNwwcTJyxYyauStVD2rK-54NTnJ451CKbfY9bldV1VRZO39XsmwUeirA7NTACimbT_aZQg-0uKULs4NkdqoWDtsa5ZWfQt4wfIGVYFVlCfmr6kUNubK-mYEbq1h_aADMojsVKEby1wzogj9GWd9xUVISPY-oFIzsUsxLJpCkR2rHtg5eFwYLzMcybaBhGqik7QzIbRjDQ_SpyUoTKORGcJxNvKXZbxmYaBXgr-a3wSpCDtZS_J7nklnd8xCQhhw-AdCenvICreA14Gukdu-NNKT28w-Yux0oU1MqmFvEkxgYrBHBRoTKMhWMHQaG-Nio1mJmlG99IO4KXRzIQ9T1q4u-Tu3VcQW-PkvBlWizSATIUVhUH0uYNxVF93tEQNoAV46gQpCiVWmDR7JT8bGS31ZCnM7v4nxTGiNipACi_g6WwXH4-J38WUShYvsLobyEOYBjoMcFETXkyN8EW0ShEkExFsQUwOcE6eEhE6W72uJcY_yN6CdoDb1IFmfFLXbt0ubdQ2QcTbEBGvBeYbCfc2SL_VVAbDEA8VFoKZ-Uie2ZRlTS_Vail63FFLlCUkOfodcHspPviyPAsqiQ5uYaeOnc021o5L5nbct8z4fqHwqxKb-KLWJsaieCExKRIsvV-iyfvgdxoTHGH9TYU_0OW2mbHiof2eXfvu2Rf5zHzqO-9pWSC62zM1ij5RE3dow-POd7N2eeSWqMfjWvb3fG3Z339T0oZBGQYAAA.nWZ1-nqcQNA4AdMbV18bMjCunE0_YRvVUvNonayTFJ9TrfwAeuQVLYKsCeHRYC3RsaBArDwXSiHQQDH8p17ofJv2Uv0wgE-plPapsDHABiPqi9SPH5ZUk7hJ3JN-OvdJ8vYsjSH023AKxk3_Yso8j3PW3MFUvBmw3MMqoXDik75OdlbjRNgsdhziewBmC-M08wGs2VOjPWtwLGyPc99BUW7G1Ju8S2Ea0nonicSTv_0OjfUz7eV5CGiDbyWhQQrZ-WwP3y-nepORNJ9-HQZNgqEIt1MtS2nz9ouEIa999yjWSD7RVxnkUftd4v5syeUyx0hB75pTx8POLRdCo3eHoYVrLMb3lngBBrEa5W4Aso1lV0wNeWVtlIymLMp8dRUWrA1vQtQwSWF79_KqT7v0zaGljc4WsaHsCbTvhUQpNV-sPTkxkNpp40pCmxVMvNdRPMN5cmzLa4yxfrMHa8YFlrRLjlgJ6o-lE2xuHq9uy0PWJdDXIDl6W4t0Up5Wf2FbFNUn4DrQFaViKT0zh1_Z94sOha3HslPc90i1RWy0wA99nl7xcGh_XwQAR9JbcwDlLlrtYz_ckL_A2ddWXi5knQzwsecUnrStVLhAJ7C4Z0clGkI_DOVl-YsdsE9Cj1uuvfBwkUkmlDtZefZDv9qNjDN0P8UMgz6bz-agQxFnfn0";
    public final String JSON_DATA_TYPE = "application/json";
    public final String USER_AGENT = "Elizabeth Fatona";

    public final String DATE_FORMAT_STARLING_BANK = "yyyy-MM-dd'T'HH:mm:ss'.000Z'";
    public final String TIMEZONE_UK = "GMT";

    private HttpRequest buildGETRequest(String url) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .header("Authorization", ACCESS_TOKEN)
                .header("Accept", JSON_DATA_TYPE)
                .header("User-Agent", USER_AGENT)
                .build();
    }

    private HttpRequest buildPUTRequest(String url, String requestBodyJsonString) throws URISyntaxException{
        return HttpRequest.newBuilder()
                .uri(new URI(url))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBodyJsonString))
                .header("Authorization", ACCESS_TOKEN)
                .header("Accept", JSON_DATA_TYPE)
                .header("User-Agent", USER_AGENT)
                .header("Content-Type", JSON_DATA_TYPE)
                .build();
    }

    public CustomerAccount getCustomerMainAccount() throws IOException, URISyntaxException, InterruptedException, APIException {
        String getAccountsUrl = "https://api-sandbox.starlingbank.com/api/v2/accounts";
        request = buildGETRequest(getAccountsUrl);
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<CustomerAccount> accounts = new ArrayList<>();

        if (response.statusCode() == 200) {
            Map<String, Object> accountsMap = gson.fromJson(response.body(), new TypeToken<Map<String, Object>>() {
            }.getType());
            accounts = mapper.readValue(gson.toJson(accountsMap.get("accounts")), new TypeReference<>() {
            });
        }else{
            throw new APIException("response status code is: " + response.statusCode(), response.statusCode());
        }
        return accounts.get(0);
    }

    public double getAccountOneWeekRoundup(String accountUid) throws InterruptedException, IOException, URISyntaxException, APIException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_STARLING_BANK);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UK));

        String getAccountRoundupUrl = "https://api-sandbox.starlingbank.com/api/v2/feed/account/" + accountUid + "/settled-transactions-between?minTransactionTimestamp=" + simpleDateFormat.format(getADate(-7)) + "&maxTransactionTimestamp=" + simpleDateFormat.format(getTodaysDate());
        request = buildGETRequest(getAccountRoundupUrl);
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        double totalRoundup = 0.00;
        if (response.statusCode() == 200) {
            Map<String, Object> feedItemsMap = gson.fromJson(response.body(), new TypeToken<Map<String, Object>>() {
            }.getType());

            double transactionAmount;
            double transactionRoundup;

            try {
                ArrayNode arrayNode = (ArrayNode) mapper.readTree(gson.toJson(feedItemsMap.get("feedItems")));
                if (arrayNode != null && arrayNode.isArray()) {
                    for (JsonNode node : arrayNode) {
                        JsonNode amountNode = node.get("amount");
                        JsonNode minorUnitsChild = amountNode.get("minorUnits");
                        transactionAmount = minorUnitsChild.asDouble() / 100;
                        transactionRoundup = Math.ceil(transactionAmount) - transactionAmount;
                        totalRoundup += Math.round(transactionRoundup * 100.0) / 100.0; // to get 2 decimal places
                    }
                }
            } catch (IOException e) {
                throw new APIException("Root exception is : " + e.getMessage(), response.statusCode());
            }

            totalRoundup = Math.round(totalRoundup * 100.0) / 100.0; // to get 2 decimal places
        }else{
            throw new APIException("response status code is: " + response.statusCode(), response.statusCode());
        }
        return totalRoundup;
    }


    public String getSavingsGoal(String accountUid) throws InterruptedException, IOException, URISyntaxException, APIException {

        String getSavingsGoalUrl = "https://api-sandbox.starlingbank.com/api/v2/account/" + accountUid + "/savings-goals";

        request = buildGETRequest(getSavingsGoalUrl);
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String savingsGoalID = "";

        if (response.statusCode() == 200) {
            Map<String, Object> savingsGoalsMap = gson.fromJson(response.body(), new TypeToken<Map<String, Object>>() {
            }.getType());

            ArrayNode arrayNode = (ArrayNode) mapper.readTree(gson.toJson(savingsGoalsMap.get("savingsGoalList")));

            // is there is currently no savings goals associated with this account, then create one
            if (arrayNode.isEmpty()) {
                return createSavingsGoal(accountUid);
            }
            savingsGoalID = arrayNode.get(0).get("savingsGoalUid").asText();
        }else{
            throw new APIException("response status code is: " + response.statusCode(), response.statusCode());
        }
        return savingsGoalID;
    }

    public String createSavingsGoal(String accountUid) throws InterruptedException, IOException, URISyntaxException, APIException {

        String requestJson = "{\n" +
                "  \"name\": \"first_savings_goal\",\n" +
                "  \"currency\": \"GBP\"\n" +
                "}";

        String createSavingsGoalUrl ="https://api-sandbox.starlingbank.com/api/v2/account/" + accountUid + "/savings-goals";
        request = buildPUTRequest(createSavingsGoalUrl, requestJson);
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String savingsGoalID = "";
        if (response.statusCode() == 200) {
            Map<String, Object> savingsGoalsMap = gson.fromJson(response.body(), new TypeToken<Map<String, Object>>() {
            }.getType());
            savingsGoalID = savingsGoalsMap.get("savingsGoalUid").toString();
        }else{
            throw new APIException("response status code is: " + response.statusCode(), response.statusCode());
        }

        return savingsGoalID;
    }

    public boolean addMoneyToSavingsGoal(double amount, String accountUid, String savingsGoalUid, String transferUid) throws InterruptedException, IOException, URISyntaxException, APIException {

        Map<String, String> amountJsonObject = new HashMap<>();
        amountJsonObject.put("currency", "GBP");
        amountJsonObject.put("minorUnits", String.valueOf((long) (amount * 100)));

        Map<String, Object> amountMap = new HashMap<>();
        amountMap.put("amount", amountJsonObject);

        String addMoneyToSavingsGoalUrl ="https://api-sandbox.starlingbank.com/api/v2/account/" + accountUid + "/savings-goals/" + savingsGoalUid + "/add-money/" + transferUid;

        request = buildPUTRequest(addMoneyToSavingsGoalUrl, gson.toJson(amountMap));
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        boolean result = false;
        if (response.statusCode() == 200) {
            JSONObject jsonObject = new JSONObject(response.body());

            Iterator<String> keys;
            keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                if (key.equals("success")) {
                    result = (boolean) jsonObject.get("success");
                }
            }
        }else{
            throw new APIException("response status code is: " + response.statusCode(), response.statusCode());
        }

        return result;
    }


    private Date getADate(int daysCount) {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daysCount + 1);
        return cal.getTime();
    }

    private Date getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

}
