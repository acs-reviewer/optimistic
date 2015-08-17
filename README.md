# optimistic

Запуск: $mvn spring-boot:run


Поиск по имени: http://localhost:8080/findUser?name=Ivan <br />
Добавить или обновить: http://localhost:8080/saveUserOrUpdateUser?name=Ivan&param=3

Для демонстрации Optimistic lock - выполнить запрос "добавить или обновить" несколько раз, в течение 5 секунд
