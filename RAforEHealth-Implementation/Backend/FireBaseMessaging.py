
#!/usr/bin/env python2

from oauth2client.service_account import ServiceAccountCredentials
import requests
import json

SCOPES = ['https://www.googleapis.com/auth/firebase.messaging']
url = 'https://fcm.googleapis.com/v1/projects/raforehealthapps/messages:send'


def _get_access_token():
    credentials = ServiceAccountCredentials.from_json_keyfile_name(
        'raforehealthapps-firebase-adminsdk-cq20c-11010aa381.json', SCOPES)
    access_token_info = credentials.get_access_token()
    return access_token_info.access_token

#Accepts integer from 1 to 3 -> sends a user process to the app with that token
def generate_user_process(body_number, token):
        if body_number == 1:
            body = '{"activities": {"Monday": "Cardio", "Tuesday": "None", "Wednesday": "Cardio", "Thursday": "None", "Friday": "Strength", "Saturday": "None","Sunday": "None"}, "goal": "1000"}'
        if body_number == 2:
            body = '{"activities": {"Monday": "None", "Tuesday": "Cardio", "Wednesday": "None", "Thursday": "Cardio", "Friday": "None", "Saturday": "None","Sunday": "None"}, "goal": "1000"}'
        if body_number == 3:
            body = '{"activities": {"Monday": "Cardio", "Tuesday": "Cardio", "Wednesday": "None", "Thursday": "Cardio", "Friday": "None", "Saturday": "Cardio","Sunday": "None"}, "goal": "1500"}'
	title = 'UserProcess' #Provide your title
	return {'message': {'token': token , 'data' : {'body' : body , 'title' : title }}}

def generate_header():
    return {'Authorization' : 'Bearer ' + _get_access_token(), 'Content-Type' : 'application/json'}

def get_tokens():
    token_file = open('demofile2.txt', 'r')
    return token_file.readlines()

def generate_user_processes():
    tokens = get_tokens()
    for token in tokens:
        token = token.strip()
        print(token)
        requests.post(url=url, data=json.dumps(generate_user_process(1, token)), headers=generate_header(), json=json)

def clean_token_file():
    open('demofile2.txt', 'w').close()
#Example user process generation
#res = requests.post(url=url, data=json.dumps(generate_user_process(1)), headers=generate_header(), json=json)
#print res
#generate_user_processes()
