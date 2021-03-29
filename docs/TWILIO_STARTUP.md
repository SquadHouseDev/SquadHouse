# Deploying Twilio Voice Server with Android Quickstart Project
1. Clone the Twilio Voice Server
```sh
git clone https://github.com/twilio/voice-quickstart-server-python.git
```
2. Install requirements
```sh
cd path/to/voice-quickstart-server-python
pip install -r requirements.txt
```
3. Configure the following fields in *server.py*
```Python
# server.py
ACCOUNT_SID = 'AC***'
API_KEY = 'SK***'
API_KEY_SECRET = '***'
PUSH_CREDENTIAL_SID = 'CR***'
APP_SID = 'AP***'
```
  - ACCOUNT_SID (Console Dashboard: https://www.twilio.com/console)
  - API_KEY (API key Dashboard: https://www.twilio.com/console/voice/settings/api-keys)
  - API_KEY_SECRET (https://www.twilio.com/console/voice/settings/api-keys)
  - APP_SID (TwiML App Dashboard: https://www.twilio.com/console/voice/twiml/apps)

4. Run the Twilio Python Voice server
```sh
python server.py
```
5. Open up the machine port 5000 (default) for access.
```sh
ngrok http 5000
```

6. Navigate to the http://localhost:5000/accessToken to find the access token. Save this token for later in step 10.

7. Clone the Android Quickstart Project
```sh
git clone https://github.com/twilio/voice-quickstart-android.git
```

8. Open up the cloned Twilio Android Voice Quickstart project.

9. Navigate to directory *exampleCustomAudioDevice*
10. Update the project variables to reflect the token from step 6.
```java
// CustomDeviceActivity.java
private String accessToken = "PASTE_YOUR_ACCESS_TOKEN_HERE";
private AudioManager audioManager;
private int savedAudioMode = AudioManager.MODE_NORMAL;
```
11. Build and run the quickstart app on an Android device.
<img width="423px" src="https://raw.githubusercontent.com/twilio/voice-quickstart-android/master/images/quickstart/voice_activity.png"><br>
Press the call button to open the call dialog.
<img width="423px" src="https://raw.githubusercontent.com/twilio/voice-quickstart-android/master/images/quickstart/voice_make_call_dialog.png"><br>
Leave the dialog text field empty and press the call button to start a call. You will hear the congratulatory message. Support for dialing another client or number is described in steps 9 and 10.
<img width="423px" src="https://raw.githubusercontent.com/twilio/voice-quickstart-android/master/images/quickstart/voice_make_call.png">


## Reference
- https://github.com/twilio/voice-quickstart-android
