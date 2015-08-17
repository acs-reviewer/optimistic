# optimistic

Запуск: $mvn spring-boot:run


Поиск по имени: <a target="_blank" href="http://localhost:8080/findUser?name=Ivan">http://localhost:8080/findUser?name=Ivan</a> <br />
Добавить или обновить: <a target="_blank" href="http://localhost:8080/saveUserOrUpdateUser?name=Ivan&param=3">http://localhost:8080/saveUserOrUpdateUser?name=Ivan&param=3</a>

Для демонстрации Optimistic lock - выполнить запрос "добавить или обновить" несколько раз, в течение 5 секунд
