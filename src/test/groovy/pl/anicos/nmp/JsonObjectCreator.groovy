package pl.anicos.nmp

import groovy.json.JsonSlurper

class JsonObjectCreator {

    def static toJson(String json) {
        return new JsonSlurper().parseText(json)
    }
}
