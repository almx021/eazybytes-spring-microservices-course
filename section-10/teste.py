import http.client
from time import sleep

conn = http.client.HTTPConnection("localhost", 8072)

for i in range(10):
    conn.request("GET", "/eazybank/cards/api/build-info", headers={"user": "teste"})

    response = conn.getresponse()
    print(response.status, response.reason)

    data = response.read()
    print(data.decode("utf-8"))

    for header, value in response.getheaders():
        print(f"{header}: {value}")

    sleep(0.5)
    print("\n")

conn.close()
