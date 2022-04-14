# TicTacToeBackend

Список эндпоинтов с примерами вызовов:


### POST /users

Создание нового пользователя

Body example:
```json
{
    "name": "test5",
    "symbol": 5
}
```

Response example:
```json
{
    "userId": 5,
    "name": "test5",
    "symbol": 5
}
```


### GET /users/all-users-ids

Получение id всех пользователей

Response example:
```json
[
    1,
    2,
    3
]
```


### POST /sessions?hostId={id}

Создание новой игровой сессии с указанием ее хоста

Response example:
```json
{
    "sessionId": 3,
    "hostId": 5,
    "guestId": null,
    "field": [
        [
            0,
            0,
            0
        ],
        [
            0,
            0,
            0
        ],
        [
            0,
            0,
            0
        ]
    ],
    "isHostTurn": true
}
```

### PUT /sessions/connect?sessionId={sessionId}&guestId={guestId}

Присоединение к сессии в качестве участника-гостя. При этом в поле сессии guestId будет выставлен id гостя

Response example:

```json
{
    "sessionId": 3,
    "hostId": 5,
    "guestId": 2,
    "field": [
        [
            0,
            0,
            0
        ],
        [
            0,
            0,
            0
        ],
        [
            0,
            0,
            0
        ]
    ],
    "isHostTurn": true
}
```


### PUT /sessions/place-symbol?sessionId=3&userId=5&x=0&y=0

Установка значка пользователя в указанной сессии на координату (x,y)

Response example:
```json
{
    "sessionId": 3,
    "hostId": 5,
    "guestId": 2,
    "field": [
        [
            5,
            0,
            0
        ],
        [
            0,
            0,
            0
        ],
        [
            0,
            0,
            0
        ]
    ],
    "isHostTurn": false
}
```
