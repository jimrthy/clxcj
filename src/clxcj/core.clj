(ns clxcj.core
  (:require [net.n01se.clojure-jna :as jna])
  (:import [com.sun.jna Native Pointer Structure]
           [java.nio Buffer]))

;;; TODO: How do I specify what's in the lxc_container?
;;; This is something that's used heavily
 
;;; c.f. https://qa.linuxcontainers.org/master/current/doc/api/lxccontainer_8h.html
;;; for the actual docs.
(jna/to-ns native-lxc lxc [Structure lxc_container_new  ; Create new container
                           Integer lxc_container_get  ; Add reference to container
                           Integer lxc_container_put  ; Drop reference to container
                           Integer lxc_get_wait_states ; Obtain list of all container states
                           String lxc_get_global_config_item ; Get value for a global config key
                           String lxc_get_version ; Determine version of LXC
                           ;; These get funkier, dealing with deeply nested pointers
                           Integer list_defined_containers ; Gets list of defined containers for an lxcpath
                           Integer list_active_containers ; Gets list af active containers for an lxcpath
                           Integer list_all_containers ; Gets complete list of containers for an lxcpath
                           Void lxc_log_close ; Close log file
                           ])

(defn get-version
  []
  (let [v-str (native-lxc/lxc_get_version)]
    ;; TODO: Parse this into major.minor.build
    v-str))

(defn list-containers
  [^String path ^Buffer names ^Pointer containers]
  (let [n (native-lxc/list_all_containers names containers)]
    (if (> n 0)
      (throw (RuntimeException. "Deal with success"))
      (throw (RuntimeException. "Failed to retrieve list")))))

