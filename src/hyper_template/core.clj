(ns hyper-template.core)

(defn hstr-parameter [key-fns template-keys bindings]
  (let [keys-cnt (count template-keys)
        first-key (-> template-keys first keyword)]

    (if (= 1 keys-cnt)
      (bindings first-key)
      (let [f (key-fns first-key)
            snd-key (-> template-keys second keyword)
            rest-keys (-> template-keys rest rest)
            ]
        (f (bindings snd-key) rest-keys)
        ))))

(defn hstr [rtn-fn kei-fns template-parts bindings]
  (rtn-fn 
    (for [part template-parts]
      (condp instance? part
        clojure.lang.Keyword 
        (hstr-parameter kei-fns
                        (.split (name part) ":") 
                        bindings)

        part))))

