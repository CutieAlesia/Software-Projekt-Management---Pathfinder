package com.mygdx.labels;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.SupportedAlgorithms;

/** Label that contains the explanation texts of our supported algorithms. */
public class ExplanationLabel extends Label {

    SupportedAlgorithms currentAlgoExplanation;

    public ExplanationLabel(CharSequence text, Skin skin) {
        super(text, skin);
    }

    public ExplanationLabel(SupportedAlgorithms algorithm, Skin skin) {
        super("", skin);
        selectAlgorithmDescription(algorithm);
        currentAlgoExplanation = algorithm;
    }

    public ExplanationLabel(SupportedAlgorithms algorithm, LabelStyle style) {
        super("", style);
        selectAlgorithmDescription(algorithm);
        currentAlgoExplanation = algorithm;
    }

    /**
     * Sets label's text to the description of the given algorithm.
     *
     * @param algorithm
     */
    public void selectAlgorithmDescription(SupportedAlgorithms algorithm) {

        if (currentAlgoExplanation == algorithm) {
            return;
        }

        switch (algorithm) {
            case ASTAR:
                this.setText(
                        "Der A* Algorithmus gehört zu den informierten Suchverfahren, was bedeutet,"
                            + " dass er über Wissen \n"
                            + "bezüglich des Suchraums verfügt, welches genutzt wird, um die"
                            + " Suchzeit zu verringern.\n"
                            + "Der Algorithmus verwendet sowohl die realen Pfadkosten als auch die"
                            + " Schätzung der Restkosten \n"
                            + "(Heuristik) eines Knotens. Die Kostenfunktion lautet dabei wie"
                            + " folgt: f(n) = g(n) + h(n).\n"
                            + "Es wird immer ein Weg gefunden (sofern vorhanden) und dieser ist"
                            + " auch immer der optimale Weg.\n"
                            + "Als Datenstruktur zur Speicherung der Pfade wird eine sortierte"
                            + " Queue (Prioritätsqueue) genutzt.\n"
                            + "\n"
                            + "Regeln für die Heuristik:\n"
                            + "Damit die Optimalität und die Vollständigkeit des A* Algorithmus"
                            + " gewährleistet werden können, \n"
                            + "müssen einige Kriterien bezüglich der Heuristik erfüllt sein.\n"
                            + "So ist die Heuristik z.B. nur dann zulässig, wenn für alle Knoten"
                            + " gilt, dass die Schätzung \n"
                            + "der Restkosten (h(n)) kleiner/gleich den tatsächlichen optimalen"
                            + " Restkosten eines Knotens ist.\n"
                            + "Außerdem muss die Heuristik für jeden Knoten größer/gleich 0 sein"
                            + " und für den Zielknoten \n"
                            + "explizit 0 betragen.  \n"
                            + "\n"
                            + "Vorgehensweise\n"
                            + "Die Vorgehensweise des A* Algorithmus wird im Folgenden"
                            + " stichpunktartig beschrieben:\n"
                            + "\n"
                            + "Schritt 1:  Nimm den Startknoten, berechne die Gesamtkosten"
                            + " (bestehend aus realen Pfadkosten \n"
                            + "            und geschätzten Restkosten) für den Knoten und füge ihn"
                            + " in eine sortierte Queue ein. \n"
                            + "Schritt 2:  Nimm den ersten Knoten aus der Queue, kalkuliere die"
                            + " Gesamtkosten für alle Nachbarn \n"
                            + "            und füge die Pfade in die Queue ein\n"
                            + "Schritt 3:  Lösche alle Pfade aus der Queue, deren Gesamtkosten"
                            + " größer oder gleich denen eines \n"
                            + "            anderen Knotens in der Queue sind, sofern beide Pfade"
                            + " auf den gleichen Knoten verweisen \n"
                            + "Schritt 4:  Sortiere die Queue, ausgehend von den Gesamtkosten der"
                            + " Pfade\n"
                            + "Schritt 5:  Überprüfe, ob der erste Knoten in der nun sortierten"
                            + " Queue auf den Zielknoten verweist\n"
                            + "\n"
                            + "Wenn in Schritt 5 festgestellt wird, dass der Zielknoten gefunden"
                            + " wurde, ist der Algorithmus fertig. \n"
                            + "Andernfalls werden die Schritte 2-5 solange durchlaufen, bis der"
                            + " erste Knoten in der \n"
                            + "sortierten Queue auf den Zielknoten verweist oder bis die Queue leer"
                            + " ist.\n");

                currentAlgoExplanation = SupportedAlgorithms.ASTAR;

                break;
            case BESTFIRST:
                this.setText(
                        "Genau wie der A*- und der Branch and Bound-Algorithmus gehört auch der"
                            + " Best First-Algorithmus \n"
                            + "zu den informierten Suchverfahren. Der Best First-Algorithmus setzt"
                            + " lediglich auf\n"
                            + "die Heuristik, was bedeutet, dass die realen Pfadkosten hier \n"
                            + "überhaupt keine Rolle spielen.\n"
                            + "Im Gegensatz zu den A*- und Branch and Bound-Algorithmen ist der"
                            + " Best First-Algorithmus\n"
                            + "weder optimal noch vollständig. Als Datenstruktur zur Speicherung"
                            + " der Pfade wird ebenfalls\n"
                            + "eine sortierte Queue (Prioritätsqueue) genutzt.\n"
                            + "\n"
                            + "Vorgehensweise\n"
                            + "Auch der Best First-Algorithmus verfolgt die gleiche Strategie wie"
                            + " der Branch and Bound-\n"
                            + " und der A*-Algorithmus, mit dem Unterschied, dass hier lediglich"
                            + " auf die Heuristik \n"
                            + "(die Schätzung der Restkosten vom aktuellen Knoten zum Zielknoten)"
                            + " gesetzt wird\n"
                            + "und die realen Pfadkosten keine Rolle spielen.\n\n");

                currentAlgoExplanation = SupportedAlgorithms.BESTFIRST;

                break;
            case BRANCHANDBOUND:
                this.setText(
                        "Der Branch and Bound-Algorithmus gehört ebenfalls zu den informierten"
                            + " Suchverfahren. Im Gegensatz\n"
                            + "zum A*-Algorithmus verwendet der Branch and Bound-Algorithmus"
                            + " ausschließlich die realen Pfadkosten,\n"
                            + "weswegen die Kostenfunktion wie folgt lautet: f(n) = g(n).\n"
                            + "Es wird immer ein Weg gefunden (sofern vorhanden), und dieser ist"
                            + " auch immer der optimale\n"
                            + "(d.h. bestmögliche) Weg. Als Datenstruktur zur Speicherung der Pfade"
                            + " wird eine sortierte Queue\n"
                            + "(Prioritätsqueue) genutzt.\n"
                            + "\n"
                            + "Vorgehensweise\n"
                            + "Die Vorgehensweise des Branch and Bound-Algorithmus ist die Gleiche"
                            + " wie bei dem A*-Algorithmus,\n"
                            + "mit dem Unterschied, dass die Berechnung der Gesamtkosten lediglich"
                            + " aus den realen Pfadkosten\n"
                            + "und nicht aus der Summe der realen Pfadkosten und der Heuristik"
                            + " besteht.\n\n\n\n");

                currentAlgoExplanation = SupportedAlgorithms.BRANCHANDBOUND;

                break;
            case BREADTHFIRST:
                this.setText(
                        "Die Breitensuche ist ein uninformierter Pathfinding-Algorithmus. Das"
                            + " bedeutet, dass der Algorithmus\n"
                            + "keine Angabe zu den Pfadkosten oder den geschätzten Kosten kennt. Er"
                            + " kann auch bei ungewichteten\n"
                            + "Graphen eingesetzt werden. Dabei hat er eine Zeitkomplexität von"
                            + " O(b^(b+1)) und garantiert den\n"
                            + "kürzesten Weg. Es wird mit dem Queue Prinzip gearbeitet, d.h. die"
                            + " Nodes werden nach dem\n"
                            + "FIFO (First in, First out) Schema abgearbeitet.\n"
                            + "\n"
                            + "Vorgehensweise:\n"
                            + "Schritt 1: Setze die Startnode in die Queue\n"
                            + "Schritt 2: Betrachte die benachbarten Nodes und setze sie in die"
                            + " Queue\n"
                            + "Schritt 3: Markiere die betrachtete Node als abgehakt, entferne sie"
                            + " aus der Queue und gehe zur nächsten Node in der Queue\n"
                            + "           (nach FIFO)\n"
                            + "Schritt 4: Bereits besuchte Nodes werden in eine Liste mit den"
                            + " visited Nodes hinzugefügt\n"
                            + "Schritt 5: Von der nächsten Node ausgehend, suche wieder nach"
                            + " benachbarten Nodes und füge sie\n"
                            + "           der Queue hinzu, sie dürfen dabei nicht in der Liste mit"
                            + " den visited Nodes sein\n"
                            + "Schritt 6: Wiederhole Schritt 3-5 so lange, bis alle Nodes"
                            + " abgearbeitet wurden\n"
                            + "Schritt 7: Ist die Queue leer, oder wurde die Endnode gefunden,"
                            + " terminiere das Programm\n");

                currentAlgoExplanation = SupportedAlgorithms.BREADTHFIRST;

                break;
            case DEPTHFIRST:
                this.setText(
                        "Die Tiefensuche ist ein uninformierter Pathfinding Algorithmus. D.h. der"
                            + " \n"
                            + "Algorithmus kennt keine Angabe zu den Pfadkosten oder den"
                            + " geschätzten \n"
                            + "kosten. Dabei hat die Tiefensuche eine Zeitkomplexität von O(|V| +"
                            + " |E|)(V = \n"
                            + "Anzahl der Knoten & E = Anzahl der Kanten) und kann nicht den"
                            + " kürzesten Weg \n"
                            + "garantieren. Die Tiefensuche arbeitet mit Hilfe eines Stacks d.h."
                            + " die Nodes \n"
                            + "werden nach dem LIFO (Last in, First out) Schema abgearbeitet.\n"
                            + "Vorgehensweise\n"
                            + "Schritt 1: Bestimme den Knoten, an dem die Suche beginnen soll\n"
                            + "Schritt 2: Expandiere den Knoten und speichere die Reihenfolge nach"
                            + " den noch nicht erschlossenen Nachfolger in einem Stack\n"
                            + "Schritt 3: Rufe rekursiv für jeden der Knoten in dem Stack auf\n"
                            + "           Falls das gesuchte Element gefunden wurde, brich die"
                            + " Suche ab und liefere ein Ergebnis\n"
                            + "           Falls es keine nicht erschlossenen Nachfolger mehr gibt,"
                            + " lösche den obersten Knoten aus dem Stack \n"
                            + "           und rufe für den jetzt oberen Knoten im Stack DFS auf\n");

                currentAlgoExplanation = SupportedAlgorithms.DEPTHFIRST;

                break;
            case DIJKSTRA:
                this.setText(
                        "Der Dijkstra-Algorithmus ist ein Greedy-Algorithmus. Er wird benutzt, um"
                            + " den kürzesten bzw.\n"
                            + "kostengünstigsten Weg zu ermitteln. Er kann rekursiv geschrieben"
                            + " werden. \n"
                            + "Da der Dijkstra-Algorithmus alle Nodes bei der Berechnung des"
                            + " kostengünstigsten Pfades in Betracht \n"
                            + "zieht, kann er vergleichsweise langsam sein. Er wird am besten bei"
                            + " wenigen bis mittelhohen \n"
                            + "Pfadmöglichkeiten angewendet. Bei der Berechnung von Distanzen, z.B."
                            + " innerhalb von Straßennetzwerken, \n"
                            + "ist er demnach sehr nützlich.\n"
                            + "\n"
                            + "Vorgehensweise:\n"
                            + "Schritt 1: Die Distanz zu allen Nodes ausgehend von der Startnode"
                            + " wird auf unendlich gesetzt\n"
                            + "Schritt 2: Es werden die Kosten zu den benachbarten Nodes ausgehend"
                            + " von der Startnode berechnet\n"
                            + "Schritt 3: Die Startnode wird als bearbeitet abgehakt, als nächstes"
                            + " wird die Node mit dem\n"
                            + "           kürzesten Weg zur Startnode betrachtet\n"
                            + "Schritt 4: Ausgehend von der neuen Node werden alle mit dieser Node"
                            + " verbundenen Pfade neu berechnet\n"
                            + "Schritt 5: Als nächstes wird wieder eine weitere Node, welche den"
                            + " nächstkürzeren Pfad zur Startnode\n"
                            + "           darstellt, betrachtet und Schritt 4 & 5 werden"
                            + " wiederholt, bis alle mit der Startnode\n"
                            + "           verbundenen Nodes abgearbeitet wurden. Danach gelten"
                            + " diese als abgehakt und Schritt 3\n"
                            + "           wiederholt sich, aber nur diesmal ausgehend von den neuen"
                            + " abgehakten Nodes\n"
                            + "Schritt 6: Schritt 3-5 werden so oft wiederholt, bis alle Nodes im"
                            + " System abgearbeitet wurden.\n"
                            + "           Das Programm terminiert nur dann, wenn alle möglichen"
                            + " Optionen berechnet wurden,\n"
                            + "           bzw. es durch alle Iterationen gegangen ist.\n"
                            + "Dies garantiert einen kürzesten Pfad.\n");

                currentAlgoExplanation = SupportedAlgorithms.DIJKSTRA;

                break;
        }
    }

    public SupportedAlgorithms getCurrentAlgoExplanation() {
        return currentAlgoExplanation;
    }
}
