package pl.anicos.nmp.json

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import pl.anicos.nmp.ResourceProvider
import pl.anicos.nmp.json.exception.JsonModelValidateException
import pl.anicos.nmp.publish.model.NpmPublishBody
import spock.lang.Specification

import javax.validation.Validation
import javax.validation.ValidatorFactory

class ObjectMapperWrapperValidationSpec extends Specification {
    ObjectMapper objectMapper = new ObjectMapper();
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    ObjectMapperValidator objectMapperValidator = new ObjectMapperValidator(factory.getValidator())
    ObjectMapperWrapper testObj = new ObjectMapperWrapper(objectMapper, objectMapperValidator)

    def 'should validate body model '() {
        when:
        String content = ResourceProvider.getText(getClass(), jsonFile)
        JsonNode tree = objectMapper.readTree(content)

        testObj.treeToValue(tree, NpmPublishBody)

        then:
        JsonModelValidateException exception = thrown();
        exception.getMessage() == message

        where:
        jsonFile                        | message
        'withoutId.json'                | "ConstraintViolationImpl{interpolatedMessage='may not be empty', propertyPath=id, rootBeanClass=class pl.anicos.nmp.publish.model.NpmPublishBody, messageTemplate='{org.hibernate.validator.constraints.NotEmpty.message}'}"
        'withoutDistTags.json'          | "ConstraintViolationImpl{interpolatedMessage='may not be null', propertyPath=distTags, rootBeanClass=class pl.anicos.nmp.publish.model.NpmPublishBody, messageTemplate='{javax.validation.constraints.NotNull.message}'}"
        'withoutDistTagsLatest.json'    | "ConstraintViolationImpl{interpolatedMessage='may not be empty', propertyPath=distTags.latest, rootBeanClass=class pl.anicos.nmp.publish.model.NpmPublishBody, messageTemplate='{org.hibernate.validator.constraints.NotEmpty.message}'}"
        'withoutAttachments.json'       | "ConstraintViolationImpl{interpolatedMessage='may not be empty', propertyPath=attachments, rootBeanClass=class pl.anicos.nmp.publish.model.NpmPublishBody, messageTemplate='{org.hibernate.validator.constraints.NotEmpty.message}'}"
        'withTooMuchAttachments.json'   | "ConstraintViolationImpl{interpolatedMessage='size must be between 0 and 1', propertyPath=attachments, rootBeanClass=class pl.anicos.nmp.publish.model.NpmPublishBody, messageTemplate='{javax.validation.constraints.Size.message}'}"
        'withoutDataInAttachments.json' | "ConstraintViolationImpl{interpolatedMessage='may not be empty', propertyPath=attachments[anicosnpm-1.12.0.tgz].data, rootBeanClass=class pl.anicos.nmp.publish.model.NpmPublishBody, messageTemplate='{org.hibernate.validator.constraints.NotEmpty.message}'}"
    }
}
