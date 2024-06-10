# HSE_SD2024_group2

Красов Владислав

Кудрявцев Фёдор

Мосин Александр

Попов Егор

## Сборка

Windows:

```
gradlew.bat build  # если по какой-то причине нужны отдельные .class файлы утилит
gradlew.bat jar    

#*Добавить в переменнную окружения JAVA_BUILD ссылающуюся на собранный .jar (в папке build/libs)*

java -cp build/libs/untitled-1.0-SNAPSHOT.jar org.example.Main

```
Linux:

```
./gradlew build  # если по какой-то причине нужны отдельные .class файлы утилит
./gradlew jar    

#*Добавить в переменнную окружения JAVA_BUILD ссылающуюся на собранный .jar (в папке build/libs)*

java -cp build/libs/untitled-1.0-SNAPSHOT.jar org.example.Main

```

