################################################################################################
# COMMON
field.firstname=Imię
field.lastname=Nazwisko
field.nick=Nick
field.email=Email
field.address=Adres
field.phone=Numer telefonu
field.password=Hasło
field.password.repeat=Powtórz hasło
field.services=Poziom uprawnień
field.parts=Użyte części
field.status=Status zgłoszenia
submit=OK
reset=Zresetuj
back=Wróć
go.index=Strona główna
################################################################################################
# TOPBAR
topbar.myaccount=Moje konto
topbar.hello=Witaj {0}!<br>Poziom uprawnień: {1}
topbar.fixrequest=Zgłoś żądanie naprawy
topbar.users=Użytkownicy
topbar.trackrequest=Śledź zgłoszenie
topbar.myrequests=Zgłoszenia
################################################################################################
# INDEX
index.title=Witaj na stronie serwisu FixME!
index.intro.logged=Jesteś zalogowany jako <span class="text-success">{0}</span>, możesz przejść do zakładki \
<a href="{1}">Moje konto</a> żeby zobaczyć i edytować swoje dane osobowe.
index.intro.notlogged=Nasz serwis oferuje naprawę sprzętu elektronicznego bez konieczności wychodzenia z domu! \
To co wyróżnia nas od konkurencji to możliwość zamówienia serwisu z naprawą na miejscu \
lub z odbiorem sprzętu przez naszego pracownika (jeśli naprawa wymaga specjalistycznego sprzętu i naszego laboratorium). \
Wystarczy, że wypełnisz zgłoszenie serwisowe, nasz praacownik odezwie się do Ciebie i umówi w dogodnej dla Ciebie dacie. \
Zachęcamy do <a href="{0}">założenia swojego konta</a>, umożliwi Ci to śledzenie statusu swoich zgłoszeń oraz przeglądanie ich historii.
################################################################################################
# MY ACCOUNT
myaccount.title=Moje konto
################################################################################################
# AUTH
signup=Załóż konto
signup.title=Zarejestruj się
signup.signin.question=Posiadasz już swoje konto?
signup.signin=Zaloguj się
signup.thanks=Dzięki {0} za rejestrację na naszej stronie!
signup.sent=Wysłaliśmy Ci wiadomość email na: {0}. Postępuj zgodnie ze wskazówkami w nim zawartymi aby ukończyć aktywację konta.
signup.ready=Twoje konto jest już aktywne
signin=Zaloguj się
signin.title=Zaloguj się na istniejące konto
signin.rememberme=Pamiętaj mnie
signin.forgot.question=Zapomniałeś hasła?
signin.forgot=Zresetuj hasło
signin.email.placeholder=adres@email.pl
signin.address.placeholder=Stefana Żeromskiego 116, 90-924 Łódź
signin.phone.placeholder=000-000-000
signout=Wyloguj się
forgot.title=Zapomniałeś hasła?
forgot.sent=Wysłaliśmy Ci wiadomość email na: {0}. Postępuj zgodnie ze wskazówkami w nim zawartymi aby ukończyć zmianę hasła.
forgot.reset.title=Zresetuj hasło
forgot.reseted=Twoje hasło zostało zmienione
edit.account.title=Edytuj konto
edit.account.save=Zapisz zmiany
edit.account.changed=Konto zostało zaktualizowane
changepass=Zmień hasło
changepass.title=Zmień swoje hasło
changepass.field.current=Stare hasło
changepass.field.new=Nowe hasło
changepass.field.repeat=Powtórz nowe hasło
auth.user.notexists=Użytkownik z podanym adresem email nie istnieje
auth.user.notunique=Konto z podanym adresem email już istnieje
auth.credentials.incorrect=Niepoprawny adres email lub hasło
auth.passwords.notequal=Hasła muszą się zgadzać
auth.password.changed=Hasło zostało pomyślnie zmienione
auth.currentpwd.incorrect=Stare hasło jest niepoprawne
denied.title=Odmowa dostępu!
denied.text=Nie posiadasz wystarczających uprawnień do wyświetlenia tej strony.
################################################################################################
# ERRORS
error.unknown.title=Oops, wystąpił błąd
error.unknown.text=Wyjątek został zalogowany z ID <strong>{0}</strong>.
error.notfound.title=Nie znaleziono uchwytu
error.notfound.text=Zgłoszenie ''{0}''
################################################################################################
# MAILS
mail.welcome.subject=Witaj w serwisie FixMe! Potwierdź aktywację swojego konta
mail.welcome.hello=Witaj {0}!
mail.welcome.prelink=Dziękujemy za założenie konta w naszym serwisie! Zweryfikuj swój adres email klikając na poniższy link.
mail.welcome.postlink=Link wygaśnie za 24 godziny, jeśli nie zostanie wykorzystany.
mail.forgotpwd.subject=Zresetuj swoje hasło do serwisu FixMe
mail.forgotpwd.prelink=Ktoś poprosił o zresetowanie hasła dla Twojego konta w serwisie FixMe. Kliknij w poniższy link aby ustanowić nowe hasło:
mail.forgotpwd.postlink=Link wygaśnie za 24 godziny, jeśli nie zostanie wykorzystany. Jeśli nie chcesz zmieniać swojego hasła, zignoruj tę wiadomość.
mail.sign=Zespół FixMe
language.change=Zmień język
################################################################################################
# FIX REQUEST
makearequest.title=Dodaj zgłoszenie naprawy
makearequest.send=Wyślij zgłoszenie
makearequest.save=Zapisz zgłoszenie
makearequest.customer.name=Imię
makearequest.customer.lastname=Nazwisko
makearequest.customer.address=Adres
makearequest.customer.email=Email
makearequest.customer.phone=Numer telefonu
makearequest.device.type=Typ urządzenia
makearequest.device.manufacturer=Producent
makearequest.device.model=Model
makearequest.description=Opis usterki
makearequest.repairdate=Preferowana data naprawy
makearequest.repairtime=Preferowana godzina naprawy
makearequest.requestmade.title=Zgłoszenie zarejestrowane
makearequest.requestmade.info=Twoje zgłoszenie zostało pomyślnie wysłane, czekaj na telefon z potwierdzeniem\
  lub sprawdź status swojego zamówienia przy użyciu tego kodu śledzącego: <span class="text-success" style="font-weight: bold;">{0}</span>
makearequest.track.details.title=Status zgłoszenia
################################################################################################
makearequest.track.error=Nie możemy znaleźć zgłoszenia pasującego do tokenu: <span class="text-danger" style="font-weight: bold;">{0}</span>\
  , proszę sprawdź czy podajesz poprawny token.
################################################################################################
makearequest.track.customer.name=Imię
makearequest.track.customer.lastname=Nazwisko
makearequest.track.customer.address=Adres
makearequest.track.customer.phone=Numer telefonu
makearequest.track.device.type=Typ urządzenia
makearequest.track.device.manufacturer=Producent
makearequest.track.device.model=Model
makearequest.track.description=Opis usterki
makearequest.track.repairdate=Preferowana data naprawy
makearequest.track.repairtime=Preferowana godzina naprawy
makearequest.track.status=Status zgłoszenia
makearequest.track.partsused=Użyte części
makearequest.track.cost.service=Koszt usługi (ZŁ)
makearequest.track.cost.parts=Koszt części (ZŁ)
makearequest.track.cost.total=Całkowity koszt (ZŁ)
makearequest.track.id=ID zgłoszenia
makearequest.track.button=Śledź zgłoszenie
makearequest.track.title=Szczegóły zgłoszenia
editrequest.title=Edytuj szczegóły zgłoszenia
################################################################################################
# MY REQUESTS
myrequests.title=Zgłoszenia naprawy
myrequests.table.id=ID
myrequests.table.type=Typ
myrequests.table.manufacturer=Producent
myrequests.table.model=Model
myrequests.table.date=Data
myrequests.table.status=Status
################################################################################################
# PARTS
parts.title=Części
parts.table.name=Nazwa
parts.table.price=Cena (ZŁ)
parts.name.placeholder=Kondensator 220uF
parts.save=Zapisz część
parts.add=Dodaj część
parts.details=Szczegóły części
################################################################################################
# Request status
requeststatus.awaitingconfirmation=Oczekuje na potwierdzenie
requeststatus.confirmed=Potwierdzone
requeststatus.inprogress=W trakcie
requeststatus.fixed=Naprawione
requeststatus.shipped=Dostarczone
requeststatus.canceled=Anulowane
requeststatus.completed=Zamknięte
################################################################################################
# Users management
users.title=Użytkownicy
users.email=Email
users.nick=Nick
users.firstname=Imię
users.lastlame=Nazwisko