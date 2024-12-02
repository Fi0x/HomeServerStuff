
/*
 * Original code: https://funduino.de/wetterstation-mit-wemos-d1
 */

// libraries
#include <ESP8266WebServer.h>
#include <ESP8266HTTPClient.h>

const char* ssid     = "SSID";
const char* password = "PW";

const char* serverName = "http://IP:PORT/api/register";

void setup() {
  Serial.begin(9600);

  WiFi.begin(ssid, password);
  Serial.println("Connecting");
  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to WiFi network with IP Address: ");
  Serial.println(WiFi.localIP());
}

void loop() {
    //Check WiFi connection status
    if(WiFi.status()== WL_CONNECTED){
      WiFiClient client;
      HTTPClient http;
      
      // Your Domain name with URL path or IP address with path
      http.begin(client, serverName);

      // Specify content-type header
      http.addHeader("content-type", "application/json");
      // Data to send with HTTP POST
      String httpRequestData = "{\"name\":\"Test\",\"description\":\"This is a description\",\"unit\":\"C\",\"type\":\"Temp\",\"tags\":[\"Temp\",\"Test\"]}";
      // Send HTTP POST request
      int httpResponseCode = http.POST(httpRequestData);
     
      Serial.print("HTTP Response code: ");
      Serial.println(httpResponseCode);
      
      // Free resources
      http.end();
    }
    else {
      Serial.println("WiFi Disconnected");
    }
    delay(1000);
}
