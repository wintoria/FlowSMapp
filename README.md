# FlowSMapp
JavaFX social media desktop app featuring OOP architecture, CSV persistence, and Singleton state management.

# Analiza Techniczna Projektu Flow

Projekt **Flow** to kompletna i przemyślana implementacja aplikacji społecznościowej zbudowanej w środowisku JavaFX. Kod charakteryzuje się czystością, modularnością oraz trafnym doborem narzędzi programistycznych do realizowanych celów.

---

## Wykorzystane Taktyki i Wzorce Projektowe

W architekturze kodu wyraźnie widać zastosowanie sprawdzonych praktyk inżynierii oprogramowania:

* **Wzorzec Singleton (`DataManager`):** Zapewnia jeden, spójny punkt dostępu do danych w całej aplikacji. Dzięki temu każdy widok (Feed, Profil) pracuje na tych samych, zsynchronizowanych informacjach.
* **Polimorfizm i Klasy Abstrakcyjne:** Wykorzystanie klasy `Content` jako bazy dla postów i komentarzy pozwala na eleganckie współdzielenie logiki (np. daty utworzenia, id, polubień), co znacząco redukuje powtarzalność kodu.
* **Abstrakcja przez Interfejsy (`Likeable`):** Wydzielenie mechanizmu polubień do osobnego interfejsu to doskonała taktyka. Umożliwia ona łatwe rozszerzenie aplikacji o nowe typy treści (np. zdjęcia czy ankiety) bez zmiany istniejącej logiki biznesowej.
* **Kompozycja:** Zamiast rozbudowywać klasę użytkownika o dziesiątki pól, w kodzie zastosowano klasę `Profile`. Pozwala to na separację danych technicznych konta od danych wizualnych profilu.

---

## Mocne Strony Implementacji



* **Zaawansowany UX (User Experience):** Implementacja edycji treści "w miejscu" (`inline editing`) oraz dynamiczne odświeżanie widoków (`refreshPosts`) sprawiają, że aplikacja jest responsywna i nowoczesna w odbiorze.
* **Logika Relacji:** System obserwowania użytkowników (`following/followers`) został rozwiązany w sposób spójny, dbając o dwustronną relację między profilami.
* **Integralność Danych:** Zastosowanie identyfikatorów `UUID` gwarantuje, że każdy post i komentarz jest unikalny w skali całego systemu, co zapobiega błędom przy nadpisywaniu danych.
* **Trwałość (Persistence):** Wbudowany system zapisu do pliku sprawia, że projekt wykracza poza proste ćwiczenie pamięciowe i staje się w pełni funkcjonalnym narzędziem przechowującym stan pracy.

---

## Kierunki Rozwoju i Skalowania

Aby projekt mógł obsługiwać coraz większą liczbę użytkowników i danych, można rozważyć następujące udoskonalenia:

| Obszar | Sugerowana Taktyka | Korzyść |
| :--- | :--- | :--- |
| **Format Danych** | Migracja z CSV na **JSON** | Bezpieczniejsza obsługa znaków specjalnych i łatwiejsze parsowanie złożonych obiektów. |
| **Architektura UI** | **Komponentyzacja** (np. klasa `PostCard`) | Możliwość wielokrotnego wykorzystania tego samego komponentu wizualnego w różnych częściach aplikacji. |
| **Bezpieczeństwo** | Hashowanie haseł | Ochrona prywatności użytkowników nawet w przypadku nieautoryzowanego dostępu do pliku bazy. |

---

 ### Podsumowanie
Kod projektu **Flow** prezentuje wysoki poziom zrozumienia paradygmatów programowania obiektowego. Architektura jest czytelna, a zastosowane rozwiązania techniczne świadczą o dużej dbałości o jakość i strukturę aplikacji.
