package de.sjanusch.data;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Sandro Janusch
 * Date: 13.05.16
 * Time: 14:09
 */
public final class Constants {

    public static final String[] CONSTANT_ARRAY = {
        "Mir doch egal, ich lass das jetzt so",
        "Männer, die einer Frau noch die Tür aufhalten, Blumen mitbringen und ihr einen Kuss auf die Stirn geben, sind wahre Helden.",
        "Wer über die Dummheit anderer lacht, hat sich über deren Motivation noch keine Gedanken gemacht.",
        "Appetit holt man sich woanders, gegessen wird zuhause.",
        "Das Leben ist eine große Gleichung. Jeder muss kämpfen um sie zu lösen, aber am Ende kommt bei allen das Gleiche raus.",
        "Keiner verdient deine Träne, denn der, der sie verdienen würde, würde dich nie zum weinen bringen!",
        "Schicksal ist, wenn Du den einen Menschen findest, ohne den Du nicht leben möchtest! Schicksal ist, wenn dieser eine Mensch das genauso sieht.",
        "Erzähle niemanden deine Pläne, zeige ihnen nur deine Ergebnisse.",
        "Lache niemals über die Dummheit anderer, das ist deine Chance.",
        "Manche Menschen sagen Liebe sei eine Krankheit, doch wenn sie es ist dann eine Unheilbare.",
        "Wir sind wie Peter Pan, kein Stück erwachsen aber derbe cool.",
        "Musik hören und alles andere vergessen",
        "Am Ende bereust du nicht was du getan hast, sondern nur was du nicht getan hast.",
        "Liebe ist wenn man über einen anderen Menschen mehr nachdenkt als sich selbst.",
        "Wir drücken an Türen auf denen dick und fett ZIEHEN! steht.",
        "Es gibt zwei Arten von Menschen: Einmal die, die darauf warten das sich alles verändert und die, die erkennen, dass sie die Macht besitzen um Alles zu verändern.",
        "Liebe ist, wenn die Sonne nicht aufgehen muss, um deinen Tag zu bezaubern.",
        "What ever your 100% looks like, give it!",
        "Wissen Sie, was Sie da tun? Nein. ich überrasche mich gern selbst.",
        "Manche Leute reagieren verwirrt, wenn ein Satz nicht so endet, wie sie es Kartoffelsalat.",
        "Der zweite Platz ist der erste Verlierer",
        "Ich mache keine Fehler, sondern lerne nur dazu.",
        "Ich lerne aus meinen Fehlern, deswegen mache ich immer wieder Neue",
        "Alle sagten immer das geht nicht, dann kam jemand der das nicht wußte und hat es einfach gemacht!",
        "Uns halten nur die Grenzen, die wir uns selbst setzen.",
        "Lieber verrückt das Leben genießen, als normal langweilen.",
        "Es gibt tausend Arten von Lärm, aber nur eine wirkliche Stille.",
        "Erfahrung bedeutet nichts, jeder kann etwas jahrelang falsch machen ohne es zu merken.",
        "Die wahre Stärke eines Menschen sieht man nicht an den Muskeln, sondern wie er hinter dir steht!",
        "Der Mensch plant und das Schicksal lacht darüber!",
        "Viele Menschen hinterlassen Spuren und nur wenige hinterlassen Eindrücke.",
        "Wenn du die Freiheit aufgibst, um Sicherheit zu gewinnen, wirst du am Ende beides verlieren.",
        "Nichts ist schmerzender, als sich der Isolation hingeben zu müssen, nur weil man glaubt alleine am Besten zurecht zu kommen.",
        "Meine Feinde kenne ich, meine Freunde nicht.",
        "Die Feinde meines Feindes sind meine Freunde.",
        "Manchmal denkt man es ist stark festzuhalten.",
        "Doch es ist das Loslassen, das was wahre Stärke zeigt.",
        "Früher war alles Besser, ist in der Zukunft das Jetzt.",
        "Ich will an den Ort an dem das Meer den Himmel berührt.",
        "Dein Herz ist frei, hab den Mut ihm zu folgen.",
        "Reisende soll man nicht aufhalten.",
        "Glücklich ist, wer sich bei Sonnenuntergang auf die Sterne freut.",
        "Nur wer Erwachsen wird und ein Kind bleibt, hat verstanden wirklich zu Leben.",
        "Zahme Vögel träumen von Freiheit… Wilde Vögel fliegen!",
        "Du darfst nicht von vorneherein davon ausgehen verletzt zu werden.",
        "Pessimismus führt unweigerlich dazu dass man enttäuscht wird, weil man das Negative heraufbeschwört!",
        "Es gibt niemals eine zweite Changse für den ersten Eindruck.",
        "Die schönste Zeit im Leben sind die kleinen Momente, in denen Du spürst, Du bist zur richtigen Zeit, am richtigen Ort.",
        "Wenn einem die Treue Spaß macht, dann ist es Liebe.",
        "Vergangenheit ist Geschichte, Zukunft ein Geheimnis und jeder Augenblick ein Geschenk.",
        "Weine für einen Augenblick in deinem Leben, aber nicht für einen Augenblick Dein ganzes Leben lang!",
        "Es ist die Frage ob man bereit ist einen Weg zusammen zu gehen oder ob man zwei Wege geht die niemals zusammenfinden würden.",
        "Fremde Fehler beurteilen wir wie Staatsanwälte, die eigenen wie Verteidiger.",
        "Weise die Schuld demjenigen zu, der sie verdient, und nicht demjenigen, der gerade im Weg steht.",
        "Mut ist der Zauber, der Träume Wirklichkeit werden lässt!",
        "Achte auf Deine Gedanken, denn sie werden deine Worte.",
        "Achte auf Deine Worte, denn sie werden deine Handlungen.",
        "Achte auf Deine Handlungen, denn sie werden deine Gewohnheiten.",
        "Achte auf Deine Gewohnheiten, denn sie werden dein Charakter.",
        "Achte auf Deinen Charakter, denn er wird dein Schicksal.",
        "Die Wahrheit über die Wahrheit: Sie tut weh, deshalb lügen wir.",
        "Die meiste Macht in einer Beziehung haben immer diejenigen, denen sie nicht soviel bedeutet",
        "Manche Menschen sind wie Glasscherben: In ihnen spiegelt sich der Sonne glänzend Licht,doch die scharfen Kanten sieht der Betrachter nicht.",
        "Mut bedeutet nicht die Abwesenheit von Angst sondern die Erkenntnis, dass es etwas wichtigeres gibt als Angst.",
        "Fürchte Dich nicht vor dem langsamen Vorwärtsgehen, fürchte Dich nur vor dem Stehen bleiben.",
        "Manche Menschen drücken nur ein Auge zu, damit sie besser zielen können.",
        "Trinke nie zu viel, denn die letzte Flasche die drauf geht, könntest Du selbst sein.",
        "Die Geschichte wird freundlich mit mir umgehen, denn ich habe vor, sie zu schreiben.",
        "Der Neider sieht immer nur das Beet, den Spaten sieht er nicht.",
        "Man sieht nur mit dem Herzen gut.",
        "Wer glaubt etwas zu sein, hat aufgehört etwas zu werden.",
        "Das Staunen ist der Anfang der Erkenntnis",
        "Der Augenblick ist zeitlos.",
        "Auch ein langer Weg beginnt mit dem ersten Schritt.",
        "Ein Tag ohne Lächeln ist ein verlorener Tag.",
        "Nur tote Fische schwimmen mit dem Strom.",
        "Ich höre und vergesse. Ich sehe und erinnere. Ich tue und verstehe. ist wichtiger als Wissen.",
        "Wissen ist begrenzt, die Phantasie unendlich.",
        "Um klar zu sehen, genügt oft ein Wechsel der Blickrichtung.",
        "Liebeskummer ist wie ein Diamant: man sollte ihn mit Fassung tragen.",
        "Wenn die Nacht am tiefesten,ist der Tag am nächsten!",
        "Das Wort ist schärfer wie die Klinge!",
        "Wer will findet Wege, wer nicht will, der findet Gründe!",
        "Wirklich reich ist, wer mehr Träume in seiner Seele hat, als die Realität zerstören kann!",
        "Vertrauen ist eine Oase im Herzen, die von der Karawane des Denkens nie erreicht wird.",
        "Das Neue ist meistens das vergessene Alte.",
        "In der Jugend lernen wir, im Alter verstehen wir.",
        "Vevendo Vides (Wer glaubt wird sehen)",
        "Alle Dinge, die man erlebt, werden verloren gehen in der Zeit, wie eine Träne im Regen.",
        "Jedesmal wenn ich denke, ich bin ganz unten angelangt, kommt jemand und leiht mir eine Schaufel.",
        "Mit den Flügeln der Zeit fliegt die Traurigkeit davon.",
        "Die  Erinnerung ist das einzige Paradies, aus dem wir nicht vertrieben werden können.",
        "Das Glück deines Lebens hängt von der Beschaffenheit deiner Gedanken ab.",
        "Die Zeit heilt keine Wunde, man gewöhnt sich nur an den Schmerz!",
        "Die schlimmste Art, jemanden zu vermissen, ist die, an seiner Seite zu stehen und zu wissen, dass er nie zu einem gehören wird !!!",
        "Du fragtest mich, was ich liebe. “Mein Leben”, sagt ich.",
        "Bedingungslose Liebe heißt nicht klammern, sondern Freiheit!",
        "Zum schweigen fehlen mir die passenden Worte!",
        "Wende dein Gesicht der Sonne zu, dann fallen die Schatten hinter dich.",
        "Schwinge Dich zum Mond empor, denn selbst wenn Du ihn verfehlst landest Du bei den Sternen!!",
        "Keiner hat mich gefragt ob ich leben will, also sagt mir auch nicht wie ich zu leben habe!!!",
        "Gras wächst nicht schneller, wenn man daran zieht.",
        "Assembler ist eine Methode, Programme, die zu langsam laufen, so umzuschreiben, daß sie überhaupt nicht mehr laufen.",
        "Ich bin ein speicherrenitentes Programm!!!",
        "Fraktaler Fehler – System abgestürzt",
        "Disk Error Drive A: – Wasser im Laufwerk (Bitte abpumpen)",
        "Ein Programm sollte nicht nur Hand und Fuß, sondern auch Herz und Hirn haben.",
        "Irren ist menschlich. Aber wer richtigen Mist bauen will, braucht einen Computer !!!",
        "Der Wert eines Programms ist umgekehrt proportional dem von ihm verbrauchten Papier.",
        "Wenn Architekten so bauen würden, wie Programmierer Ihre Programme machen, könnte ein einziger Specht ganze Städte zerstören.",
        "Jedes Programm, in das sich ein Fehler einschleichen kann, wird auch einen enthalten.",
        "Die Arbeit am Computer ist eine Erfahrung extremer Isolation.",
        "Computer sind dazu da, uns die Arbeit zu erleichtern, die wir ohne sie gar nicht haetten.",
        "Wenn am Anfang alles schief geht, nenne es Version 1.0!",
        "Multitasking erzeugt Schizophrenie!",
        "One man’s error is another mans data. (Murphy,one of his laws)",
        "Diese Software verdient die 3 großen GGG: — Gesehen — Gelacht — Gelöscht — {brigens Ein Kezboard/Treiber ist v;llig [berfl[ssig!",
        "Fragen Sie nicht, was Ihr Computer für Sie tun kann. Fragen Sie, was Sie für Ihren Computer tun können.",
        "Informatiklüge: Ich mach’s mal schnell mit dem Computer …! Internetlüge: Ich hol’s mir schnell aus dem Web … ! Lebenslüge: Da bin ich ja zu Fuß noch schneller … !",
        "Wenn Debuggen der Vorgang ist, Fehler aus einem Programm auszubauen, dann ist Programmieren der Vorgang, Fehler in ein Programm einzubauen!",
        "Datentraeger in Laufwerk C: einlegen, weiter mit beliebiger Taste",
        "Real programers do not comment their code. It is hard to write and it should be hard to understand!",
        "Die Sicherheit einer Rechneranlage ist umgekehrt proportional zur Bestechlichkeit der Systembetreuer.",
        "Der Daten sind genug gewechselt, nun lasst uns endlich Informationen sehen!",
        "Es soll sogar Programme geben, die fehlerfrei sind.",
        "Erst wenn der letzte FTP Server kostenpflichtig, der letzte GNU-Sourcecode verkauft, der letzte Algorithmus patentiert, der letzte Netzknoten verkommerzialisert ist, werdet Ihr merken, dass Geld nicht von alleine programmiert.",
        "REALITY.SYS is corrupt. Reboot universe? (y/n)",
        "Computer sind grossartig. Mit ihnen macht man die Fehler viel schneller.",
        "Es ist bemerkenswert, daß nur vielleicht 10% aller Programmierer Programme ohne Verwendung von Flussdiagrammen erfolgreich schreiben können. Unglücklicherweise glauben aber 90%, daß sie der Gruppe dieser 10% angehören.",
        "JEDES Programm hat mindestens einen Fehler. Wenn man versucht diesen Fehler zu beheben baut man mindesestens zwei neue ein!",
        "Lieber Nachts auf wilden Festen, als Zuhause Beta’s testen.",
        "Alle Computer warten mit der gleichen Geschwindigkeit!",
        "Ein Programm, dass du Freitags ablieferst, siehst du Montag wieder.",
        "Jesus definierte den Stack: Die ersten werden die letzten sein.",
        "Der Fehler sitzt meist *vor* dem Rechner!",
        "Für das große Chaos haben wir Computer. Die übrigen Fehler machen wir von Hand.",
        "It’s not a trick, it’s a…. I’ll find the problem.",
        "Undetected errors are handled as if no error occurred.",
        "Fehler 17263: Computer nicht gefunden!",
        "Wie beschleunige ich einen Mac? – Mit 9,81 m/s².",
        "Who the fuck is General Failure? And why is he reading my harddisk?",
        "Ist das nicht der Kollege von General Protection Fault und Major Problem?",
        "Wer Backups macht ist feige!",
        "Software ist der geglueckte Versuch, die Fehler der Computerhardware zu optimieren und durch Weiterentwicklung neue Fehler hinzuzufuegen.",
        "Erst wer beim Programmieren in C ein Bit durch den Speicher schießt, weiß, wie verheerend und wie ästhetisch ein Bug mit Absturzfolge sein kann.",
        "Unix IST benutzerfreundlich – es ist nur etwas waehlerisch",
        "Schizophrenie ist auch nur eine Form von Multitasking",
        "Man verliert Frau und Braut, wenn man am Computer baut, ist man von der Frau entzweit, hat man fuer Computer noch mehr Zeit!",
        "Was man nicht im Kopf hat, muss man im Computer haben.",
        "Fehler, die ein Computer macht, sind nie seine eigenen.",
        "Traue keinem Programm, es sei denn du hast es selbst geschrieben.",
        "Manche Computer kosten ein Vermoegen, manche nur den Verstand.",
        "Computer sind das genialste Produkt menschlicher Faulheit!",
        "Computer koennen uns bei Entscheidungen helfen. Bei welchen muessen wir entscheiden.",
        "Der Computer ist die Loesung auf der Suche nach Problemen.",
        "Der Computer bietet Lösungen an, fuer Probleme, die wir ohne ihn gar nicht hätten.",
    };

    public static final List<String> CONSTANT_LIST = Arrays.asList(CONSTANT_ARRAY);

    public static String getRandomText(final String text) {
        int number = getRandomNumberInRange(0, Constants.CONSTANT_ARRAY.length + 150);
        if (number >= 0 && number < Constants.CONSTANT_ARRAY.length && !Constants.CONSTANT_LIST.contains(text)) {
            return Constants.CONSTANT_ARRAY[number];
        }
        return null;
    }

    public static String getNoRandomText(final String text) {
        int number = getRandomNumberInRange(0, Constants.CONSTANT_ARRAY.length);
        if (number >= 0 && number < Constants.CONSTANT_ARRAY.length && !Constants.CONSTANT_LIST.contains(text)) {
            return Constants.CONSTANT_ARRAY[number];
        }
        return null;
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
