# ELearningSystem


## Описание
В данном репозитории находится приложение системы дистанционного обучения. Данное приложение разработано как часть курсовой работы на 4-ем семестре обучения в РТУ МИРЭА по дисциплине **«Шаблоны Программных Платформ Языка Джава»**

## Содержание
1. [Описание функциональности](#task0)
2. [Руководство по пользованию](#task1)
3. [Требования к системе](#task2)
4. [Установка и настройка](#task3)

## <a name="task0"></a> 1. Описание функциональности

Приложение использует разделение ролей пользователей:
- сотрудник учебного отдела
- учитель
- ученик

И обладает следующей функциональностью:

**Сотрудник УО**
- может составлять учебный план
- формировать классы
- формировать кафедры
- сопоставлять учителей классам

**Учитель**
- может выполнять проверку домашнего задания
- публиковать учебные материалы

**Ученик**
- может изучать учебные материалы
- выполнять домашние задания


## <a name="task1"></a> 2. Руководство по пользованию

Каждый из пользователей осуществляет вход с помощью формы – вводит логин и пароль. После этого система определяет роль пользователя и загружает доступные ему страницы.

![image](https://user-images.githubusercontent.com/71013663/168915709-f51f8cdb-78aa-49d5-886c-4ca0690a85a6.png)

### Руководство для пользователя с ролью «Cотрудник УО»

Сотрудник может перейти на страницу «Кафедры» и добавить новую кафедру с помощью формы.

![image](https://user-images.githubusercontent.com/71013663/168915795-98a1ed47-72dd-4514-9b6d-7dcf42ecc6f8.png)

После этого сотрудник может добавит учителей на кафедру.

![image](https://user-images.githubusercontent.com/71013663/168915831-dbc6a239-dfa6-4f39-b03c-d78a93662d08.png)

Сотрудник УО может добавить учеников и сформировать классы 

![image](https://user-images.githubusercontent.com/71013663/168915855-7dc576b6-d5d7-4dee-bcf5-559aebe80cba.png)


![image](https://user-images.githubusercontent.com/71013663/168915869-21819e96-5fc4-40db-ad6d-5729b9680875.png)

На странице «Учебный план» сотрудник может добавить для класса дисциплину и установить преподавателя

![image](https://user-images.githubusercontent.com/71013663/168915895-39de667f-e913-437a-8c50-c309e35f76b6.png)

На странице дисциплины сотрудник добавляет новые дисциплины, указывая кафедру, на которой они преподаются

![image](https://user-images.githubusercontent.com/71013663/168915916-9b5cc0e3-6b68-4ead-a4c4-7080b57dc424.png)

## Руководство для пользователя с ролью «Ученик»

Как и сотрудник учебного отдела ученик осуществляет вход с помощью формы входа. После чего система распознает его и дает доступ к страницам. Ученик может просматривать список изучаемых уроков и видеть их содержание, а также выполнять домашние задания.

![image](https://user-images.githubusercontent.com/71013663/168915975-db359a56-8f2c-4e04-a334-f0041b659812.png)

## Руководство для пользователя с ролью «Учитель»

Как и ученик и сотрудник УО, учитель осуществляет вход с помощью формы входа. Ему доступна страница «Программа занятий», где учитель видит список дисциплин и классы, у которых он преподает. Учитель может добавлять новые уроки и проверять домашние задания.

![image](https://user-images.githubusercontent.com/71013663/168916030-9ffaa3fe-0943-4197-a166-6f367331c6b2.png)


## Демонстрация работы приложения 


https://user-images.githubusercontent.com/71013663/169062972-a581e3e4-7a4e-41dc-86cd-71c5adcbdb80.mp4


## <a name="task2"></a> 3. Требования к системе

Для сборки и запуска проекта должны выполняться следующие системные требования:
-	Операционная система Windows \ Linux
-	Установленная JRE (Java Runtime Environment)
-	Версия Java начиная с 11
-	СУБД PostgreSQL
-	Созданная база данных для подключения приложения. Конфигурация БД
  - url: `jdbc:postgresql://localhost:5432/crowd`
  - password: `11`
  - username: `root`
  - все таблицы создаются автоматически


## <a name="task3"></a> 4. Установка и настройка

В данном репозитории проекта в папке `build` содержится актуальная версия установочного файла – `ElearningSystem.jar`. Его можно запустить с помощью команды `java -jar`.

Также можно скачать репозиторий проекта и выполнить сборку с помощью команды `mvnw clean package -Pproduction`. Установочный файл будет расположен в директории `target`.
