package de.tum.mw.ftm.praktikum.smartinsightphd;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by marcengelmann on 14.12.15.
 */
public class LoginHandler {

    private User user = null;

    protected boolean tryLogin(String mUsername, String mPassword)
    {
        HttpURLConnection connection;
        OutputStreamWriter request = null;
        URL url = null;
        String response = null;
        String parameters = "matrikelnummer="+mUsername+"&passwort="+mPassword;

        try
        {
            url = new URL("http://www.marcengelmann.com/smart/check_credidentials.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            // Response from server after login process will be stored in response variable.
            response = sb.toString();

            isr.close();
            reader.close();

            if(response.contains("access_denied")) {
                return false;
            } else {
                saveUserData(response);
                return true;
            }

        }
        catch(IOException e)
        {
            // Error
        }
        return false;
    }

    private void saveUserData(String jsonString) {

        try {
            System.out.println("User Data received ... saving now!");
            JSONObject json = new JSONObject(jsonString);

            System.out.println(json.toString());

            String name = json.get("name").toString();
            //Todo speichern der email adresse statt matrikelnummern, aber dann muss auch in MainActivity die matrikelnummer vom Serverseite aus angepasst werdne
            String email = json.get("matrikelnummer").toString();

            // Todo speichern der name und email der dozenten!! Ebenfalls bei Login Daten

            user = new User(email, name);

        } catch(JSONException e) {
            System.out.println(e);
        } catch(NullPointerException e) {
            System.out.println(e);
        }
    }

    public User getUserData() {
        return user;
    }
}