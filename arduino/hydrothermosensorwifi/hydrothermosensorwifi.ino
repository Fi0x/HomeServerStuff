/*
 * Original code: https://funduino.de/wetterstation-mit-wemos-d1
 */

// libraries
#include <ESP8266WebServer.h>
#include <time.h>
#include <DHT.h>

// DHT11 / DHT22
//# define DHTTYPE DHT11
#define DHTTYPE DHT22
#define DHTPIN D7

// WIFI settings
#define WIFI_SSID "SSID"
#define WIFI_PASSWORD "PW"

// Set time-server and time-zone
#define TIME_SERVER "de.pool.ntp.org"
#define TIME_ZONE "CET-1CEST,M3.5.0/02,M10.5.0/03"

// Set sensor to pin
DHT dht(DHTPIN, DHTTYPE);

// Time setup
time_t currentTime;
tm Zeit;

// Webserver Port 80
WiFiServer Server(80);

// create wifi client
WiFiClient Client;

//Setup
void setup()
{
  Serial.begin(9600);

  // Parameters for the time
  configTime(TIME_ZONE, TIME_SERVER);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  // Open wifi connection
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(200);
    Serial.print(".");
  }

  Server.begin();

  // start sensor
  dht.begin();
}

//Loop
void loop()
{
  // read current time
  time(&currentTime);

  // transform time to current time-zone
  localtime_r(&currentTime, &Zeit);

  // variables for temp and humidity
  float Temperatur;
  float Luftfeuchtigkeit;
  String AnzeigeTemperatur;
  String AnzeigeLuftfeuchtigkeit;

  Client = Server.available();
  if (Client)
  {
    // Seite aufbauen wenn SeiteAufbauen true ist
    boolean SeiteAufbauen = true;

    // String SchaltungLesen = "";
    while (Client.connected())
    {
      if (Client.available())
      {
        char Zeichen = Client.read();
        if (Zeichen == '\n')
        {
          // wenn SeiteAufbauen den Wert true hat
          if (SeiteAufbauen)
          {
            // HTTP-Anforderung senden
            Client.println("HTTP/1.1 200 OK");
            Client.println("Content-Type: text/html");

            // Leerzeile zwingend erforderlich
            Client.println();

            /*
              HTML-Seite aufbauen
              die folgenden Anweisungen müssen mit print oder println gesendet werden
              println "verschönert" den Quelltext
              " muss mit \" maskiert werden
            */
            Client.println("<!doctype html>");
            Client.println("<html>");
            Client.println("<body>");

            // alle 60 Sekunden aktualisieren mit meta-Tag
            Client.println("<meta http-equiv=\"refresh\" content=\"60\">");
            Client.println("<h1> Temperatur und Luftfeuchtigkeit messen</h1>");
            Client.println("<hr />");
            Client.print("<h2>Letzte Messung: ");

            // tm_mday -> Wochentag anzeigen
            switch (Zeit.tm_wday)
            {
              case 0:
                Client.print(F("Sonntag"));
                break;
              case 1:
                Client.print(F("Montag"));
                break;
              case 2:
                Client.print(F("Dienstag"));
                break;
              case 3:
                Client.print(F("Mittwoch"));
                break;
              case 4:
                Client.print(F("Donnerstag"));
                break;
              case 5:
                Client.print(F("Freitag"));
                break;
              case 6:
                Client.print(F("Samstag"));
                break;
            }
            Client.print(", ");

            // Tag
            // bei einstelligen Werten -> führende 0 ergänzen

            if (Zeit.tm_mday < 10) Client.print("0");
            Client.print(Zeit.tm_mday);
            Client.print(".");

            // Monat Zählung beginnt mit 0
            // bei einstelligen Werten -> führende 0 ergänzen
            if (Zeit.tm_mon < 10) Client.print("0");
            Client.print(Zeit.tm_mon + 1);
            Client.print(".");

            // tm_year + 1900
            Client.print(Zeit.tm_year + 1900);

            // Uhrzeit
            Client.print(" Uhrzeit: ");

            // Stunden
            if (Zeit.tm_hour < 10) Client.print("0");
            Client.print(Zeit.tm_hour);
            Client.print(":");

            // Minuten
            if (Zeit.tm_min < 10) Client.print("0");
            Client.print(Zeit.tm_min);
            Client.print(":");

            // Sekunden
            if (Zeit.tm_sec < 10) Client.print("0");
            Client.print(Zeit.tm_sec);

            Client.println("</h2>");
            Client.println("<hr />");

            // Temperatur lesen
            AnzeigeTemperatur = String(dht.readTemperature());

            // . durch , ersetzen
            AnzeigeTemperatur.replace(".", ",");

            // Luftfeuchtigkeit lesen
            AnzeigeLuftfeuchtigkeit = String(dht.readHumidity());

            // . durch , ersetzen
            AnzeigeLuftfeuchtigkeit.replace(".", ",");

            // Daten im Browser anzeigen
            Client.print("<b>Temperatur:</b><blockquote>");
            Client.println(AnzeigeTemperatur + " &deg;C</blockquote>");
            Client.println("<br>");
            Client.print("<b>Luftfeuchtigkeit:</b><blockquote>");
            Client.println(AnzeigeLuftfeuchtigkeit + " %</blockquote><hr>");
            Client.println("<form>");

            // Button formatieren
            Client.print("<input style=\"font-size:16pt; font-weight:bold;");
            Client.print("background-color:#55A96B;");
            Client.print("display:block; cursor:pointer;\"type=\"button\"");

            // Seite neu laden
            Client.println(" onClick=\"location.href='WiFi.localIP()'\" value=\"aktualisieren\">");
            Client.println("</form>");
            Client.println("<hr />");

            // IPs anzeigen
            Client.print("<b>Eigene IP: ");
            Client.print(Client.remoteIP());
            Client.print("</b>");
            Client.print("<br><b>IP Klient (D1): ");
            Client.print(WiFi.localIP());
            Client.print("</b>");
            Client.println("</body>");
            Client.print("</html>");

            // HTTP-Antwort endet mit neuer Zeile
            Client.println();

            // Seite vollständig geladen -> loop verlassen
            break;
          }

          // wenn new line (\n) gesendet wurde -> Seite aufbauen
          if (Zeichen == '\n') SeiteAufbauen = true;

          // bei einem anderen Zeichen als return (\r)
          else if (Zeichen != '\r')
          {
            SeiteAufbauen = false;
          }
          delay(1);
          Client.stop();
        }
      }
    }
  }
}
