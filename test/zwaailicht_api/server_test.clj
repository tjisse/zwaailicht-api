(ns zwaailicht-api.server_test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [zwaailicht-api.server :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response (http-handler (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Hello World"))))

  (testing "not-found route"
    (let [response (http-handler (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
