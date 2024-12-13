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
#define DEEP_SLEEP_DELAY 600e6

// Set sensor to pin
DHT dht(DHTPIN, DHTTYPE);

// Communication setup
WiFiClient client;
HTTPClient http;

//Setup
void setup()
{
  delay(100);
  String serverUrl = "http://";
  serverUrl.concat(SERVER_IP);
  serverUrl.concat(":");
  serverUrl.concat(SERVER_PORT);

  // Start and read sensor data
  dht.begin();
  delay(100);
  float temperature = dht.readTemperature();
  float humidity = dht.readHumidity();
  delay(100);

  // Open wifi connection
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  int connectionAttempts = 0;
  while (WiFi.status() != WL_CONNECTED && connectionAttempts < 60)
  {
    delay(500);
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
  while(statusCode != 200 && connectionAttempts < 60)
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
      delay(500);
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
  while(statusCode != 200 && connectionAttempts < 60)
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
      delay(500);
    }
  }

  // Put esp to deep-sleep to save energy
  ESP.deepSleep(DEEP_SLEEP_DELAY);
  delay(100);
}

// Loop (Not used, since deep-sleep will re-do the setup)
void loop()
{
}
