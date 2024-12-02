
/*
 * Original code: https://funduino.de/wetterstation-mit-wemos-d1
 */

// libraries
#include <ESP8266WebServer.h>
#include <ESP8266HTTPClient.h>

// WIFI settings
#define WIFI_SSID "FRITZ!Box 6670 UZ"
#define WIFI_PASSWORD "17473358040032949830"
#define SERVER "192.168.178.42:2347/api/register"

//Setup
void setup()
{
  Serial.begin(9600);
  
  // Open wifi connection
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
  }
  Serial.print("Connected to wifi");
}

//Loop
void loop()
{
  if(WiFi.status() == WL_CONNECTED)
  {
    WiFiClient client;
    HTTPClient http;

    // Create request
    http.begin(client, SERVER);

    http.addHeader("Content-Type", "application/json");
    String requestData = "{}";
    int httpResponseCode = http.POST(requestData);

    Serial.print("Http Response code: ");
    Serial.println(httpResponseCode);
    
    http.end();
  } else
  {
    Serial.print("Wifi is not connected");
  }

  delay(1000);
}
