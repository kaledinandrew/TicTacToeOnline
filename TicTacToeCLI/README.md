# Frontend часть проекта
## Что сделано и как?
13/04: реализован основной логический шаблон класса `Console`, а именно методы `GET`, `POST` через интерфейс `SessionObserver`; 
методы вставки символа (примитив), отображения результата и игрового поля. 

## Нюансы
### Класс Console
* Метод `connectToSession` выполняет подключение по URL и возвращает объект типа `HttpURLConnection`. Это означает,
что созданное соединение необходимо закрывать вручную -- методом `disconnect`
* Методы `getUpdate` и `postUpdate` принимают на вход объект типа `HttpURLConnection` заранее созданного соединения.
Предполагается, что оно уже имеет статус OK, т.е. код 200.
### Прочее
* Класс `Variables` считается статическим, т.е. глобальным

## Планы
* Разбор методов `PATCH` и `DELETE`
* Разработка прототика класса `JsonParser`
* Разработка класса `UserClient` и определения способа взаимодействия с серверной частью
* Отлов исключений и возможных ошибок
* Разработка прототипа функции `main`

## Источники информации
* [Как создать проект (для Дианы)](https://www.youtube.com/watch?v=dxn5DsMWhGY)
* [Метод POST (видео)](https://www.youtube.com/watch?v=pc_jrANrjUc&list=PL81zTpL449O1KU5CCjGGqLXoxqZQj6pNr&index=11)
* [Старые песни о главном. Java и исходящие запросы (статья)](https://habr.com/ru/company/umbrellaitcom/blog/423591/)
* [Как отправить HTTP-запрос GET / POST в Java (статья)](https://russianblogs.com/article/35471062972/)
* [Как отправлять HTTP-запросы на Java (статья)](https://javascopes.com/how-to-send-http-requests-in-java-301bb159/)
* [Class URLConnection (основная документация)](https://docs.oracle.com/javase/8/docs/api/java/net/URLConnection.html#addRequestProperty-java.lang.String-java.lang.String-)
