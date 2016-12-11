package pl.anicos.nmp

class ResourceProvider {

    static def getText(Class<?> clazz, String resourceName) {
        return new File(clazz.getResource(resourceName).toURI()).getText('UTF-8')
    }

    static def getUri(Class<?> clazz, String resourceName) {
        return clazz.getResource(resourceName).toURI()
    }
}
