package hr.algebra.fishingstore.dal.services.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.fishingstore.config.PayPalConfig;
import hr.algebra.fishingstore.exceptions.PayPalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PayPalService {
    private static final String V_2_CHECKOUT_ORDERS = "/v2/checkout/orders";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APP_JSON = "application/json";
    private static final String LINKS = "links";
    private static final String APPROVE = "approve";
    private static final String REL = "rel";
    private static final String HREF = "href";
    private static final String EMPTY_BODY = "{}";
    private static final String CAPTURE = "/capture";
    private static final String COMPLETED = "COMPLETED";
    private static final String STATUS = "status";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String V_1_OAUTH_2_TOKEN = "/v1/oauth2/token";
    private static final String BASIC = "Basic ";
    private static final String FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String GRANT_TYPE_CLIENT_CREDENTIALS = "grant_type=client_credentials";

    private static final String URL_NOT_FOUND = "PayPal approval URL not found";

    private final PayPalConfig payPalConfig;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String createOrder(BigDecimal amount, Long orderId, String returnUrl, String cancelUrl)
            throws IOException, InterruptedException {
        String accessToken = getAccessToken();
        String body = buildOrderBody(amount, orderId, returnUrl, cancelUrl);
        HttpRequest request = buildCreateOrderRequest(accessToken, body);

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode node = objectMapper.readTree(response.body());

        return extractApprovalUrl(node);
    }

    private static String extractApprovalUrl(JsonNode node) {
        for (JsonNode link : node.get(LINKS)) {
            if (APPROVE.equals(link.get(REL).asText()))
                return link.get(HREF).asText();
        }
        throw new PayPalException(URL_NOT_FOUND);
    }

    private HttpRequest buildCreateOrderRequest(String accessToken, String body) {
        return HttpRequest.newBuilder()
                .uri(URI.create(payPalConfig.getBaseUrl() + V_2_CHECKOUT_ORDERS))
                .header(AUTHORIZATION, BEARER + accessToken)
                .header(CONTENT_TYPE, APP_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }

    private static String buildOrderBody(BigDecimal amount, Long orderId, String returnUrl, String cancelUrl) {
        return """
                {
                  "intent": "CAPTURE",
                  "purchase_units": [{
                    "reference_id": "%d",
                    "amount": {
                      "currency_code": "EUR",
                      "value": "%s"
                    }
                  }],
                  "application_context": {
                    "return_url": "%s",
                    "cancel_url": "%s"
                  }
                }
                """.formatted(orderId, amount.toPlainString(), returnUrl, cancelUrl);
    }

    public boolean captureOrder(String paypalOrderId) throws IOException, InterruptedException {
        String accessToken = getAccessToken();
        HttpRequest request = buildCaptureOrderRequest(paypalOrderId, accessToken);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode node = objectMapper.readTree(response.body());
        return COMPLETED.equals(node.get(STATUS).asText());
    }

    private HttpRequest buildCaptureOrderRequest(String paypalOrderId, String accessToken) {
        return HttpRequest.newBuilder()
                .uri(URI.create(payPalConfig.getBaseUrl() + V_2_CHECKOUT_ORDERS + "/" + paypalOrderId + CAPTURE))
                .header(AUTHORIZATION, BEARER + accessToken)
                .header(CONTENT_TYPE, APP_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(EMPTY_BODY))
                .build();
    }

    private String getAccessToken() throws IOException, InterruptedException {
        String credentials = Base64.getEncoder().encodeToString(
                (payPalConfig.getClientId() + ":" + payPalConfig.getClientSecret()).getBytes()
        );

        HttpRequest request = buildAccessTokenRequest(credentials);

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode node = objectMapper.readTree(response.body());
        return node.get(ACCESS_TOKEN).asText();
    }

    private HttpRequest buildAccessTokenRequest(String credentials) {
        return HttpRequest.newBuilder()
                .uri(URI.create(payPalConfig.getBaseUrl() + V_1_OAUTH_2_TOKEN))
                .header(AUTHORIZATION, BASIC + credentials)
                .header(CONTENT_TYPE, FORM_URLENCODED)
                .POST(HttpRequest.BodyPublishers.ofString(GRANT_TYPE_CLIENT_CREDENTIALS))
                .build();
    }
}