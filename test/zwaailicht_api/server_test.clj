(ns zwaailicht-api.server_test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [zwaailicht-api.server :refer :all]))

(deftest test-app
  (testing "get on main route"
    (let [response (http-handler (mock/request :get "/"))]
      (is (= 200 (:status response)))
      (is (= "off" (:body response)))))

  (testing "post on main route"
    (let [set-state-calls (atom [])]
      (with-redefs [zwaailicht-api.serial/set-state! (fn [arg] (swap! set-state-calls conj arg))]
        (let [response (http-handler (-> (mock/request :post "/")
                                         (mock/body "on")))]
          (is (= 200 (:status response)))
          (is (not-empty @set-state-calls))))))

  (testing "not-found route"
    (let [response (http-handler (mock/request :get "/invalid"))]
      (is (= 404 (:status response))))))
