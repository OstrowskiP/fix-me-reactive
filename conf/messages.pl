
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

submit=OK
reset=Zresetuj
back=Wróć
go.index=Strona główna

################################################################################################
# TOPBAR

topbar.index=Indeks
topbar.myaccount=Moje konto
topbar.hello=Witaj {0}!<br>Poziom uprawnień: {1}
topbar.fixrequest=Zgłoś żądanie naprawy
topbar.trackrequest=Śledź zgłoszenie
topbar.myrequests=Moje zgłoszenia

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

index.auth.status=In this case, you are logged as {0} and your services are: {1}
index.table.section=Section
index.table.authobject=Authorization object
index.table.services=Services needed
index.table.allowed=Are you allowed?
index.table.go=Go there and check yourself

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
signin.signup.question=Nie masz jeszcze konta?
signin.signup=Zarejestruj się
signin.forgot.question=Zapomniałeś hasła?
signin.forgot=Zresetuj hasło
signin.email.placeholder=adres@email.pl
signin.address.placeholder=Stefana Żeromskiego 116, 90-924 Łódź
signin.phone.placeholder=000000000

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

auth.user.notexists=There is no user with this email
auth.user.notunique=There is another user with this email
auth.credentials.incorrect=Incorrect email or password
auth.passwords.notequal=The passwords must be equal
auth.password.changed=The password has been successfully changed
auth.currentpwd.incorrect=The current password is incorrect

denied.title=Odmowa dostępu!
denied.text=Nie posiadasz wystarczających uprawnień do wyświetlenia tej strony.

################################################################################################
# ERRORS

error.unknown.title=Oops, an error occurred
error.unknown.text=This exception has been logged with id <strong>{0}</strong>.
error.notfound.title=Handler Not Found
error.notfound.text=For request ''{0}''

################################################################################################
# MAILS

mail.welcome.subject=Welcome to MyWeb! Please confirm your account
mail.welcome.hello=Welcome {0}!
mail.welcome.prelink=Thanks for signing up for this great webpage! Please verify your email address by clicking the link below.
mail.welcome.postlink=Note that this link will expire in 24 hours if it''s not activated.

mail.forgotpwd.subject=Reset your MyWeb password
mail.forgotpwd.prelink=Someone (hopefully you) has requested a password reset for your MyWeb account. Follow the link below to set a new password:
mail.forgotpwd.postlink=Note that this link will expire in 24 hours. If you don''t wish to reset your password, disregard this email and no action will be taken.

mail.sign=Team MyWeb

language.change=Zmień język

################################################################################################
# FIX REQUEST

makearequest.title=Dodaj zgłoszenie naprawy
makearequest.send=Wyślij zgłoszenie
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
makearequest.track.cost.service=Koszt usługi
makearequest.track.cost.parts=Koszt części
makearequest.track.cost.total=Całkowity koszt
makearequest.track.id=ID zgłoszenia
makearequest.track.button=Śledź zgłoszenie
makearequest.track.title=Szczegóły zgłoszenia
editrequest.title=Edytuj szczegóły zgłoszenia

################################################################################################
# MY REQUESTS

myrequests.title=Moje zgłoszenia
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

################################################################################################
# Request status

requeststatus.awaitingconfirmation=Oczekuje na potwierdzenie
requeststatus.confirmed=Potwierdzone
requeststatus.inprogress=W trakcie
requeststatus.fixed=Naprawione
requeststatus.shipped=Dostarczone
requeststatus.canceled=Anulowane
requeststatus.completed=Zamknięte