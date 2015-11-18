(ns lojo.core
  (:import (javax.swing JFrame JPanel)
           (java.awt Dimension Color RenderingHints)
           (java.awt.event ComponentAdapter)))

(defonce panel* (atom 0))
(defonce g* (atom 0))

(defonce turtle* (atom {:x 0
                        :y 0
                        :theta 0
                        :pen true}))

(defn home
  []
  (reset! turtle* {:x 0
                   :y 0
                   :theta 0
                   :pen true}))

(defn left
  [t]
  (swap! turtle* assoc
         :theta (+ (:theta @turtle*)
                   (* 2 Math/PI (/ t 360)))))

(defn right
  [t]
  (left (- t)))

(defn- round
  [n]
  (double
   (/ (Math/round (* n 1000)) 1000)))

(defn forward
  [l]
  (let [old-x (:x @turtle*)
        old-y (:y @turtle*)
        t (:theta @turtle*)
        new-x (+ old-x (* (Math/cos t) l))
        new-y (+ old-y (* (Math/sin t) l))]
    (when (:pen @turtle*)
      (.drawLine @g* old-x old-y new-x new-y))
    (swap! turtle* assoc :x (round new-x) :y (round new-y))))

(defn back
  [l]
  (forward (- l)))

(defn penup
  []
  (swap! turtle* assoc :pen false))

(defn pendown
  []
  (swap! turtle* assoc :pen true))

(defn penswap
  []
  (swap! turtle* update-in [:pen] not))

(defn clear
  []
  (let [w (.getWidth @panel*)
        h (.getHeight @panel*)]
    (.setColor @g* (Color. 0.9 0.9 0.9))
    (.fillRect @g* (- (/ w 2)) (- (/ h 2)) w h)
    (.setColor @g* (Color. 0.2 0.2 0.2))))

(defn- make-panel
  []
  (let [p (proxy [JPanel] []
            (getPreferredSize [] (Dimension. 800 450)))]
    (reset! panel* p)
    p))

(defn start
  []
  (let [l (proxy [ComponentAdapter] []
            (componentResized [e]
              (let [dim (-> e .getSource .getSize)
                    w (.getWidth dim)
                    h (.getHeight dim)]
                (println "Resized to:" w h)
                (reset! g* (.getGraphics @panel*))
                (.translate @g* (/ w 2.0) (/ h 2.0))
                (.scale @g* 1 -1)
                (.setRenderingHints @g* (RenderingHints.
                                         RenderingHints/KEY_ANTIALIASING
                                         RenderingHints/VALUE_ANTIALIAS_ON))
                (clear))))]
    (doto (JFrame. "LOJO")
            (.addComponentListener l)
            (.add (make-panel))
            (.pack)
            (.setVisible true))
    :ok))
