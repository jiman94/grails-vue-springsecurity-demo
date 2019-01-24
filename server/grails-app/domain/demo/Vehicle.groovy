package demo

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.Resource
import groovy.transform.CompileStatic

@CompileStatic
@Secured(['ROLE_DRIVER'])
@Resource(uri = '/api/vehicle')
class Vehicle {

    String name

    Make make
    Model model

    static belongsTo = [driver: Driver]

    static constraints = {
        //name unique: true
    }

    String toString() {
        return "Vehicle name: ${name} Make: ${make.name} Model: ${model.name} Driver: ${driver.name}"
    }
}
