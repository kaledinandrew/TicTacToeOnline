# Frontend часть проекта
## Что сделано и как?
13/04: реализован основной логический шаблон класса `Console`, а именно методы `GET`, `POST` через интерфейс `SessionObserver`; 
методы вставки символа (примитив), отображения результата и игрового поля.

15/04: написан прототип функции `main`, разработаны (и переделаны) прототипов методов подключения к серверу и получения данных, а именно `startSession`, `connectToSession`, `placeSymbol`, `showField`, `createUser`, `connect`, написан JSON-парсер.

28/04: практически полностью налажено взаимодействие с backend-частью, прописан отлов исключений, запросы работают стабильно, расширен класс парсера. Написана рабочая (почти) имитация игры.

## Актуальные планы
* Перенос типа `SessionObserver` из интерфейса в класс, используемый статически
* Разработка других методов, таких как `updateUser`, `deleteUser` и т.д.
* Разбор способов подключения двух игроков с разных машин и передачи аргументов

## Источники информации
* [Как создать проект (для Дианы)](https://www.youtube.com/watch?v=dxn5DsMWhGY)
* [Метод POST (видео)](https://www.youtube.com/watch?v=pc_jrANrjUc&list=PL81zTpL449O1KU5CCjGGqLXoxqZQj6pNr&index=11)
* [Старые песни о главном. Java и исходящие запросы (статья)](https://habr.com/ru/company/umbrellaitcom/blog/423591/)
* [Как отправить HTTP-запрос GET / POST в Java (статья)](https://russianblogs.com/article/35471062972/)
* [Как отправлять HTTP-запросы на Java (статья)](https://javascopes.com/how-to-send-http-requests-in-java-301bb159/)
* [Class URLConnection (основная документация)](https://docs.oracle.com/javase/8/docs/api/java/net/URLConnection.html#addRequestProperty-java.lang.String-java.lang.String-)
