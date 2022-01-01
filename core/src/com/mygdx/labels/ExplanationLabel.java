package com.mygdx.labels;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.SupportedAlgorithms;

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

    public void selectAlgorithmDescription(SupportedAlgorithms algorithm) {

        if (currentAlgoExplanation == algorithm) { return; }

        switch(algorithm) {

            case ASTAR:
                this.setText("Der A* Algorithmus gehört zu den informierten Suchverfahren, was bedeutet, dass er über Wissen \nbezüglich des Suchraums verfügt, welches genutzt wird um die Suchzeit zu verringern.\n" +
                    "Der Algorithmus verwendet sowohl die realen Pfadkosten als auch die Schätzung der Restkosten \n(Heuristik) eines Knotens. Die Kostenfunktion lautet dabei wie folgt: f(n) = g(n) + h(n)\n" +
                    "Es wird immer ein Weg gefunden (sofern vorhanden) und dieser ist auch immer der optimale Weg\n" +
                    "Als Datenstruktur zur Speicherung der Pfade wird eine sortierte Queue (Prioritätsqueue genutzt)\n" +
                    "Regeln für die Heuristik:\n" +
                    "Damit die Optimalität und die Vollständigkeit des A* Algorithmus gewährleistet werden können, \nmüssen einige Kriterien bezüglich der Heuristik erfüllt sein.\n" +
                    "So ist die Heuristik z.B. nur dann zulässig, wenn für alle Knoten gilt, dass die Schätzung \nder Restkosten (h(n)) kleiner/gleich den tatsächlichen optimalen Restkosten eines Knotens ist.\n" +
                    "Außerdem muss die Heuristik für jeden Knoten größer/gleich 0 sein und für den Zielknoten \nexplizit 0 betragen.  \n" +
                    "Vorgehensweise\n" +
                    "Die Vorgehensweise des A* Algorithmus wird im Folgenden Stichpunktartig beschrieben:\n" +
                    "\nSchritt 1:  Nimm den Startknoten, berechne die Gesamtkosten (bestehend aus realen Pfadkosten " +
                    "\n            und geschätzten Restkosten) für den Knoten und füge ihn in eine sortierte Queue ein. \n" +
                    "Schritt 2:  Nimm den ersten Knoten aus der Queue, kalkuliere die Gesamtkosten für alle Nachbarn " +
                    "\n            und füge die Pfade in die Queue ein\n" +
                    "Schritt 3:  Lösche alle Pfade aus der Queue, deren Gesamtkosten größer oder gleich denen eines " +
                    "\n            anderen Knotens in der Queue sind, sofern beide Pfade auf den gleichen Knoten verweisen \n" +
                    "Schritt 4:  Sortiere die Queue, ausgehend von den Gesamtkosten der Pfade\n" +
                    "Schritt 5:  Überprüfe ob der erste Knoten in der nun sortierten Queue auf den Zielknoten verweist\n" +
                    "\nWenn in Schritt 5 festgestellt wird, dass der Zielknoten gefunden wurde ist der Algorithmus fertig. " +
                    "\nAndernfalls werden die Schritte 2-5 solange durchlaufen, bis der erste Knoten in der " +
                    "\nsortierten Queue auf den Zielknoten verweist oder bis die Queue leer ist.\n");

                currentAlgoExplanation = SupportedAlgorithms.ASTAR;

                break;
            case BESTFIRST:
                this.setText("Best First Best First Best First Best First Best First \n Best First Best First Best First Best First \n Best First Best First Best First Best First");

                currentAlgoExplanation = SupportedAlgorithms.BESTFIRST;

                break;
            case BRANCHANDBOUND:
                this.setText("[insert Branch and Bound text here]");

                currentAlgoExplanation = SupportedAlgorithms.BRANCHANDBOUND;

                break;
            case BREADTHFIRST:
                this.setText("[insert Breadth First text here]");

                currentAlgoExplanation = SupportedAlgorithms.BREADTHFIRST;

                break;
            case DEPTHFIRST:
                this.setText("[insert Depth First text here]");

                currentAlgoExplanation = SupportedAlgorithms.DEPTHFIRST;

                break;
            case DIJKSTRA:
                this.setText("[insert Dijkstra text here]");

                currentAlgoExplanation = SupportedAlgorithms.DIJKSTRA;

                break;
        }
    }

    public SupportedAlgorithms getCurrentAlgoExplanation() {
        return currentAlgoExplanation;
    }

}
