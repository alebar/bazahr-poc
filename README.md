# bazahr-poc

POC dla aplikacji BaZaHR, pełniącej rolę QueryModelu w projektowanym przez nasz zespół systemie.

## Setup bazy danych

```
docker run --name postgres -e POSTGRES_PASSWORD=<secret> -e POSTGRES_DB=bazahrdb -d -p 5432:5432 postgres
```

## Idea działania

BaZaHR dostaje event na generyczny endpoint:

```
POST /v1/messages
{
    "type": "urn:ekp:events/candidate-accepted",
    "payload": { nieustrukturalizowane dane }
}
```

i zapisuje go w kolejce w tabeli `inbox` ze statusem `pending`.

Następnie jest job w BaZaHR (`InboxProcessingTrigger`), który co jakiś czas sprawdza, czy są w bazie nowe zdarzenia, czekające na obsługę. Jeśli tak, to je pobiera z kolejki, przy pomocy kwerendy `UPDATE ... RETURNING *;`, zmieniając status na `processing`. Jest to odpowiednik `findAndModify()` z Mongo. Pozwala atomowo zmienić i pobrać rekord. Dzięki temu joby nie muszą mieć locków — może ich być uruchomionych wiele (w wielu wątkach, na wielu instancjach).

Pobranie wiadomości do obsługi dzieje się poza transakcją, ale następne kroki są już robione w ramach `@Transactional`, w osobnej klasie `InboxProcessor`. Te kroki to wywołanie po kolei wszystkich _denormalizerów_, które interpretują event w read modelu.

Na końcu transakcji (ale jeszcze w jej ramach) zdarzenie jest publikowane na kolejkę wychodzącą (wzorzec `transactional outbox`) i ustawia mu się status na `finished`. Publikacja na kolejkę jest w modelu _fan-out_, tzn. dla jednego procesowanego eventu może być zapisanych kilka wiadomości wychodzących, w zależności od liczby listenerów zarejestrowanych na dany event.

Kolejkę `outbox` obsługuje inny, niezależny job: `OutboxProcessor`. Jego zadaniem jest wysłanie wiadomości na skonfigurowany endpoint. Tu nie musi być transakcji. Aplikacje muszą być gotowe na _at-least-once delivery_, bo możliwe będą powtórzenia (np. wysłałem powiadomienie RESTem, ale nie zapisałem info o tym do bazy, więc za chwilę mój kolega podejmie zadanie ponownie).

Mechanizm publikacji zdarzeń powinien obsługiwać powtórzenia z limitem (np. w przypadku błędu zapisując w bazie, który to był _retry_).
