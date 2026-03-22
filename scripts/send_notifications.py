#!/usr/bin/env python3
"""
Sends subscription lifecycle notifications to the API.
Follows the flow: PURCHASED -> CANCELED -> RESTARTED.
Uses only stdlib (urllib, json) - no extra dependencies.
"""
import json
import sys
import urllib.error
import urllib.request

BASE_URL = "http://localhost:8080/api/subscriptions/notifications"
SUBSCRIPTION_ID = 1
NOTIFICATION_TYPES = [
    "SUBSCRIPTION_PURCHASED",
    "SUBSCRIPTION_CANCELED",
    "SUBSCRIPTION_RESTARTED",
]


def send_notification(ntype: str) -> bool:
    payload = {"subscriptionId": SUBSCRIPTION_ID, "type": ntype}
    data = json.dumps(payload).encode("utf-8")
    req = urllib.request.Request(
        BASE_URL,
        data=data,
        method="POST",
        headers={"Content-Type": "application/json"},
    )
    try:
        with urllib.request.urlopen(req, timeout=10) as resp:
            status = resp.getcode()
            print(f"  {ntype}: {status} Accepted")
            return status == 202
    except urllib.error.HTTPError as e:
        body = e.read().decode("utf-8") if e.fp else ""
        print(f"  {ntype}: ERROR {e.code} - {body[:200]}", file=sys.stderr)
        return False
    except urllib.error.URLError as e:
        print(f"  {ntype}: ERROR - {e.reason}", file=sys.stderr)
        return False


def main():
    print("Sending subscription lifecycle notifications...")
    ok = True
    for ntype in NOTIFICATION_TYPES:
        if not send_notification(ntype):
            ok = False
    if ok:
        print("Done. Check subscription status and event_history in the database.")
    else:
        sys.exit(1)


if __name__ == "__main__":
    main()
