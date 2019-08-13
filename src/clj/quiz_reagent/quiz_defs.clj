(ns quiz-reagent.quiz-defs)

(def quizes {1 {:id        1
                :name      "Karneval I"
                :summary   "Teil 1 - Karnevalsquiz anlässlich des 11-jährigen Jubiläums der EAG"
                :img-url   "http://placehold.it/230x200"
                :questions {1 {:id             1
                               :question       "Wessen Schminke hatte die Yvonne/Stefanie schon im Gesicht?"
                               :answers        [{:id "a", :text "Steffen Rienekker"}
                                                {:id "b", :text "Holger Kiehm"}
                                                {:id "c", :text "Frederic Türk"}
                                                {:id "d", :text "alle 3 zusammen"}
                                                ]
                               :correct-answer "d"
                               :selection      nil
                               :submitted      false}
                            2 {:id             1
                               :question       "Geht's raus, spuilts Fußball!"
                               :answers        [{:id "a", :text "Pele"}
                                                {:id "b", :text "Beckenbauer"}]
                               :correct-answer "b"
                               :selection      nil
                               :submitted      false}
                            }}
             2 {:id        2
                :name      "Karneval II"
                :summary   "Teil 2 unseres phänomenalen Karnevalsquiz'"
                :img-url   "http://placehold.it/230x200"
                :questions {1 {:id             1
                               :question       "Do. Or do not. There is no try."
                               :answers        [{:id "a", :text "Hamlet"}
                                                {:id "b", :text "Yoda"}
                                                {:id "c", :text "Dalai Lama"}]
                               :correct-answer "b"
                               :selection      nil
                               :submitted      false}
                            2 {:id             2
                               :question       "Fear is the path to the dark side. Fear leads to anger, anger leads to hate, hate leads to suffering."
                               :answers        [{:id "a", :text "Yoda"}
                                                {:id "b", :text "Prince P"}
                                                {:id "c", :text "Batman"}]
                               :correct-answer "a"
                               :selection      nil
                               :submitted      false}
                            3 {:id             3
                               :question       "How said this: \"If my calculations are correct, when this baby hits 88 miles per hour... you're gonna see some serious shit.\""
                               :answers        [{:id "a", :text "Dart Vader"}
                                                {:id "b", :text "Doc Brown"}
                                                {:id "c", :text "Captain America"}]
                               :correct-answer "b"
                               :selection      nil
                               :submitted      false}}}
             3 {:id        3
                :name      "Sports"
                :summary   "Dapibus ac facilisis in, egestas eget donec id elit non mi porta. Nullam id dolor id nibh ultricies vehicula ut id elit."
                :img-url   "http://placehold.it/230x200"
                :questions {1 {:id             1
                               :question       "Geht's raus, spuilts Fußball!"
                               :answers        [{:id "a", :text "Pele"}
                                                {:id "b", :text "Beckenbauer"}]
                               :correct-answer "b"
                               :selection      nil
                               :submitted      false}
                            2 {:id             2
                               :question       "Fear is the path to the dark side. Fear leads to anger, anger leads to hate, hate leads to suffering."
                               :answers        [{:id "a", :text "Yoda"}
                                                {:id "b", :text "Prince P"}
                                                {:id "c", :text "Batman"}]
                               :correct-answer "a"
                               :selection      nil
                               :submitted      false}}}
             4 {:id        4
                :name      "Music"
                :summary   "Stet clita kasd gubergren lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat."
                :img-url   "http://placehold.it/230x200"
                :questions {1 {:id             1
                               :question       "Fear is the path to the dark side. Fear leads to anger, anger leads to hate, hate leads to suffering."
                               :answers        [{:id "a", :text "Yoda"}
                                                {:id "b", :text "Prince P"}
                                                {:id "c", :text "Batman"}]
                               :correct-answer "a"
                               :selection      nil
                               :submitted      false}}}
             5 {:id        5
                :name      "Philosophy"
                :summary   "Nullam id dolor id nibh ultricies vehicula ut id elit. Dapibus ac facilisis in wers, isot culum manim."
                :img-url   "http://placehold.it/230x200"
                :questions {1 {:id             2
                               :question       "Fear is the path to the dark side. Fear leads to anger, anger leads to hate, hate leads to suffering."
                               :answers        [{:id "a", :text "Yoda"}
                                                {:id "b", :text "Prince P"}
                                                {:id "c", :text "Batman"}]
                               :correct-answer "a"
                               :selection      nil
                               :submitted      false}
                            2 {:id             1
                               :question       "Geht's raus, spuilts Fußball!"
                               :answers        [{:id "a", :text "Pele"}
                                                {:id "b", :text "Beckenbauer"}]
                               :correct-answer "b"
                               :selection      nil
                               :submitted      false}
                            }}
             6 {:id        6
                :name      "Movie quotes"
                :summary   "Cras justo odio, dapibus ac facilisis in egestas. Donec id elit non mi porta gravida at eget metus. Nullam id dolor id nibh ultricies vehicula ut id elit."
                :img-url   "http://placehold.it/230x200"
                :questions {1 {:id             1
                               :question       "Do. Or do not. There is no try."
                               :answers        [{:id "a", :text "Hamlet"}
                                                {:id "b", :text "Yoda"}
                                                {:id "c", :text "Dalai Lama"}]
                               :correct-answer "b"
                               :selection      nil
                               :submitted      false}
                            2 {:id             2
                               :question       "Fear is the path to the dark side. Fear leads to anger, anger leads to hate, hate leads to suffering."
                               :answers        [{:id "a", :text "Yoda"}
                                                {:id "b", :text "Prince P"}
                                                {:id "c", :text "Batman"}]
                               :correct-answer "a"
                               :selection      nil
                               :submitted      false}
                            3 {:id             3
                               :question       "How said this: \"If my calculations are correct, when this baby hits 88 miles per hour... you're gonna see some serious shit.\""
                               :answers        [{:id "a", :text "Dart Vader"}
                                                {:id "b", :text "Doc Brown"}
                                                {:id "c", :text "Captain America"}]
                               :correct-answer "b"
                               :selection      nil
                               :submitted      false}}}
             })

(defn sorted [quiz-map]
  (into (sorted-map-by <) quiz-map))
