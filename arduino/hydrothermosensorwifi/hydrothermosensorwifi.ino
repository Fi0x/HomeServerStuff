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
#define TEMPERATURE_UNIT "C"
#define HUMIDITY_SENSOR_NAME "Humidity-sensor-name"
#define HUMIDITY_SENSOR_DESCRIPTION "Humidity-Description"
#define HUMIDITY_UNIT "%"
#define DELAY 300000

// Set sensor to pin
DHT dht(DHTPIN, DHTTYPE);

// Communication setup
WiFiClient client;
HTTPClient http;
int statusCode = -1;
String serverUrl = "http://";

//Setup
void setup()
{
  Serial.begin(9600);
  
  serverUrl.concat(SERVER_IP);
  serverUrl.concat(":");
  serverUrl.concat(SERVER_PORT);
  
  // Open wifi connection
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
  }

  // Register temperature-sensor at server
  String requestJson = "{\"name\":\"";
  requestJson.concat(TEMPERATURE_SENSOR_NAME);
  requestJson.concat("\",\"description\":\"");
  requestJson.concat(TEMPERATURE_SENSOR_DESCRIPTION);
  requestJson.concat("\",\"unit\":\"");
  requestJson.concat(TEMPERATURE_UNIT);
  requestJson.concat("\",\"type\":\"Temperature\",\"tags\":[\"Temperature\"]}");
  statusCode = -1;
  while(statusCode != 200)
  {
    if(WiFi.status() == WL_CONNECTED)
    {
      // Send request to register endpoint
      http.begin(client, serverUrl + "/api/register");
      http.addHeader("content-type", "application/json");
      
      statusCode = http.POST(requestJson);
      http.end();
    }
    else
    {
      delay(500);
    }
  }

  // Register humidity-sensor at server
  requestJson = "{\"name\":\"";
  requestJson.concat(HUMIDITY_SENSOR_NAME);
  requestJson.concat("\",\"description\":\"");
  requestJson.concat(HUMIDITY_SENSOR_DESCRIPTION);
  requestJson.concat("\",\"unit\":\"");
  requestJson.concat(HUMIDITY_UNIT);
  requestJson.concat("\",\"type\":\"Humidity\",\"tags\":[\"Humidity\"]}");
  statusCode = -1;
  while(statusCode != 200)
  {
    if(WiFi.status() == WL_CONNECTED)
    {
      // Send request to register endpoint
      http.begin(client, serverUrl + "/api/register");
      http.addHeader("content-type", "application/json");
      statusCode = http.POST(requestJson);
      http.end();
    }
    else
    {
      delay(500);
    }
  }

  // Start sensor (TODO: Put this in the loop and power the sensor with an output pin only when the sensor is needed)
  dht.begin();
}

//Loop
void loop()
{
  // Variables for temperature and humidity
  float temperature = dht.readTemperature();
  float humidity = dht.readHumidity();

  // Upload temperature-data
  String requestJson = "{\"sensorName\":\"";
  requestJson.concat(TEMPERATURE_SENSOR_NAME);
  requestJson.concat("\",\"value\":");
  requestJson.concat(temperature);
  requestJson.concat("}");
  statusCode = -1;
  while(statusCode != 200)
  {
    if(WiFi.status() == WL_CONNECTED)
    {
      // Send request to data-endpoint for temperature
      http.begin(client, serverUrl + "/api/upload");
      http.addHeader("content-type", "application/json");
      statusCode = http.POST(requestJson);
      http.end();
    }
    else
    {
      delay(500);
    }
  }

  // Upload humidity-data
  requestJson = "{\"sensorName\":\"";
  requestJson.concat(HUMIDITY_SENSOR_NAME);
  requestJson.concat("\",\"value\":");
  requestJson.concat(humidity);
  requestJson.concat("}");
  statusCode = -1;
  while(statusCode != 200)
  {
    if(WiFi.status() == WL_CONNECTED)
    {
      // Send request to data-endpoint for temperature
      http.begin(client, serverUrl + "/api/upload");
      http.addHeader("content-type", "application/json");
      statusCode = http.POST(requestJson);
      http.end();
    }
    else
    {
      delay(500);
    }
  }

  delay(DELAY);
}
