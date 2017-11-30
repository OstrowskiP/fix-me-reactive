
################################################################################################
# COMMON

field.firstname=Imię
field.lastname=Nazwisko
field.nick=Nick
field.email=Email
field.password=Hasło
field.password.repeat=Powtórz hasło
field.services=Serwisy

submit=OK
reset=Zresetuj
back=Wróć
go.index=Strona główna

################################################################################################
# TOPBAR

topbar.index=Indeks
topbar.myaccount=Moje konto
topbar.hello=Witaj {0}!<br>Twoje serwisy: {1}

################################################################################################
# INDEX

index.title=Authorization example
index.intro.logged=You are now logged as <span class="text-success">{0}</span>, so you can go to \
<a href="{1}">My account</a> section to see your contact info.<br>\
Of course you can <a href="{2}">sign out</a>.
index.intro.notlogged=Now, you aren''t logged, so you can try to <a href="{0}">sign in</a> or <a href="{1}">sign up</a> and create your own account.<br>\
If you don''t remember your password, you also can <a href="{2}">reset your password</a> with the traditional email mechanism.
index.explanation=Each user has one or more services that indicate a specific area or hierarchical level.<br>\
You can restrict sections to those users who match with a set of services (using logic OR or AND, you can choose).<br>\
The <span class="text-warning">master</span> role has always full access to everywhere.
index.example=For example:
index.example.serviceA=the user has access to the ''service A'' area.
index.example.serviceA_serviceB=the user has access to ''service A'' and ''service B'' areas.
index.example.master=full access to every point of the webpage.
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

signout=Wyloguj się

forgot.title=Zapomniałeś hasła?
forgot.sent=Wysłaliśmy Ci wiadomość email na: {0}. Postępuj zgodnie ze wskazówkami w nim zawartymi aby ukończyć zmianę hasła.
forgot.reset.title=Zresetuj hasło
forgot.reseted=Twoje hasło zostało zmienione

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

denied.title=Access denied!
denied.text=You have no privileges to be here.

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
