import os
import json

def load_config(path_to_config):
    with open(path_to_config, 'r') as config:
        return json.load(config)

def main():
    stats_path = os.getenv('STATS_PATH', '/config/stats.json')
    user_name = os.getenv('USER_NAME')
    password = os.getenv('PASSWORD')

    if not user_name or not password:
        print("Username or password is missing")
        return

    if user_name == 'admin' and password == 'topsecret':
        print("Welcome back!")
        try:
            config = load_config(stats_path)
            print(f"Your GH stats:\n")
            print(json.dumps(config, indent=1))
        except Exception as e:
            print(f"Failed to load configuration: {e}")
        return
    else:
        print("Wrong creds for admin user!")



if __name__ == "__main__":
    main()