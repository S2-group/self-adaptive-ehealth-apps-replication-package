from flask import Flask, request
import FireBaseMessaging
import time
app = Flask(__name__)
@app.route('/', methods=['POST'])
def result():
    f = open("demofile2.txt", "a")
    f.write(str(request.get_json().get('token'))+'\n')
    f.close()
    #Time needed for Android runner to trace and do initial app interaction
    time.sleep(100)
    #Send the user process to the app
    FireBaseMessaging.generate_user_processes()
    #Clean the token file so that it never has more than one
    FireBaseMessaging.clean_token_file()
    return('Sucessfully received a token')
#app.run(host="0.0.0.0", port=5000, debug=True)
