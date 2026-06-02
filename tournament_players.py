import json
import random
import sys

DEFAULT_COUNT = 1000
DEFAULT_FILE = "tournament_players.json"

player_count = DEFAULT_COUNT
output_file = DEFAULT_FILE

if len(sys.argv) >= 2:
    try:
        player_count = int(sys.argv[1])

        if player_count <= 0:
            raise ValueError()

    except ValueError:
        print("Player count must be a positive integer.")
        sys.exit(1)

if len(sys.argv) >= 3:
    output_file = sys.argv[2]

players = []

for i in range(player_count):

    games_played = random.randint(1, 500)
    wins = random.randint(0, games_played)
    kills = random.randint(
        games_played // 2,
        games_played * 8
    )

    avg_placement = round(
        random.uniform(0.2, 1.0),
        4
    )

    player = {
        "name": f"Player{i}",
        "kills": kills,
        "gamesPlayed": games_played,
        "wins": wins,
        "averagePlacement": avg_placement,
        "placementGames": games_played
    }

    players.append(player)

with open(output_file, "w") as f:
    json.dump(players, f, indent=4)

print(
    f"Generated {player_count} players "
    f"to {output_file}"
)