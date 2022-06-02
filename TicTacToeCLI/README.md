# Frontend-часть проекта
Упакована с помощью [GraalVM native-image](https://www.graalvm.org/22.1/reference-manual/native-image/).

## Сборка под Windows
1. Установить виртуальную машину в точности как [здесь](https://www.graalvm.org/22.1/docs/getting-started/windows/);
2. Установить необходимые дополнения: читать [здесь](https://stackoverflow.com/questions/64197329/cl-exe-missing-when-building-native-app-using-graalvm);
3. Собираем jar-файл проекта:
    1. В IntelliJ: `File` -> `Project Structure` -> `Artifacts`;
    2. Во второй колонке `+` -> `JAR` -> `From module with dependencies`;
    3. В поле `Main Class` выбираем `FrontendMain`;
    4. **ОБЯЗАТЕЛЬНО**: в поле `Directory for META-INF/MANIFEST.MF` заменить `..\src\main\java` -> `..\src\main\resources`;
    5. `Ok` -> `Apply` -> `Ok`;
    6. В IntelliJ: `Build` -> `Build Artifacts` -> `TicTacToeCLI:jar` -> `Build`;
    7. Если в папке `\TicTacToeCLI\out\artifacts\TicTacToeCLI_jar` появился файл `TicTacToeCLI.jar`, то все сделано верно.
4. Упаковка в `native-image`. **Обязательно** через **Native Tools Command Prompt**:
    1. Через `cd` переходим в папку `TicTacToeCLI_jar`;
    2. Запускаем `native-image -jar TicTacToeCLI.jar`.
    3. Ждем минут 5 и видим появление в папке `TicTacToeCLI_jar` исполняемого файла.

Некоторые ссылки:
* Ошибка [no main manifest attribute](https://qna.habr.com/q/614861);
* [Она же](https://stackoverflow.com/questions/9689793/cant-execute-jar-file-no-main-manifest-attribute).
