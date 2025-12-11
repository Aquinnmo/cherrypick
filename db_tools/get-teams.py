import requests
import firebase_admin
from firebase_admin import credentials, firestore

cred = credentials.Certificate("./db_tools/env/cherrypick-6-key.json")
firebase_admin.initialize_app(cred)

BASE_URL = "https://api-web.nhle.com/v1/"
ENDPOINT = "standings/now"

url = BASE_URL + ENDPOINT

res = requests.get(url, timeout=6.7)
res.raise_for_status()

res = res.json()

standings = res["standings"]

cleaned = dict()

for s in standings:
    teamKey = s["teamAbbrev"]["default"]

    team = {
        "gamesPlayed": s["gamesPlayed"],
        "wins": s["wins"],
        "losses": s["losses"],
        "otLosses": s["otLosses"],
        "pointPctg": s["pointPctg"],
        "points": s["points"],
        "conference": s["conferenceName"],
        "division": s["divisionName"]
    }

    cleaned[teamKey] = team

#works to this point

db = firestore.client()

for key in cleaned.keys():
    db.collection("teams").document(key).set(cleaned[key])

print("Success! Go touch some grass!")