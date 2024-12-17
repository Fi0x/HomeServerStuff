// Libraries
#include <ESP8266WebServer.h>
#include <ESP8266HTTPClient.h>
#include <DHT.h>

// DHT11 / DHT22
//# define DHTTYPE DHT11
#define DHTTYPE DHT22
#define DHTPIN D7

// WIFI settings
#define WIFI_SSID "SSID"
#define WIFI_PASSWORD "PW"

// Server settings
#define SERVER_IP "192.168.x.x"
#define SERVER_PORT "2347"

// Sensor settings
#define TEMPERATURE_SENSOR_NAME "Temperature-sensor-name"
#define TEMPERATURE_SENSOR_DESCRIPTION "Temperature-Description"
#define TEMPERATURE_TAG "Test"
#define TEMPERATURE_UNIT "°C"
#define HUMIDITY_SENSOR_NAME "Humidity-sensor-name"
#define HUMIDITY_SENSOR_DESCRIPTION "Humidity-Description"
#define HUMIDITY_TAG "Test"
#define HUMIDITY_UNIT "%"
#define MS_DELAY 600000

// Set sensor to pin
DHT dht(DHTPIN, DHTTYPE);

// Communication setup
WiFiClient client;
HTTPClient http;

//Setup
void setup()
{
  dht.begin();
}

// Loop
void loop()
{
  String serverUrl = "http://";
  serverUrl.concat(SERVER_IP);
  serverUrl.concat(":");
  serverUrl.concat(SERVER_PORT);

  // Read sensor data
  float temperature = dht.readTemperature();
  float humidity = dht.readHumidity();
  delay(100);

  // Open wifi connection
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  int connectionAttempts = 0;
  while (WiFi.status() != WL_CONNECTED && connectionAttempts < 10)
  {
    delay(1000);
  }

  // Build body for temperature-request
  String requestJson = "{\"name\":\"";
  requestJson.concat(TEMPERATURE_SENSOR_NAME);
  requestJson.concat("\",\"description\":\"");
  requestJson.concat(TEMPERATURE_SENSOR_DESCRIPTION);
  requestJson.concat("\",\"unit\":\"");
  requestJson.concat(TEMPERATURE_UNIT);
  requestJson.concat("\",\"type\":\"Temperature\",\"tags\":[\"Temperature\",\"");
  requestJson.concat(TEMPERATURE_TAG);
  requestJson.concat("\"],\"dataDelay\":\"");
  requestJson.concat(MS_DELAY);
  requestJson.concat("\",\"value\":");
  requestJson.concat(temperature);
  requestJson.concat("}");

  // Register temperature sensor and send data so server
  int statusCode = -1;
  connectionAttempts = 0;
  while(statusCode != 200 && connectionAttempts < 10)
  {
    if(WiFi.status() == WL_CONNECTED)
    {
      // Send request to new-data endpoint
      http.begin(client, serverUrl + "/api/new-data");
      http.addHeader("content-type", "application/json");
      
      statusCode = http.POST(requestJson);
      http.end();
    }
    else
    {
      delay(1000);
    }
  }

  // Build body for humidity-request
  requestJson = "{\"name\":\"";
  requestJson.concat(HUMIDITY_SENSOR_NAME);
  requestJson.concat("\",\"description\":\"");
  requestJson.concat(HUMIDITY_SENSOR_DESCRIPTION);
  requestJson.concat("\",\"unit\":\"");
  requestJson.concat(HUMIDITY_UNIT);
  requestJson.concat("\",\"type\":\"Humidity\",\"tags\":[\"Humidity\",\"");
  requestJson.concat(HUMIDITY_TAG);
  requestJson.concat("\"],\"dataDelay\":\"");
  requestJson.concat(MS_DELAY);
  requestJson.concat("\",\"value\":");
  requestJson.concat(humidity);
  requestJson.concat("}");

  statusCode = -1;
  connectionAttempts = 0;
  while(statusCode != 200 && connectionAttempts < 10)
  {
    if(WiFi.status() == WL_CONNECTED)
    {
      // Send request to new-data endpoint
      http.begin(client, serverUrl + "/api/new-data");
      http.addHeader("content-type", "application/json");
      statusCode = http.POST(requestJson);
      http.end();
    }
    else
    {
      delay(1000);
    }
  }

// TODO: Close wifi connection to save energy

  delay(MS_DELAY);
}
