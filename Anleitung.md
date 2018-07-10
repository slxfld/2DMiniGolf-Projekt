# Anleitung

Zum Spielen muss als erstes die Executable JAR Datei geöffnet werden. Dies ist möglich durch einen Doppelklick auf das Symbol der JAR Datei oder durch den Befehl „Java –jar ‚Name Der Datei‘“ in einer Befehls Konsole, in Windows CMD.
Wenn sich das Fenster Öffnet, gelangt man in das Hauptmenu des Spiels. Dort kann ausgewählt werden ob man entweder einen neuen Spiel Server Starten will, oder einem Spiel beitreten will. <br/>
<p> </p>
</br>
Klickt man mit der Maus auf den Button mit der Beschriftung „Beitreten“, gelangt man zu einer neuen Seite. Auf dieser Seite kann mit den Pfeiltasten „Hoch“ und „Runter“ zwischen den Eingabe Fenstern gewechselt werden um die Daten einzutragen. In 
das Obere Fenster muss die IP Adresse des Zielrechners eingegeben werden, auf dem bereits ein Spiel gestartet wurde, dass als Server agiert. Im unteren Fenster muss ein Name eingegeben werden, der zur Identifizierung des Clients dient. Wird der Button Verbinden geklickt und die Daten wurden richtig eingegeben, wird eine Verbindung zum Server hergestellt und ein Objekt des Clients am Server angelegt. Man wird darauf hin auf die nächste Seite des GUIs geleitet, welches die Spiel Lobby ist wo alle Spieler Namen des Aktiven Servers aufgelistet werden. </br>

Klickt man mit der Maus auf den Button mit der Beschriftung „Host“, Wird auf dem eigenen Rechner ein Server gestartet, auf den sich andere Spieler verbinden können. Man wird darauf hin auf die nächste Seite des GUIs geleitet, welches die Spiel Lobby ist wo alle Spieler Namen des Aktiven Servers aufgelistet werden. </br>

Wenn man In der Lobby angekommen ist, und alle Spieler die am Spiel teilnehmen wollen in der Liste richtig angezeigt werden, kann das Spiel gestartet werden. Soll das Spiel gestartet werden, wird nur beim Host ein Knopf mit der Beschriftung „Start“ dargestellt. Bei normalen Spielern ist dieser Button nicht zu sehen, das heißt nur der Host kann das Spiel starten. Klickt der Host auf den Start Button, wechselt das Spiel in den nächsten Zustand und alle Spieler inklusive Host befinden sich nun im Spiel Zustand. Bei allen Spielern wird nun der erste Level geladen und die Spielerliste verschwindet. Standard mäßig ist als erster immer der Host, also Client 0 am Spielzug. Wird in der oberen rechten Ecke des Bildschirms der Text „Du bist Dran“ eingeblendet. Wenn dieser Text zu sehen ist hat dieser Spieler die Kontrolle über
den Ball. </br>

Am Anfang jedes Levels wird der Ball in eine voreingestellte Position gesetzt von der jeder Spieler startet. Klickt man auf den Bildschirm in den Bereich des J Frames (Das Fenster des Spieles), sollte die Maus nicht wieder losgelassen werden, außer man ist mit der Lage des Schlages zufrieden. Wird die Linke Maustaste gehalten wird eine Linie vom Ball zum Mauszeiger gezeichnet. Von dieser Linie kann die Stärke des Schlages und die Richtung, in die sich der Ball bewegt wird abgelesen werden. </br>

Wird die Linke Maustaste losgelassen, wird der dargestellte Schlag bestätigt. Nachdem man die Maustaste loslässt verschwindet die Linie und der Ball bewegt sich in die angegebene Richtung mit angegebener Geschwindigkeit. Der Ball rollt über die Bahn, verliert an Geschwindigkeit und prallt an Wänden ab, wenn diese in den Weg des Balles kommen. Das Abprallen des Balles an den Wänden kann als Vorteil verwendet werden, um den Ball durch den Level zu navigieren. Kommt ein Spieler zum Ende der Bahn muss er das Ziel mit einer geringen Geschwindigkeit treffen, damit der Ball nicht über das Ziel hinaus rollt. </br>

Wird das Zielloch getroffen, so wird der Zug des Spielers beendet. Nach dem beendeten Zug verschwindet der Text „Du bist Dran“ und der nächste Spieler folgend auf den aktuellen Spieler wird an die Reihe genommen. Der Ball wird zurück auf die Startposition der Bahn gesetzt und beim neuen Spieler erscheint nun der Text „Du bist dran“. Wird das Spielerlimit überschritten, so wird der erste Spieler, also der Host wieder dran genommen. Dazu wird der Level zum nächsten Level geändert wodurch eine neue Runde beginnt. </br>

Wenn alle Levels durchgespielt worden sind und sich das Spiel am Ende befindet, werden die Punkte alle Spieler angezeigt. Der Spieler mit der niedrigsten Punkte Anzahl gewinnt wie auch beim richtigen Minigolf. Die Punkte der Spieler werden mit Spieler sortiert und untereinander in einer Liste angezeigt. </br>


