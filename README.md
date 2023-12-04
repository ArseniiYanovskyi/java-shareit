# java-shareit
Приложение по размещению предложений об аренде вещей(к примеру: дрель, лестница, автомобиль и т.п.), с дополнительным функционалом по запросам таких.
Архитектура состоит из двух модулей: Gateway и основной сервис. Gateway обращается к сервису через HTTP клиент на основе RestTemplate. Оба модуля запускаются в своём Docker контейнере. Основной сервис использует базу данных PostgreSQL, что также разворачивается в своём Docker контейнере.
