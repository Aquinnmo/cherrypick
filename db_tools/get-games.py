from datetime import datetime, timedelta
import requests
import firebase_admin
from firebase_admin import credentials, firestore

cred = credentials.Certificate("./db_tools/env/cherrypick-6-key.json")
firebase_admin.initialize_app(cred)

db = firestore.client()

BASE_URL = "https://api-web.nhle.com/v1/"


#first let's grab the start and end dates for the season
ENDPOINT = "schedule/now"

url = BASE_URL + ENDPOINT

res = requests.get(url, timeout=6.7)
res.raise_for_status()

res = res.json()

start = res["regularSeasonStartDate"]
end = res["regularSeasonEndDate"]

print("Season start date: " + start + ", Season end date: " + end)

#convert the strings to usable datetime objects
format_string = "%Y-%m-%d"
cDate = datetime.strptime(start, format_string)
end = datetime.strptime(end, format_string)

weekNum = 1

while cDate <= end:

    #Sunday is 0, continue to iterate if not Sunday, Friday, Saturday (0, 5, 6)
    if int(cDate.strftime("%w")) != 5:
        cDate = cDate + timedelta(days=1)
        continue

    #creating the endpoint string
    ENDPOINT = "schedule/" + cDate.strftime("%Y") + "-" + cDate.strftime("%m") + "-" + cDate.strftime("%d")

    url = BASE_URL + ENDPOINT

    print("Requesting scheudle for: " + cDate.strftime("%x"))

    #grab the data
    res = requests.get(url, timeout=6.7)
    res.raise_for_status()

    print("Request successful!")

    res = res.json()

    if res["gameWeek"][0]["dayAbbrev"] != "FRI":
        raise Exception("Day of the week does not match expected.")
        break

    fridayGames = res["gameWeek"][0]["games"]
    saturdayGames = res["gameWeek"][1]["games"]
    sundayGames = res["gameWeek"][2]["games"]

    print(str(len(fridayGames)) + " game(s) on Friday, " + str(len(saturdayGames)) + " game(s) on Saturday, " + str(len(sundayGames)) + " game(s) on Sunday")

    if (len(fridayGames) + len(saturdayGames) + len(sundayGames)) == 0:
        print("No games this week. Advancing a week...")
        cDate = cDate + timedelta(days=7)
        continue

    week = dict()
    
    for game in fridayGames:

        id = game["id"]

        info = {
            "venue": game["venue"]["default"],
            "startTimeUTC": game["startTimeUTC"],
            "gameState": game["gameState"],
            "away": game["awayTeam"]["abbrev"],
            "home": game["homeTeam"]["abbrev"],
            "homeScore": game["homeTeam"].get("score", "NULL"),
            "awayScore": game["awayTeam"].get("score", "NULL"),
            "endingPeriod": game["periodDescriptor"]["periodType"],
            "dayOfWeek": "FRI"
        }

        week[str(id)] = info

    print("Parsed week "+str(weekNum)+" friday games")

    for game in saturdayGames:

        id = game["id"]

        info = {
            "venue": game["venue"]["default"],
            "startTimeUTC": game["startTimeUTC"],
            "gameState": game["gameState"],
            "away": game["awayTeam"]["abbrev"],
            "home": game["homeTeam"]["abbrev"],
            "homeScore": game["homeTeam"].get("score", "NULL"),
            "awayScore": game["awayTeam"].get("score", "NULL"),
            "endingPeriod": game["periodDescriptor"]["periodType"],
            "dayOfWeek": "SAT"
        }

        week[str(id)] = info
    
    print("Parsed week "+str(weekNum)+" saturday games")

    for game in sundayGames:

        id = game["id"]

        info = {
            "venue": game["venue"]["default"],
            "startTimeUTC": game["startTimeUTC"],
            "gameState": game["gameState"],
            "away": game["awayTeam"]["abbrev"],
            "home": game["homeTeam"]["abbrev"],
            "homeScore": game["homeTeam"].get("score", "NULL"),
            "awayScore": game["awayTeam"].get("score", "NULL"),
            "endingPeriod": game["periodDescriptor"]["periodType"],
            "dayOfWeek": "SUN"
        }

        week[str(id)] = info

    
    print("Parsed week "+str(weekNum)+" sunday games")

    db.collection("games").document(str(weekNum)).set(week)

    print("Week "+str(weekNum)+" write successful!")
    weekNum += 1

    #iterate the date forward a week
    cDate = cDate + timedelta(days=7)

print("Process complete! Go touch some grass.")